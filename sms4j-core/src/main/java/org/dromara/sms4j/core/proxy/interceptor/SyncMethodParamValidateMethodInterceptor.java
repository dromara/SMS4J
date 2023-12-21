package org.dromara.sms4j.core.proxy.interceptor;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.proxy.AbstractGenericMethodInterceptor;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;

import java.util.*;


/**
 * 同步调用方法参数校验前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 * TODO 异步调用和延迟调用的参数是否需要校验？
 */
@Slf4j
public class SyncMethodParamValidateMethodInterceptor extends AbstractGenericMethodInterceptor {

    @Override
    public int getOrder() {
        return SmsProxyFactory.CORE_PARAM_VALIDATE_METHOD_INTERCEPTOR_ORDER;
    }

    @Override
    public void beforeSendMessage(String phone, String message) {
        validatePhone(phone);
        validateMessage(message);
    }

    @Override
    protected void beforeSendMessageWithTemplate(String phone, LinkedHashMap<String, String> messages) {
        validatePhone(phone);
        validateMessage(messages);
    }

    @Override
    public void beforeSendMessageWithCustomTemplate(String phone, String templateId, LinkedHashMap<String, String> messages) {
        validatePhone(phone);
        validateMessages(templateId, messages);
    }

    @Override
    public void beforeMassTexting(List<String> phones, String message) {
        validateMessage(message);
        validatePhones(phones);
    }

    @Override
    public void beforeMassTextingWithTemplate(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        validatePhones(phones);
        validateMessages(templateId, messages);
    }

    public void validateMessage(Object messageObj) {
        if (messageObj instanceof String){
            String message = (String) messageObj;
            if (CharSequenceUtil.isEmpty(message)) {
                throw new SmsBlendException("can't send a null message!");
            }
        }
        if (messageObj instanceof Map){
            Map<?, ?> message = (Map<?, ?>) messageObj;
            if (CollUtil.isEmpty(message)) {
                throw new SmsBlendException("can't send a null message!");
            }
        }
    }

    public void validatePhone(String phone) {
        if (CharSequenceUtil.isEmpty(phone)) {
            throw new SmsBlendException("can't send message to null!");
        }
    }

    public void validatePhones(List<String> phones) {
        if (CollUtil.isEmpty(phones)) {
            throw new SmsBlendException("can't send message to null!");
        }
        phones.forEach(this::validatePhone);
    }

    public void validateMessages(String templateId, LinkedHashMap<String, String> messages) {
        if (CharSequenceUtil.isNotEmpty(templateId) && CollUtil.isEmpty(messages)) {
            throw new SmsBlendException("can't use template without template param");
        }
    }
}
