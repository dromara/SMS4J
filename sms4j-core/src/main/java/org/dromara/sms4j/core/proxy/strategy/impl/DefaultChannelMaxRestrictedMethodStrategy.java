package org.dromara.sms4j.core.proxy.strategy.impl;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.SmsMethodType;
import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsBlendConfigAware;
import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsDaoAware;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.proxy.interceptor.ChannelMaxRestrictedMethodInterceptor;
import org.dromara.sms4j.core.proxy.strategy.IChannelRestrictedStrategy;

import java.util.Map;
import java.util.Objects;

/**
 * 短信发送时间级上限拦截策略
 *
 * @author sh1yu
 * @since 2023/12/22 13:03
 */
@Slf4j
public class DefaultChannelMaxRestrictedMethodStrategy implements IChannelRestrictedStrategy, InterceptorStrategySmsDaoAware, InterceptorStrategySmsBlendConfigAware {


    private static final String REDIS_KEY = "sms:restricted:";
    private static final String REDIS_KEY_FIX = "maximum";
    private static final String SEND_MESSAGE_METHOD = "sendMessage";
    private static final String MASS_TEXT_METHOD = "massText";

    /**
     * 缓存实例
     */
    @Setter
    private SmsDao smsDao;

    @Setter
    private Map<String, Map<String, Object>> smsBlendsConfig;

    public void restricted(SmsMethodType methodType, SmsBlend smsBlend) {
        if (shouldSkip(methodType)) {
            return;
        }
        String configId = smsBlend.getConfigId();
        Map<String, Object> targetConfig = smsBlendsConfig.get(configId);
        Object maximumObj = targetConfig.get(REDIS_KEY_FIX);
        if (SmsUtils.isEmpty(maximumObj)) {
            return;
        }
        int maximum;
        if (maximumObj instanceof String){
            maximum = Integer.parseInt((String) maximumObj);
        }else{
            try {
                maximum = (int) maximumObj;
            } catch (Exception e) {
                log.error("获取厂商级发送上限参数错误！请检查！");
                throw new IllegalArgumentException("获取厂商级发送上限参数错误");
            }
        }
        Integer i = (Integer) smsDao.get(REDIS_KEY + configId + REDIS_KEY_FIX);
        if (SmsUtils.isNotEmpty(i) && i >= maximum) {
            log.info("The channel: {}, messages reached the maximum", configId);
            throw new SmsBlendException("The channel: " + configId + ", messages reached the maximum", configId);
        }
    }


    public void flushRecord(SmsMethodType methodType, SmsBlend smsBlend) {
        if (shouldSkip(methodType)) {
            return;
        }
        String configId = smsBlend.getConfigId();
        Map<String, Object> targetConfig = smsBlendsConfig.get(configId);
        Object maximumObj = targetConfig.get(REDIS_KEY_FIX);
        if (SmsUtils.isEmpty(maximumObj)) {
            return;
        }
        Integer i = (Integer) smsDao.get(REDIS_KEY + configId + REDIS_KEY_FIX);
        if (SmsUtils.isEmpty(i)) {
            smsDao.set(REDIS_KEY + configId + REDIS_KEY_FIX, 1);
        } else {
            smsDao.set(REDIS_KEY + configId + REDIS_KEY_FIX, i + 1);
        }
    }


    public boolean shouldSkip(SmsMethodType methodType) {
        return Objects.isNull(methodType)
                || !(Objects.equals(methodType.getName(), SEND_MESSAGE_METHOD)
                || Objects.equals(methodType.getName(), MASS_TEXT_METHOD));
    }

    @Override
    public Class<?> aPendingProblemWith() {
        return ChannelMaxRestrictedMethodInterceptor.class;
    }
}