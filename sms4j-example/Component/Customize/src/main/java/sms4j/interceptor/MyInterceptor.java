package sms4j.interceptor;

import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.proxy.SmsMethodType;

import java.lang.reflect.Method;
import java.util.Objects;

public class MyInterceptor implements SmsMethodInterceptor<IMyIntercepterStrategy> {

    public Object afterCompletion(
            SmsMethodType methodType, Method method, Object target, Object[] params, Object result, Exception ex) {
        if (Objects.nonNull(methodType)) {
            switch (methodType) {
                case MASS_TEXTING:
                case SEND_MESSAGE:
                    System.out.println("自定义拦截器被触发，触发顺序为"+getOrder());
                    getStrategy().doSomeThing();
                    break;
            }
        }

        return result;
    }

    public int getOrder() {
        return Integer.MAX_VALUE - 1;
    }

}
