package org.dromara.sms4j.core.proxy;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.proxy.SmsProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;


/**
 * SmsBlend增强，封装smsblend和执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class SmsInvocationHandler implements InvocationHandler {
    private final SmsBlend smsBlend;


    public SmsInvocationHandler(SmsBlend smsBlend) {
        this.smsBlend = smsBlend;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) {
        Object result = null;
        //前置执行器
        objects = doPreProcess(smsBlend, method, objects);
        try {
            result = method.invoke(smsBlend, objects);
        } catch (Exception e) {
            // method.invoke 会包装真实业务异常，异常处理器需要看到原始异常类型和消息。
            doErrorHandleProcess(smsBlend, method, objects, unwrapException(e));
        }
        //后置执行器
        return doPostrocess(smsBlend, method, objects, result);
    }

    /**
     * 前置执行器
     * */
    public Object[] doPreProcess(Object o, Method method, Object[] objects) {
        for (SmsProcessor processor : SmsProxyFactory.getProcessors()) {
            objects = processor.preProcessor(method, o, objects);
        }
        return objects;
    }

    /**
     * 异常执行器
     * */
    public void doErrorHandleProcess(Object o, Method method, Object[] objects,Exception e) throws RuntimeException{
        for (SmsProcessor processor : SmsProxyFactory.getProcessors()) {
            processor.exceptionHandleProcessor(method, o, objects,e);
        }
    }

    private Exception unwrapException(Exception e) {
        if (e instanceof InvocationTargetException) {
            Throwable target = ((InvocationTargetException) e).getTargetException();
            if (target instanceof Exception) {
                return (Exception) target;
            }
            return new RuntimeException(target);
        }
        return e;
    }

    /**
     * 后置执行器
     * */
    public Object doPostrocess(Object o, Method method, Object[] objects, Object result) {
        for (SmsProcessor processor : SmsProxyFactory.getProcessors()) {
            if (Objects.nonNull(result) && result instanceof SmsResponse){
                Object overrideResult = processor.postProcessor((SmsResponse)result, objects);
                if (overrideResult != null) {
                    return overrideResult;
                }
            }
        }
        return result;
    }
}
