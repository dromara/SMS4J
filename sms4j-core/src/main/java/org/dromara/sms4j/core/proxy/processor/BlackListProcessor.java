package org.dromara.sms4j.core.proxy.processor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.proxy.CoreMethodProcessor;
import org.dromara.sms4j.api.proxy.aware.SmsConfigAware;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.provider.config.SmsConfig;

import java.util.*;

/**
 * 黑名单前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class BlackListProcessor implements CoreMethodProcessor, SmsConfigAware {
    @Setter
    Object smsConfig;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void sendMessagePreProcess(String phone, String message) {
        doRestricted(Collections.singletonList(phone));
    }

    @Override
    public void sendMessageByTemplatePreProcess(String phone, String templateId, LinkedHashMap<String, String> messages) {
        doRestricted(Collections.singletonList(phone));
    }

    @Override
    public void massTextingPreProcess(List<String> phones, String message) {
        doRestricted(phones);
    }

    @Override
    public void massTextingByTemplatePreProcess(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        doRestricted(phones);
    }

    public void doRestricted(List<String> phones) {
        ArrayList<String> blackList = ((SmsConfig)smsConfig).getBlackList();
        for (String phone : phones) {
            if (blackList.stream().filter(black -> black.replace("-","").equals(phone)).findAny().isPresent()) {
                throw new SmsBlendException("The phone:", phone + " hit blacklist！");
            }
        }

    }

}
