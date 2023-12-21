package org.dromara.sms4j.core.proxy;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.proxy.SmsMethodType;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * {@link SmsBlend}代理，用于织入{@link SmsMethodInterceptor}实现前置和后置拦截
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
@RequiredArgsConstructor
public class SmsInvocationHandler implements InvocationHandler {

    private final SmsBlend delegate;
    private final List<SmsMethodInterceptor> interceptors;

    @Override
    public Object invoke(Object target, Method method, Object[] params) {
        SmsMethodType methodType = SmsMethodType.of(method);
        Object result = null;
        // 前置拦截
        params = invokePreHandle(methodType, delegate, method, params);
        Exception ex = null;
        try {
            result = method.invoke(delegate, params);
        } catch (Exception e) {
            ex = e;
        }
        // 后置拦截
        return invokeAfterCompletion(methodType, method, params, result, ex);
    }

    private Object[] invokePreHandle(SmsMethodType methodType, Object o, Method method, Object[] objects) {
        for (SmsMethodInterceptor interceptor : interceptors) {
            objects = interceptor.beforeInvoke(methodType, method, o, objects);
        }
        return objects;
    }

    private Object invokeAfterCompletion(SmsMethodType methodType, Method method, Object[] params, Object result, Exception ex) {
        for (SmsMethodInterceptor interceptor : interceptors) {
            result = interceptor.afterCompletion(methodType, method, params, result, ex);
        }
        return result;
    }
}
