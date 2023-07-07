package org.dromara.sms4j.aliyun.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.aliyun.utils.AliyunUtils;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;

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
        return getSmsResponse(SmsUtil.arrayToString(phones), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
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
        try(HttpResponse response = HttpRequest.post(requestUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(paramStr)
                .execute()){
            JSONObject body = JSONUtil.parseObj(response.body());
            return this.getResponse(body);
        }
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess("OK".equals(resJson.getStr("Code")));
        smsResponse.setData(resJson);
        return smsResponse;
    }

}