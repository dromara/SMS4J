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
        return invokeAfterCompletion(methodType,delegate,method, params, result, ex);
    }

    private Object[] invokePreHandle(SmsMethodType methodType, Object o, Method method, Object[] objects) {
        for (SmsMethodInterceptor interceptor : interceptors) {
            objects = interceptor.beforeInvoke(methodType, method, o, objects);
        }
        return objects;
    }

    private Object invokeAfterCompletion(SmsMethodType methodType, Object o, Method method, Object[] params, Object result, Exception ex) {
        // TODO 后续把错误SmsBlendException 分等级，如果影响运行的就直接程序停止，如果不影响程序运行是发送中的小问题就把错误吞掉，封装成SmsResponse，然后正常返回，这个拦截器要加到最前面，前置最先执行后置最后执行
        for (int index = interceptors.size() - 1; index >= 0; index--) {
            result = interceptors.get(index).afterCompletion(methodType, method, o,params, result, ex);
        }
        return result;
    }
}
