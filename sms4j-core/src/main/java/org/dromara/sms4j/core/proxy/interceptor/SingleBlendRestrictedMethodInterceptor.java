package org.dromara.sms4j.core.proxy.interceptor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.SmsMethodType;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.proxy.aware.SmsBlendConfigAware;
import org.dromara.sms4j.api.proxy.aware.SmsDaoAware;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;

import java.lang.reflect.Method;
import java.util.*;


/**
 * 短信发送渠道级上限前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class SingleBlendRestrictedMethodInterceptor implements SmsMethodInterceptor, SmsDaoAware, SmsBlendConfigAware {

    private static final String REDIS_KEY = "sms:restricted:";
    private static final String SEND_MESSAGE_METHOD = "sendMessage";
    private static final String MASS_TEXT_METHOD = "massText";

    /**
     * 缓存实例
     */
    @Setter
    private SmsDao smsDao;

    @Setter
    private Map<String, Map<String, Object>> smsBlendsConfig;

    @Override
    public int getOrder() {
        return SmsProxyFactory.SINGLE_BLEND_RESTRICTED_METHOD_INTERCEPTOR_ORDER;
    }

    @Override
    public Object[] beforeInvoke(SmsMethodType methodType, Method method, Object target, Object[] params) {
        if (Objects.isNull(methodType)
            || !Objects.equals(methodType.getName(), SEND_MESSAGE_METHOD)
            || !Objects.equals(methodType.getName(), MASS_TEXT_METHOD)) {
            return params;
        }
        SmsBlend smsBlend = (SmsBlend)target;
        String configId = smsBlend.getConfigId();
        Map<String, Object> targetConfig = smsBlendsConfig.get(configId);
        Object maximumObj = targetConfig.get("maximum");
        if (SmsUtils.isEmpty(maximumObj)) {
            return params;
        }
        int maximum = 0;
        try {
            maximum = (int)maximumObj;
        } catch (Exception e) {
            log.error("获取厂商级发送上限参数错误！请检查！");
            throw new IllegalArgumentException("获取厂商级发送上限参数错误");
        }
        Integer i = (Integer) smsDao.get(REDIS_KEY + configId + "maximum");
        if (SmsUtils.isEmpty(i)) {
            smsDao.set(REDIS_KEY + configId + "maximum", 1);
        } else if (i >= maximum) {
            log.info("The channel: {}, messages reached the maximum", configId);
            throw new SmsBlendException("The channel: " + configId + ", messages reached the maximum", configId);
        } else {
            smsDao.set(REDIS_KEY + configId + "maximum", i + 1);
        }
        return params;
    }
}
