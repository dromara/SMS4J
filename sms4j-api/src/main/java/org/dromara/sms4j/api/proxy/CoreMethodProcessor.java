package org.dromara.sms4j.api.proxy;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
/**
 * 核心方法执行器接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface CoreMethodProcessor extends SmsProcessor {
    default Object[] preProcessor(Method method, Object source, Object[] param) {
        String name = method.getName();
        int parameterCount = method.getParameterCount();
        if ("sendMessage".equals(method.getName())) {
            if (2 == parameterCount) {
                sendMessagePreProcess((String) param[0],(String) param[1]);
                return param;
            }
            if (3 == parameterCount) {
                sendMessageByTemplatePreProcess((String)param[0],(String) param[1],(LinkedHashMap<String, String>)param[2]);
                return param;
            }
        }
        if ("massTexting".equals(method.getName())) {
            if (2 == parameterCount) {
                massTextingPreProcess((List<String>)param[0],(String)param[1]);
                return param;
            }
            if (3 == parameterCount) {
                massTextingByTemplatePreProcess((List<String>)param[0],(String)param[1],(LinkedHashMap<String, String>)param[2]);
                return param;
            }
        }
        return param;
    }
    void sendMessagePreProcess(String phone, String message);
    void sendMessageByTemplatePreProcess(String phone, String templateId, LinkedHashMap<String, String> messages);
    void massTextingPreProcess(List<String> phones, String message);
    void massTextingByTemplatePreProcess(List<String> phones, String templateId, LinkedHashMap<String, String> messages);
}
