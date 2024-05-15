package org.dromara.sms4j.core.proxy.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.proxy.AbstractGenericMethodInterceptor;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;
import org.dromara.sms4j.core.proxy.strategy.ISyncMethodParamValidateStrategy;

import java.util.*;



/**
 * 同步调用方法参数校验前置拦截器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class SyncMethodParamValidateMethodInterceptor extends AbstractGenericMethodInterceptor<ISyncMethodParamValidateStrategy> {

    @Override
    public int getOrder() {
        return SmsProxyFactory.CORE_PARAM_VALIDATE_METHOD_INTERCEPTOR_ORDER;
    }

    @Override
    public void beforeSendMessage(String phone, String message) {
        ISyncMethodParamValidateStrategy strategy =  getStrategy();
        strategy.validatePhone(phone);
        strategy.validateMessage(message);
    }

    @Override
    protected void beforeSendMessageWithTemplate(String phone, LinkedHashMap<String, String> messages) {
        ISyncMethodParamValidateStrategy strategy = getStrategy();
        strategy.validatePhone(phone);
        strategy.validateMessage(messages);
    }

    @Override
    public void beforeSendMessageWithCustomTemplate(String phone, String templateId, LinkedHashMap<String, String> messages) {
        ISyncMethodParamValidateStrategy strategy = getStrategy();
        strategy.validatePhone(phone);
        strategy.validateMessages(templateId, messages);
    }

    @Override
    public void beforeMassTexting(List<String> phones, String message) {
        ISyncMethodParamValidateStrategy strategy = getStrategy();
        strategy.validateMessage(message);
        strategy.validatePhones(phones);
    }

    @Override
    public void beforeMassTextingWithTemplate(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        ISyncMethodParamValidateStrategy strategy = getStrategy();
        strategy.validatePhones(phones);
        strategy.validateMessages(templateId, messages);
    }
}
