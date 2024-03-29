package org.dromara.sms4j.core.proxy.processor;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.proxy.CoreMethodProcessor;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.*;


/**
 * 核心方法参数校验前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class CoreMethodParamValidateProcessor implements CoreMethodProcessor {
    @Override
    public int getOrder() {
        return -1;
    }

    @Override
    public void sendMessagePreProcess(String phone, Object message) {
        validatePhone(phone);
        validateMessage(message);
    }

    @Override
    public void sendMessageByTemplatePreProcess(String phone, String templateId, LinkedHashMap<String, String> messages) {
        validatePhone(phone);
        validateMessages(templateId, messages);
    }

    @Override
    public void massTextingPreProcess(List<String> phones, String message) {
        validateMessage(message);
        validatePhones(phones);
    }

    @Override
    public void massTextingByTemplatePreProcess(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        validatePhones(phones);
        validateMessages(templateId, messages);
    }

    public void validateMessage(Object messageObj) {
        if (messageObj instanceof String){
            String message = (String) messageObj;
            if (null == message || "".equals(message)) {
                throw new SmsBlendException("cant send a null message!");
            }
        }
        if (messageObj instanceof Map){
            Map message = (Map) messageObj;
            if (message.size()<1) {
                throw new SmsBlendException("cant send a null message!");
            }
        }
    }

    public void validatePhone(String phone) {
        if (null == phone || "".equals(phone)) {
            throw new SmsBlendException("cant send message to null!");
        }
    }

    public void validatePhones(List<String> phones) {
        if (null == phones) {
            throw new SmsBlendException("cant send message to null!");
        }
        for (String phone : phones) {
            if (null != phone && !"".equals(phone)) {
                return;
            }
        }
        throw new SmsBlendException("cant send message to null!");
    }

    public void validateMessages(String templateId, LinkedHashMap<String, String> messages) {
        if (null != templateId && !"".equals(templateId) && (messages == null || messages.size() < 1)) {
            throw new SmsBlendException("cant use template without template param");
        }
    }
}
