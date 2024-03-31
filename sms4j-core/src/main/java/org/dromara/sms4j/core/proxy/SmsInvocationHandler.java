package org.dromara.sms4j.core.proxy;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.proxy.SmsProcessor;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.LinkedList;


/**
 * SmsBlend增强，封装smsblend和执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class SmsInvocationHandler implements InvocationHandler {
    private final SmsBlend smsBlend;
    private final LinkedList<SmsProcessor> processors;


    public SmsInvocationHandler(SmsBlend smsBlend, LinkedList<SmsProcessor> processors) {
        this.smsBlend = smsBlend;
        this.processors = processors;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) {
        Object result = null;
        //前置执行器
        objects = doPreProcess(smsBlend, method, objects);
        try {
            result = method.invoke(smsBlend, objects);
        } catch (Exception e) {
            //错误执行器
            doErrorHandleProcess(smsBlend, method, objects,e);
            throw new SmsBlendException(e);
        }
        //后置执行器
        doPostrocess(smsBlend, method, objects, result);
        return result;
    }

    /**
     * 前置执行器
     * */
    public Object[] doPreProcess(Object o, Method method, Object[] objects) {
        for (SmsProcessor processor : processors) {
            objects = processor.preProcessor(method, o, objects);
        }
        return objects;
    }

    /**
     * 异常执行器
     * */
    public void doErrorHandleProcess(Object o, Method method, Object[] objects,Exception e) {
        for (SmsProcessor processor : processors) {
            processor.exceptionHandleProcessor(method, o, objects,e);
        }
    }

    /**
     * 后置执行器
     * */
    public Object doPostrocess(Object o, Method method, Object[] objects, Object result) {
        for (SmsProcessor processor : processors) {
            Object overrideResult = processor.postProcessor(result, objects);
            if (overrideResult != null) {
                return overrideResult;
            }
        }
        return result;
    }
}
