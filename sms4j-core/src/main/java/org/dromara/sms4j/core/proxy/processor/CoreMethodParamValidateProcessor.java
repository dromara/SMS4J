package org.dromara.sms4j.core.proxy.processor;

import cn.hutool.core.util.StrUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.proxy.CoreMethodProcessor;
import org.dromara.sms4j.api.verify.PhoneVerify;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;


/**
 * 核心方法参数校验前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Setter
@Slf4j
public class CoreMethodParamValidateProcessor implements CoreMethodProcessor {

    /**
     * -- SETTER --
     *  设置 phoneVerify
     */
    private PhoneVerify phoneVerify;

    public CoreMethodParamValidateProcessor(PhoneVerify phoneVerify) {
        this.phoneVerify = phoneVerify;
    }

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
        if(Objects.isNull(messageObj)){
            throw new SmsBlendException("cant send a null message!");
        }
        if (messageObj instanceof String){
            String message = (String) messageObj;
            if (StrUtil.isBlank(message)) {
                throw new SmsBlendException("cant send a null message!");
            }
        }
    }

    public void validatePhone(String phone) {
        if (StrUtil.isBlank(phone)) {
            throw new SmsBlendException("cant send message to null!");
        }
        if (phoneVerify != null && !phoneVerify.verifyPhone(phone)){
            throw new SmsBlendException("The mobile phone number format is invalid!");
        }
    }

    public void validatePhones(List<String> phones) {
        if (null == phones) {
            throw new SmsBlendException("cant send message to null!");
        }
        for (String phone : phones) {
            if (StrUtil.isNotBlank(phone)) {
                if (phoneVerify != null && !phoneVerify.verifyPhone(phone)){
                    throw new SmsBlendException("The mobile phone number format is invalid!");
                }
            }
        }
    }

    public void validateMessages(String templateId, LinkedHashMap<String, String> messages) {
        if (StrUtil.isEmpty(templateId)) {
            throw new SmsBlendException("cant use template without template param");
        }
    }
}
