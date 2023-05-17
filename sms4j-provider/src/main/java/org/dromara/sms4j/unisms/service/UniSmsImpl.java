package org.dromara.sms4j.unisms.service;

import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.unisms.config.UniConfig;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.unisms.core.Uni;
import org.dromara.sms4j.unisms.core.UniResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;


/**
 * <p>类名: UniSmsImpl
 * <p>说明：  uniSms短信实现
 * @author :Wind
 * 2023/3/26  17:10
 **/

@Slf4j
public class UniSmsImpl extends AbstractSmsBlend {

    private UniConfig config;

    public UniSmsImpl(UniConfig config, Executor pool, DelayedTime delayed) {
        super(pool,delayed);
        this.config = config;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        if ("".equals(config.getTemplateId()) && "".equals(config.getTemplateName())){
            throw new SmsBlendException("配置文件模板id和模板变量不能为空！");
        }
        LinkedHashMap<String, String>map = new LinkedHashMap<>();
        map.put(config.getTemplateName(),message);
        return sendMessage(phone, config.getTemplateId(),map);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        Map<String, Object> data = new HashMap<>();
        data.put("to", Collections.singletonList(phone));
        data.put("signature", config.getSignature());
        data.put("templateId", templateId);
        data.put("templateData", messages);
        return getSmsResponse(data);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        if ("".equals(config.getTemplateId()) && "".equals(config.getTemplateName())){
            throw new SmsBlendException("配置文件模板id和模板变量不能为空！");
        }
        LinkedHashMap<String, String>map = new LinkedHashMap<>();
        map.put(config.getTemplateName(),message);
        return massTexting(phones, config.getTemplateId(),map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (phones.size()>1000){
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("to", phones);
        data.put("signature", config.getSignature());
        data.put("templateId", templateId);
        data.put("templateData", messages);
        return getSmsResponse(data);
    }

    private SmsResponse getSmsResponse( Map<String, Object> data) {
        SmsResponse smsResponse = new SmsResponse();
        try {
            UniResponse send = Uni.getClient().request("sms.message.send", data);
            smsResponse.setCode(String.valueOf(send.status));
            smsResponse.setErrorCode(send.code);
            smsResponse.setMessage(send.message);
            smsResponse.setBizId(send.requestId);
            smsResponse.setData(send);
        }catch(Exception e){
            smsResponse.setErrMessage(e.getMessage());
        }

        return smsResponse;
    }
}
