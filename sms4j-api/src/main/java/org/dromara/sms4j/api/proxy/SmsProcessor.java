package org.dromara.sms4j.api.proxy;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * 执行器接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface SmsProcessor extends Order {
    default Object[] preProcessor(Method method, Object source, Object[] param) {
        return null;
    }

    default Object postProcessor(Object result, Object[] param) {
        return null;
    }

    default Object exceptionHandleProcessor(Method method, Object source, Object[] param) throws InvocationTargetException, IllegalAccessException {
        return null;
    }
}
