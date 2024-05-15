package org.dromara.sms4j.core.proxy.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.proxy.AbstractGenericMethodInterceptor;
import org.dromara.sms4j.api.proxy.Restricted;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;
import org.dromara.sms4j.core.proxy.strategy.IRestrictedMethodStrategy;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * 短信发送账号级上限拦截器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class AcctMaxRestrictedMethodInterceptor extends AbstractGenericMethodInterceptor<IRestrictedMethodStrategy> implements Restricted {

    @Override
    public int getOrder() {
        return SmsProxyFactory.ACCT_RESTRICTED_METHOD_INTERCEPTOR;
    }

    @Override
    public void beforeSendMessage(String phone, String message) {
        getStrategy().restricted(Collections.singletonList(phone));
    }

    @Override
    protected void afterSendMessage(String phone, String message, Object result, Exception ex) {
        getStrategy().flushRecord(Collections.singletonList(phone));
    }

    @Override
    protected void beforeSendMessageWithTemplate(String phone, LinkedHashMap<String, String> messages) {
        getStrategy().restricted(Collections.singletonList(phone));
    }

    @Override
    protected void afterSendMessageWithTemplate(String phone, LinkedHashMap<String, String> messages, Object result, Exception ex) {
        getStrategy().flushRecord(Collections.singletonList(phone));
    }

    @Override
    public void beforeSendMessageWithCustomTemplate(String phone, String templateId, LinkedHashMap<String, String> messages) {
        getStrategy().restricted(Collections.singletonList(phone));
    }

    @Override
    protected void afterSendMessageWithCustomTemplate(String phone, String templateId, LinkedHashMap<String, String> messages, Object result, Exception ex) {
        getStrategy().flushRecord(Collections.singletonList(phone));
    }

    @Override
    public void beforeMassTexting(List<String> phones, String message) {
        getStrategy().restricted(phones);
    }

    @Override
    protected void afterMassTexting(List<String> phones, String message, Object result, Exception ex) {
        getStrategy().flushRecord(phones);
    }

    @Override
    public void beforeMassTextingWithTemplate(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        getStrategy().restricted(phones);
    }

    @Override
    protected void afterMassTextingWithTemplate(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Object result, Exception ex) {
        getStrategy().flushRecord(phones);
    }
}
