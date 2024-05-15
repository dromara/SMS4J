package org.dromara.sms4j.api.proxy;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.manage.InterceptorStrategySmsManager;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;

import java.lang.reflect.Method;

/**
 * <p>方法拦截器，用于拦截{@link SmsBlend}中的方法。<br />
 * 推荐基于{@link AbstractGenericMethodInterceptor}实现自定义拦截器。
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 * @see AbstractGenericMethodInterceptor
 * @see SmsMethodType
 */
public interface SmsMethodInterceptor<C extends IInterceptorStrategy> extends Order {

    /**
     * 前置拦截，在方法执行前调用
     *
     * @param methodType 方法类型，若不是{@link SmsMethodType}则可能为{@code null}
     * @param method 方法
     * @param target 调用对象
     * @param params 调用参数
     * @return 调用参数
     */
    default Object[] beforeInvoke(
        SmsMethodType methodType, Method method, Object target, Object[] params) {
        return params;
    }

    /**
     * 后置拦截，在方法执行后，无论是否发生异常都会调用
     *
     * @param methodType 方法类型，若不是{@link SmsMethodType}则可能为{@code null}
     * @param method 调用方法
     * @param params 调用参数
     * @param result 返回值
     * @param ex 调用过程捕获的异常，可能为{@code null}
     * @return 返回值，不为{@code null}时将覆盖原有的返回值
     */
    default Object afterCompletion(
        SmsMethodType methodType, Method method, Object target,Object[] params, Object result, Exception ex) {
        return result;
    }

    default C getStrategy(){
        return (C) InterceptorStrategySmsManager.getStrategyByProblemClass(this.getClass());
    }

}
