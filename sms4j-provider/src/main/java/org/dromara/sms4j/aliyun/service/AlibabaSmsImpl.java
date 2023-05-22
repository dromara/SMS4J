package org.dromara.sms4j.aliyun.service;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.aliyun.utils.AliyunUtils;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>类名: AlibabaSmsImpl
 * <p>说明：  阿里云短信实现
 *
 * @author :Wind
 * 2023/3/26  17:16
 **/

@Slf4j
public class AlibabaSmsImpl extends AbstractSmsBlend {

    private final AlibabaConfig alibabaSmsConfig;

    /**
     * AlibabaSmsImpl
     * <p>构造器，用于构造短信实现模块
     *
     * @author :Wind
     */
    public AlibabaSmsImpl(AlibabaConfig alibabaSmsConfig, Executor pool, DelayedTime delayedTime) {
        super(pool, delayedTime);
        this.alibabaSmsConfig = alibabaSmsConfig;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(alibabaSmsConfig.getTemplateName(), message);
        return sendMessage(phone, alibabaSmsConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSONUtil.toJsonStr(messages);
        return getSmsResponse(phone, messageStr, templateId);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(alibabaSmsConfig.getTemplateName(), message);
        return massTexting(phones, alibabaSmsConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSONUtil.toJsonStr(messages);
        return getSmsResponse(arrayToString(phones), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
        AtomicReference<SmsResponse> reference = new AtomicReference<>();
        String requestUrl;
        String paramStr;
        try {
            requestUrl = AliyunUtils.generateSendSmsRequestUrl(this.alibabaSmsConfig, message, phone, templateId);
            paramStr = AliyunUtils.generateParamBody(alibabaSmsConfig, phone, message, templateId);
        } catch (Exception e) {
            log.error("aliyun send message error", e);
            throw new SmsBlendException(e.getMessage());
        }
        log.debug("requestUrl {}", requestUrl);
        super.http.post(requestUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addBody(paramStr)
                .onSuccess(((data, req, res) -> {
                    Map map = res.get(Map.class);
                    reference.set(getResponse(map));
                }))
                .onError((ex, req, res) -> {
                    Map map = res.get(Map.class);
                    reference.set(getResponse(map));
                })
                .execute();
        return reference.get();
    }

    private static SmsResponse getResponse(Map map) {
        SmsResponse smsResponse = new SmsResponse();
        if (map == null) {
            smsResponse.setErrorCode("500");
            smsResponse.setErrMessage("aliyun send sms response is null.check param");
            return smsResponse;
        }
        smsResponse.setCode((String) map.get("Code"));
        smsResponse.setMessage((String) map.get("Message"));
        if ("OK".equals(smsResponse.getCode())) {
            smsResponse.setBizId((String) map.get("BizId"));
        }
        return smsResponse;
    }


    private String arrayToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(",").append("+86").append(s);
        }
        return sb.substring(1);
    }
}
