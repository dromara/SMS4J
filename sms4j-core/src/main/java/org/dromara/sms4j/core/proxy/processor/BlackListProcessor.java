package org.dromara.sms4j.core.proxy.processor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.CoreMethodProcessor;
import org.dromara.sms4j.api.proxy.aware.SmsDaoAware;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 黑名单前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Setter
@Slf4j
public class BlackListProcessor implements CoreMethodProcessor, SmsDaoAware {
    SmsDao smsDao;

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void sendMessagePreProcess(String phone, Object message) {
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
        ArrayList<String> blackList = (ArrayList<String>) smsDao.get("sms:blacklist:global");
        if(null==blackList){
            return;
        }
        for (String phone : phones) {
            if (blackList.stream().anyMatch(black -> black.replace("-","").equals(phone))) {
                throw new SmsBlendException("The phone:", phone + " hit global blacklist！");
            }
        }
    }
}
