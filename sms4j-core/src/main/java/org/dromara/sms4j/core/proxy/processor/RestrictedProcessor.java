package org.dromara.sms4j.core.proxy.processor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.CoreMethodProcessor;
import org.dromara.sms4j.api.proxy.aware.SmsDaoAware;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BeanFactory;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;


/**
 * 短信发送账号级上限前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Setter
@Slf4j
public class RestrictedProcessor implements CoreMethodProcessor, SmsDaoAware {
    static Long minTimer = 60 * 1000L;
    private static final String REDIS_KEY = "sms:restricted:";

    /**
     * 缓存实例
     */
    private SmsDao smsDao;

    @Override
    public int getOrder() {
        return 3;
    }

    @Override
    public void sendMessagePreProcess(String phone, Object message) {
        doRestricted(Collections.singletonList(phone));
    }

    @Override
    public void sendMessageByTemplatePreProcess(String phone, String templateId, LinkedHashMap<String, String> messages) {
        doRestricted(Collections.singletonList(phone));
    }

    @Override
    public void massTextingPreProcess(List<String> phones, String message) {
        doRestricted(phones);
    }

    @Override
    public void massTextingByTemplatePreProcess(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        doRestricted(phones);
    }

    private long calculateExpiryTime() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrowMidnight = now.toLocalDate().plusDays(1).atStartOfDay();
        Duration duration = Duration.between(now, tomorrowMidnight);
        return duration.getSeconds();
    }

    public void doRestricted(List<String> phones) {
        if (Objects.isNull(smsDao)) {
            throw new SmsBlendException("The smsDao tool could not be found");
        }
        SmsConfig config = BeanFactory.getSmsConfig();
        // 每日最大发送量
        Integer accountMax = config.getAccountMax();
        // 每分钟最大发送量
        Integer minuteMax = config.getMinuteMax();
        // 配置了每日最大发送量
        boolean dailyMaxLimitExists = SmsUtils.isNotEmpty(accountMax);
        // 配置了每分钟最大发送量
        boolean perMinuteLimitExists = SmsUtils.isNotEmpty(minuteMax);
        // 如果未开启限制或未配置任何限制发送量，不做处理
        boolean isNoProcessing = !config.getRestricted() || (!dailyMaxLimitExists && !perMinuteLimitExists);
        if (isNoProcessing) {
            return;
        }
        for (String phone : phones) {
            // 分钟发送量缓存key
            String minuteMaxKey = REDIS_KEY + phone;
            // 天发送量缓存key
            String accountMaxKey = minuteMaxKey.concat("max");
            // 是否配置了每日限制
            if (dailyMaxLimitExists) {
                Integer dailyCount = (Integer) smsDao.get(accountMaxKey);
                if (SmsUtils.isEmpty(dailyCount)) {
                    smsDao.set(accountMaxKey, 1, calculateExpiryTime());
                } else if (dailyCount >= accountMax) {
                    log.info("The phone: {} number of short messages reached the maximum today", phone);
                    throw new SmsBlendException(String.format("The phone: %s number of short messages reached the maximum today", phone));
                } else {
                    smsDao.set(accountMaxKey, dailyCount + 1, calculateExpiryTime());
                }
            }
            // 是否配置了每分钟最大限制
            if (perMinuteLimitExists) {
                Integer minuteCount = (Integer) smsDao.get(REDIS_KEY + phone);
                if (SmsUtils.isNotEmpty(minuteCount)) {
                    if (minuteCount < minuteMax) {
                        smsDao.set(minuteMaxKey, minuteCount + 1, minTimer / 1000);
                    } else {
                        //如果能走到这里且存在每日限制，说明每日限制已经计数，这里将之前的计数减一次
                        if (dailyMaxLimitExists) {
                            Integer dailyCount = (Integer) smsDao.get(accountMaxKey);
                            if (dailyCount > 1) {
                                smsDao.set(accountMaxKey, dailyCount - 1, calculateExpiryTime());
                            } else {
                                smsDao.remove(accountMaxKey);
                            }
                        }
                        log.info("The phone: {} text messages are sent too often！", phone);
                        throw new SmsBlendException(String.format("The phone: %s text messages are sent too often！", phone));
                    }
                } else {
                    smsDao.set(minuteMaxKey, 1, minTimer / 1000);
                }
            }
        }
    }

    /**
     * 发送过程异常时回滚本次已计入的发送次数，避免失败的发送占用每日/每分钟限额
     */
    @Override
    public void exceptionHandleProcessor(Method method, Object source, Object[] param, Exception exception) {
        try {
            rollbackRestricted(extractPhones(param));
        } catch (Exception e) {
            log.warn("短信发送失败后回滚发送次数失败: {}", e.getMessage());
        }
        throw new SmsBlendException(exception);
    }

    private static List<String> extractPhones(Object[] param) {
        if (param == null || param.length == 0 || param[0] == null) {
            return Collections.emptyList();
        }
        if (param[0] instanceof List) {
            return (List<String>) param[0];
        }
        if (param[0] instanceof String) {
            return Collections.singletonList((String) param[0]);
        }
        return Collections.emptyList();
    }

    private void rollbackRestricted(List<String> phones) {
        if (Objects.isNull(smsDao) || phones.isEmpty()) {
            return;
        }
        SmsConfig config = BeanFactory.getSmsConfig();
        boolean dailyMaxLimitExists = SmsUtils.isNotEmpty(config.getAccountMax());
        boolean perMinuteLimitExists = SmsUtils.isNotEmpty(config.getMinuteMax());
        if (!config.getRestricted() || (!dailyMaxLimitExists && !perMinuteLimitExists)) {
            return;
        }
        for (String phone : phones) {
            String minuteMaxKey = REDIS_KEY + phone;
            String accountMaxKey = minuteMaxKey.concat("max");
            if (dailyMaxLimitExists) {
                Integer dailyCount = (Integer) smsDao.get(accountMaxKey);
                if (SmsUtils.isNotEmpty(dailyCount)) {
                    if (dailyCount > 1) {
                        smsDao.set(accountMaxKey, dailyCount - 1, calculateExpiryTime());
                    } else {
                        smsDao.remove(accountMaxKey);
                    }
                }
            }
            if (perMinuteLimitExists) {
                Integer minuteCount = (Integer) smsDao.get(minuteMaxKey);
                if (SmsUtils.isNotEmpty(minuteCount)) {
                    if (minuteCount > 1) {
                        smsDao.set(minuteMaxKey, minuteCount - 1, minTimer / 1000);
                    } else {
                        smsDao.remove(minuteMaxKey);
                    }
                }
            }
        }
    }
}
