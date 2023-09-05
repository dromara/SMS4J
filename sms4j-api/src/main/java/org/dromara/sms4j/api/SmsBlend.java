package org.dromara.sms4j.api;

import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * SmsBlend
 * <p> 通用接口，定义国内短信方法
 * @author :Wind
 * 2023/5/16  16:03
 **/
public interface SmsBlend {

    String getConfigId();

    String getSupplier();

    SmsResponse sendMessage(String phone, String message);

    SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages);

    SmsResponse massTexting(List<String> phones, String message);

    SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages);

    void sendMessageAsync(String phone, String message, CallBack callBack);

    void sendMessageAsync(String phone, String message);

    void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack);

    void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages);

    void delayedMessage(String phone, String message, Long delayedTime);

    void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime);

    void delayMassTexting(List<String> phones, String message, Long delayedTime);

    void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime);

}
