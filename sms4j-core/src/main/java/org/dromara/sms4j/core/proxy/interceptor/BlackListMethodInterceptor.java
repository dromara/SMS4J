package org.dromara.sms4j.core.proxy.interceptor;

import cn.hutool.core.collection.CollUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.AbstractGenericMethodInterceptor;
import org.dromara.sms4j.api.proxy.aware.SmsDaoAware;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;

import java.util.*;

/**
 * 黑名单前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class BlackListMethodInterceptor extends AbstractGenericMethodInterceptor implements SmsDaoAware {

    private static final String CONFIG_PROPERTIES_PREFIX = "sms:blacklist:global";

    @Setter
    private SmsDao smsDao;

    @Override
    public int getOrder() {
        return SmsProxyFactory.BLACK_LIST_METHOD_INTERCEPTOR_ORDER;
    }

    @Override
    protected void beforeSendMessage(String phone, String message) {
        doRestricted(Collections.singletonList(phone));
    }

    @Override
    protected void beforeSendMessageWithTemplate(String phone, LinkedHashMap<String, String> messages) {
        doRestricted(Collections.singletonList(phone));
    }

    @Override
    protected void beforeSendMessageWithCustomTemplate(String phone, String templateId, LinkedHashMap<String, String> messages) {
        doRestricted(Collections.singletonList(phone));
    }

    @Override
    protected void beforeMassTexting(List<String> phones, String message) {
        doRestricted(phones);
    }

    @Override
    protected void beforeMassTextingWithTemplate(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        doRestricted(phones);
    }

    @SuppressWarnings("unchecked")
    private void doRestricted(List<String> phones) {
        List<String> blackList = (List<String>) smsDao.get(CONFIG_PROPERTIES_PREFIX);
        if (CollUtil.isEmpty(blackList)) {
            return;
        }
        for (String phone : phones) {
            if (blackList.stream().anyMatch(black -> black.replace("-","").equals(phone))) {
                throw new SmsBlendException("The phone:", phone + " hit global blacklist！");
            }
        }
    }
}
