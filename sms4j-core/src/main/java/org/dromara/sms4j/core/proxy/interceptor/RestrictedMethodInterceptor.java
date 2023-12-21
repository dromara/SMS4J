package org.dromara.sms4j.core.proxy.interceptor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.AbstractGenericMethodInterceptor;
import org.dromara.sms4j.api.proxy.aware.SmsDaoAware;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;
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
@Slf4j
public class RestrictedMethodInterceptor extends AbstractGenericMethodInterceptor implements SmsDaoAware {

    /**
     * 每分钟最多可发送条数
     * TODO 每分钟最多可发送条数应当可配置
     */
    private static final Long MINUTE_MAXIMUM_COUNT = 60 * 1000L;
    /**
     * 每个账号最多可发送条数
     * TODO 每个账号最多可发送条数应当可配置
     */
    private static final Long ACCOUNT_MAXIMUM_COUNT = 24 * 60 * 60 * 1000L;
    /**
     * redis缓存前缀
     * TODO redis缓存前缀应当可配置
     */
    private static final String REDIS_KEY = "sms:restricted:";

    /**
     * 缓存实例
     */
    @Setter
    private SmsDao smsDao;

    @Override
    public int getOrder() {
        return SmsProxyFactory.RESTRICTED_METHOD_INTERCEPTOR;
    }

    @Override
    public void beforeSendMessage(String phone, String message) {
        restricted(Collections.singletonList(phone));
    }

    @Override
    protected void beforeSendMessageWithTemplate(String phone, LinkedHashMap<String, String> messages) {
        restricted(Collections.singletonList(phone));
    }

    @Override
    public void beforeSendMessageWithCustomTemplate(String phone, String templateId, LinkedHashMap<String, String> messages) {
        restricted(Collections.singletonList(phone));
    }

    @Override
    public void beforeMassTexting(List<String> phones, String message) {
        restricted(phones);
    }

    @Override
    public void beforeMassTextingWithTemplate(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        restricted(phones);
    }

    public void restricted(List<String> phones) {
        if (Objects.isNull(smsDao)) {
            throw new SmsBlendException("The dao tool could not be found");
        }
        SmsConfig config = BeanFactory.getSmsConfig();
        // 每日最大发送量
        Integer accountMax = config.getAccountMax();
        // 每分钟最大发送量
        Integer minuteMax = config.getMinuteMax();
        for (String phone : phones) {
            doRestricted(accountMax, minuteMax, phone);
        }
    }

    private void doRestricted(Integer accountMax, Integer minuteMax, String phone) {
        // 是否配置了每日限制
        if (SmsUtils.isNotEmpty(accountMax)) {
            checkAccountMax(accountMax, phone);
        }
        // 是否配置了每分钟最大限制
        if (SmsUtils.isNotEmpty(minuteMax)) {
            Integer count = (Integer) smsDao.get(REDIS_KEY + phone);
            checkMinuteMax(minuteMax, phone, count);
        }
    }

    private void checkMinuteMax(Integer minuteMax, String phone, Integer count) {
        if (SmsUtils.isEmpty(count)) {
            smsDao.set(REDIS_KEY + phone, 1, MINUTE_MAXIMUM_COUNT / 1000);
            return;
        }
        if (count < minuteMax) {
            smsDao.set(REDIS_KEY + phone, count + 1, MINUTE_MAXIMUM_COUNT / 1000);
            return;
        }
        log.info("The phone: {}, number of short messages reached the maximum today", phone);
        throw new SmsBlendException("The phone:", phone + " Text messages are sent too often！");
    }

    private void checkAccountMax(Integer accountMax, String phone) {
        Integer i = (Integer) smsDao.get(REDIS_KEY + phone + "max");
        if (SmsUtils.isEmpty(i)) {
            smsDao.set(REDIS_KEY + phone + "max", 1, ACCOUNT_MAXIMUM_COUNT / 1000);
            return;
        }
        if (i >= accountMax) {
            log.info("The phone: {}, number of short messages reached the maximum today", phone);
            throw new SmsBlendException("The phone:" + phone + ",number of short messages reached the maximum today");
        }
        smsDao.set(REDIS_KEY + phone + "max", i + 1, ACCOUNT_MAXIMUM_COUNT / 1000);
    }
}
