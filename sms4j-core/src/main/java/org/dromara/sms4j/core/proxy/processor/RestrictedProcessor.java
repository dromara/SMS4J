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
    static Long accTimer = 24 * 60 * 60 * 1000L;
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

    public void doRestricted(List<String> phones) {
        if (Objects.isNull(smsDao)) {
            throw new SmsBlendException("The smsDao tool could not be found");
        }
        SmsConfig config = BeanFactory.getSmsConfig();
        // 如果未开始限制则不做处理
        if (!config.getRestricted()){
            return;
        }
        Integer accountMax = config.getAccountMax(); // 每日最大发送量
        Integer minuteMax = config.getMinuteMax(); // 每分钟最大发送量
        for (String phone : phones) {
            if (SmsUtils.isNotEmpty(accountMax)) {   // 是否配置了每日限制
                Integer i = (Integer) smsDao.get(REDIS_KEY + phone + "max");
                if (SmsUtils.isEmpty(i)) {
                    smsDao.set(REDIS_KEY + phone + "max", 1, accTimer / 1000);
                } else if (i >= accountMax) {
                    log.info("The phone: {},number of short messages reached the maximum today", phone);
                    throw new SmsBlendException("The phone: {},number of short messages reached the maximum today", phone);
                } else {
                    smsDao.set(REDIS_KEY + phone + "max", i + 1, accTimer / 1000);
                }
            }
            if (SmsUtils.isNotEmpty(minuteMax)) {  // 是否配置了每分钟最大限制
                Integer o = (Integer) smsDao.get(REDIS_KEY + phone);
                if (SmsUtils.isNotEmpty(o)) {
                    if (o < minuteMax) {
                        smsDao.set(REDIS_KEY + phone, o + 1, minTimer / 1000);
                    } else {
                        log.info("The phone: {},number of short messages reached the maximum today", phone);
                        throw new SmsBlendException("The phone: {} Text messages are sent too often！", phone);
                    }
                } else {
                    smsDao.set(REDIS_KEY + phone, 1, minTimer / 1000);
                }
            }
        }
    }
}
