package org.dromara.sms4j.core.proxy.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.proxy.AbstractGenericMethodInterceptor;
import org.dromara.sms4j.api.proxy.Restricted;
import org.dromara.sms4j.api.proxy.SmsMethodType;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;
import org.dromara.sms4j.core.proxy.strategy.IChannelRestrictedStrategy;

import java.lang.reflect.Method;


/**
 * 短信发送渠道级上限拦截器
 *
 * @author sh1yu
 * @since 2023/12/22 13:03
 */
@Slf4j
public class ChannelMaxRestrictedMethodInterceptor extends AbstractGenericMethodInterceptor<IChannelRestrictedStrategy> implements Restricted  {

    @Override
    public int getOrder() {
        return SmsProxyFactory.SINGLE_BLEND_RESTRICTED_METHOD_INTERCEPTOR_ORDER;
    }

    @Override
    public Object[] beforeInvoke(SmsMethodType methodType, Method method, Object target, Object[] params) {
        getStrategy().restricted(methodType, (SmsBlend) target);
        return params;
    }

    @Override
    public Object afterCompletion(SmsMethodType methodType, Method method,Object target, Object[] params, Object result, Exception ex) {
        getStrategy().flushRecord(methodType, (SmsBlend) target);
        return result;
    }


}
