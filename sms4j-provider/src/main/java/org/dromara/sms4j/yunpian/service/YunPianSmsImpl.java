package org.dromara.sms4j.yunpian.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.yunpian.config.YunpianConfig;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author wind
 */
public class YunPianSmsImpl extends AbstractSmsBlend {

    public YunPianSmsImpl(YunpianConfig config, Executor pool, DelayedTime delayed) {
        super(pool, delayed);
        this.config = config;
    }

    private final YunpianConfig config;

    private SmsResponse getResponse(JSONObject execute) {
        SmsResponse smsResponse = new SmsResponse();
        if (execute == null) {
            smsResponse.setSuccess(false);
            return smsResponse;
        }
        smsResponse.setSuccess(execute.getInt("code") == 0);
        smsResponse.setData(execute);
        smsResponse.setConfigId(this.config.getConfigId());
        return smsResponse;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        Map<String, String> body = setBody(phone, message, null, config.getTemplateId());
        return getSendResponse(body);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        Map<String, String> body = setBody(phone, "", messages, templateId);
        return getSendResponse(body);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        if (phones.size() > 1000) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
        }
        return sendMessage(SmsUtil.listToString(phones), message);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (phones.size() > 1000) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
        }
        return sendMessage(SmsUtil.listToString(phones), templateId, messages);
    }

    private String formattingMap(Map<String, String> messages) {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            str.append("#");
            str.append(entry.getKey());
            str.append("#=");
            str.append(entry.getValue());
            str.append("&");
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    private Map<String, String> setBody(String phone, String mes, LinkedHashMap<String, String> messages, String tplId) {
        LinkedHashMap<String, String> message = new LinkedHashMap<>();
        if (mes.isEmpty()) {
            message = messages;
        } else {
            message.put(config.getTemplateName(), mes);
        }
        Map<String, String> body = new HashMap<>();
        body.put("apikey", config.getAccessKeyId());
        body.put("mobile", phone);
        body.put("tpl_id", tplId);
        body.put("tpl_value", formattingMap(message));
        if (config.getCallbackUrl() != null && !config.getCallbackUrl().isEmpty())
            body.put("callback_url", config.getCallbackUrl());
        return body;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json;charset=utf-8");
        headers.put("Content-Type", Constant.FROM_URLENCODED);
        return headers;
    }

    private SmsResponse getSendResponse(Map<String, String> body) {
        Map<String, String> headers = getHeaders();
        try(HttpResponse response = HttpRequest.post(Constant.YUNPIAN_URL + "/sms/tpl_single_send.json")
                .addHeaders(headers)
                .body(JSONUtil.toJsonStr(body))
                .execute()){
            JSONObject res = JSONUtil.parseObj(response.body());
            return getResponse(res);
        }
    }
}
