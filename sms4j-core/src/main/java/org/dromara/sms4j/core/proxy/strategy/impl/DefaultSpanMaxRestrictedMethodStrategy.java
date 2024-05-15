package org.dromara.sms4j.core.proxy.strategy.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsDaoAware;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.proxy.interceptor.SpanMaxRestrictedMethodInterceptor;
import org.dromara.sms4j.core.proxy.strategy.IRestrictedMethodStrategy;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BeanFactory;

import java.util.List;
import java.util.Objects;

/**
 * 短信发送时间级上限拦截策略
 *
 * @author sh1yu
 * @since 2023/12/22 13:03
 */
@Slf4j
public class DefaultSpanMaxRestrictedMethodStrategy implements IRestrictedMethodStrategy, InterceptorStrategySmsDaoAware {

    @Setter
    SmsDao smsDao;

    /**
     * 每分钟最多可发送条数
     * TODO 每分钟最多可发送条数应当可配置
     */
    private static final Long MINUTE_MAXIMUM_COUNT = 60 * 1000L;
    /**
     * redis缓存前缀
     * TODO redis缓存前缀应当可配置
     */
    private static final String REDIS_KEY = "sms:restricted:";
    private static final String REDIS_KEY_FIX = "span:";

    public void restricted(List<String> phones) {
        if (Objects.isNull(smsDao)) {
            throw new SmsBlendException("The dao tool could not be found");
        }
        SmsConfig config = BeanFactory.getSmsConfig();
        // 每分钟最大发送量
        Integer minuteMax = config.getMinuteMax();
        for (String phone : phones) {
            doRestricted(minuteMax, phone);
        }
    }

    public void doRestricted(Integer minuteMax, String phone) {
        // 是否配置了每分钟最大限制
        if (SmsUtils.isNotEmpty(minuteMax)) {
            Integer count = (Integer) smsDao.get(REDIS_KEY + phone + REDIS_KEY_FIX);
            if (SmsUtils.isNotEmpty(count) && minuteMax <= count) {
                log.info("The phone: {}, number of short messages reached the maximum today", phone);
                throw new SmsBlendException("The phone:", phone + " Text messages are sent too often！");
            }
        }
    }

    public void flushRecord(List<String> phones) {
        SmsConfig config = BeanFactory.getSmsConfig();
        // 每分钟最大发送量
        Integer minuteMax = config.getMinuteMax();
        for (String phone : phones) {
            doFlushRecord(minuteMax, phone);
        }
    }

    public void doFlushRecord(Integer minuteMax, String phone) {
        if (SmsUtils.isNotEmpty(minuteMax)) {
            Integer count = (Integer) smsDao.get(REDIS_KEY + phone+ REDIS_KEY_FIX);
            if (SmsUtils.isEmpty(count)) {
                count = 0;
            }
            smsDao.set(REDIS_KEY + phone + REDIS_KEY_FIX, count + 1, MINUTE_MAXIMUM_COUNT / 1000);
        }
    }

    @Override
    public Class<?> aPendingProblemWith() {
        return SpanMaxRestrictedMethodInterceptor.class;
    }
}
