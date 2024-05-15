package org.dromara.sms4j.core.proxy.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.proxy.AbstractGenericMethodInterceptor;
import org.dromara.sms4j.api.proxy.Restricted;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;
import org.dromara.sms4j.core.proxy.strategy.IListCheckMethodStrategy;

import java.util.*;

/**
 * 黑名单前置拦截器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class BlackListMethodInterceptor extends AbstractGenericMethodInterceptor<IListCheckMethodStrategy> implements Restricted {


    @Override
    public int getOrder() {
        return SmsProxyFactory.BLACK_LIST_METHOD_INTERCEPTOR_ORDER;
    }

    @Override
    protected void beforeSendMessage(String phone, String message) {
        getStrategy().checkListWithRecord(Collections.singletonList(phone));
    }

    @Override
    protected void beforeSendMessageWithTemplate(String phone, LinkedHashMap<String, String> messages) {
        getStrategy().checkListWithRecord(Collections.singletonList(phone));
    }

    @Override
    protected void beforeSendMessageWithCustomTemplate(String phone, String templateId, LinkedHashMap<String, String> messages) {
        getStrategy().checkListWithRecord(Collections.singletonList(phone));
    }

    @Override
    protected void beforeMassTexting(List<String> phones, String message) {
        getStrategy().checkListWithRecord(phones);
    }

    @Override
    protected void beforeMassTextingWithTemplate(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        getStrategy().checkListWithRecord(phones);
    }
}
