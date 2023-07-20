package org.dromara.sms4j.emay.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.emay.util.EmayBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author Richard
 * @date 2023-04-11 12:00
 */
@Slf4j
public class EmaySmsImpl extends AbstractSmsBlend {
    public EmaySmsImpl(EmayConfig config, Executor pool, DelayedTime delayed) {
        super(pool, delayed);
        this.config = config;
    }

    private final EmayConfig config;

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        String url = config.getRequestUrl();
        Map<String, Object> params;
        try {
            params = EmayBuilder.buildRequestBody(config.getAppId(), config.getSecretKey(), phone, message);
        } catch (SmsBlendException e) {
            SmsResponse smsResponse = new SmsResponse();
            smsResponse.setSuccess(false);
            return smsResponse;
        }
        return getSendResponse(params, url);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        return sendMessage(phone, EmayBuilder.listToString(list));
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        if (phones.size() > 500) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于500");
        }
        return sendMessage(SmsUtil.listToString(phones), message);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (phones.size() > 500) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于500");
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        return sendMessage(SmsUtil.listToString(phones), EmayBuilder.listToString(list));
    }

    private SmsResponse getSendResponse(Map<String, Object> body, String requestUrl) {
        try(HttpResponse response = HttpRequest.post(requestUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .body(JSONUtil.toJsonStr(body))
                .execute()){
            JSONObject res = JSONUtil.parseObj(response.body());
            return this.getResponse(res);
        }
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess("success".equalsIgnoreCase(resJson.getStr("code")));
        smsResponse.setData(resJson);
        smsResponse.setConfigId(this.config.getConfigId());
        return smsResponse;
    }

}
