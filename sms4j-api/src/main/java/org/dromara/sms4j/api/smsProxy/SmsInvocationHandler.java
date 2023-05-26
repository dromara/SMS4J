package org.dromara.sms4j.api.smsProxy;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.comm.config.SmsConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

@Slf4j
public class SmsInvocationHandler implements InvocationHandler {
    private SmsBlend smsBlend;
    private SmsConfig config;
    private static RestrictedProcess restrictedProcess = new RestrictedProcess();

    private SmsInvocationHandler(SmsBlend smsBlend, SmsConfig config) {
        this.smsBlend = smsBlend;
        this.config = config;
    }

    public static SmsInvocationHandler newSmsInvocationHandler(SmsBlend smsBlend, SmsConfig config){
        return new SmsInvocationHandler(smsBlend,config);
    }

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Object result = null;
        if ("sendMessage".equals(method.getName()) || "massTexting".equals(method.getName())) {
            //取手机号作为参数
            String phone = (String) objects[0];
            SmsBlendException smsBlendException = restrictedProcess.process(config,phone);
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
