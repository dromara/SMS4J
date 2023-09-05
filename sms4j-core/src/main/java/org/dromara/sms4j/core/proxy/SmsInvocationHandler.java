package org.dromara.sms4j.core.proxy;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.proxy.RestrictedProcess;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class SmsInvocationHandler implements InvocationHandler {
    private final SmsBlend smsBlend;
    private static RestrictedProcess restrictedProcess = new RestrictedProcessDefaultImpl();

    private SmsInvocationHandler(SmsBlend smsBlend) {
        this.smsBlend = smsBlend;
    }

    public static SmsInvocationHandler newSmsInvocationHandler(SmsBlend smsBlend) {
        return new SmsInvocationHandler(smsBlend);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Object result;
        if ("sendMessage".equals(method.getName()) || "massTexting".equals(method.getName())) {
            //取手机号作为参数
            String phone = (String) objects[0];
            SmsBlendException smsBlendException = restrictedProcess.process(phone);
            if (!Objects.isNull(smsBlendException)) {
                throw smsBlendException;
            }
        }
        result = method.invoke(smsBlend, objects);
        return result;
    }

    /**
     * 设置 restrictedProcess
     */
    public static void setRestrictedProcess(RestrictedProcess restrictedProcess) {
        SmsInvocationHandler.restrictedProcess = restrictedProcess;
    }
}
