package org.dromara.sms4j.core.proxy.strategy.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.proxy.interceptor.SyncMethodParamValidateMethodInterceptor;
import org.dromara.sms4j.core.proxy.strategy.ISyncMethodParamValidateStrategy;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * 同步调用方法参数校验策略
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class DefaultSyncMethodParamValidateStrategy implements ISyncMethodParamValidateStrategy {

    public void validateMessage(Object messageObj) {
        /*if (null == messageObj)
            throw new SmsBlendException("can't send a null message!");*/
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
        if (CharSequenceUtil.isEmpty(templateId)){
            throw new SmsBlendException("can't use a null template");
        }
        validateMessage(messages);
    }

    public Class<?> aPendingProblemWith() {
        return SyncMethodParamValidateMethodInterceptor.class;
    }
}
