package org.dromara.sms4j.api.proxy;

import org.dromara.sms4j.comm.constant.NumberOfParasmeters;

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
    @Override
    default Object[] preProcessor(Method method, Object source, Object[] param) {
        String name = method.getName();
        int parameterCount = method.getParameterCount();
        if ("sendMessage".equals(name)) {
            if (NumberOfParasmeters.TWO == NumberOfParasmeters.getNumberOfParasmetersEnum(parameterCount)) {
                sendMessagePreProcess((String) param[0], param[1]);
                return param;
            }
            if (NumberOfParasmeters.THREE == NumberOfParasmeters.getNumberOfParasmetersEnum(parameterCount)) {
                if (null == param[2]){
                    param[2] = new LinkedHashMap<>();
                }
                sendMessageByTemplatePreProcess((String)param[0],(String) param[1],(LinkedHashMap<String, String>)param[2]);
                return param;
            }
        }
        if ("massTexting".equals(name)) {
            if (NumberOfParasmeters.TWO == NumberOfParasmeters.getNumberOfParasmetersEnum(parameterCount)) {
                massTextingPreProcess((List<String>)param[0],(String)param[1]);
                return param;
            }
            if (NumberOfParasmeters.THREE == NumberOfParasmeters.getNumberOfParasmetersEnum(parameterCount)) {
                if (null == param[2]){
                    param[2] = new LinkedHashMap<>();
                }
                massTextingByTemplatePreProcess((List<String>)param[0],(String)param[1],(LinkedHashMap<String, String>)param[2]);
                return param;
            }
        }
        return param;
    }
    void sendMessagePreProcess(String phone, Object message);
    void sendMessageByTemplatePreProcess(String phone, String templateId, LinkedHashMap<String, String> messages);
    void massTextingPreProcess(List<String> phones, String message);
    void massTextingByTemplatePreProcess(List<String> phones, String templateId, LinkedHashMap<String, String> messages);
}
