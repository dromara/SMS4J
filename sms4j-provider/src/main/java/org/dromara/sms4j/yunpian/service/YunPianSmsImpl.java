package org.dromara.sms4j.yunpian.service;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import org.dromara.sms4j.yunpian.config.YunpianConfig;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * @author wind
 */
@Slf4j
public class YunPianSmsImpl extends AbstractSmsBlend<YunpianConfig> {

    private int retry = 0;

    public YunPianSmsImpl(YunpianConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
    }

    public YunPianSmsImpl(YunpianConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.YUNPIAN;
    }

    private SmsResponse getResponse(JSONObject execute) {
        SmsResponse smsResponse = new SmsResponse();
        if (execute == null) {
            smsResponse.setSuccess(false);
            return smsResponse;
        }
        smsResponse.setSuccess(execute.getInt("code") == 0);
        smsResponse.setData(execute);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        Map<String, Object> body = setBody(phone, message, null, getConfig().getTemplateId());
        Map<String, String> headers = getHeaders();

        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postFrom(Constant.YUNPIAN_URL + "/sms/tpl_single_send.json", headers, body));
        } catch (SmsBlendException e) {
            smsResponse = new SmsResponse();
            smsResponse.setSuccess(false);
            smsResponse.setData(e.getMessage());
        }
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phone, message);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<>();
        }
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    private SmsResponse requestRetry(String phone, String message) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {" + retry + "} 次重新发送");
        return sendMessage(phone, message);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        Map<String, Object> body = setBody(phone, "", messages, templateId);
        Map<String, String> headers = getHeaders();

        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postFrom(Constant.YUNPIAN_URL + "/sms/tpl_single_send.json", headers, body));
        } catch (SmsBlendException e) {
            return requestRetry(phone, templateId, messages);
        }
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phone, templateId, messages);
    }

    private SmsResponse requestRetry(String phone, String templateId, LinkedHashMap<String, String> messages) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {" + retry + "} 次重新发送");
        return sendMessage(phone, templateId, messages);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        if (phones.size() > 1000) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
        }
        return sendMessage(SmsUtils.listToString(phones), message);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (phones.size() > 1000) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
        }
        return sendMessage(SmsUtils.listToString(phones), templateId, messages);
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

    private Map<String, Object> setBody(String phone, String mes, LinkedHashMap<String, String> messages, String tplId) {
        LinkedHashMap<String, String> message = new LinkedHashMap<>();
        if (mes.isEmpty()) {
            message = messages;
        } else {
            message.put(getConfig().getTemplateName(), mes);
        }
        Map<String, Object> body = new HashMap<>();
        body.put("apikey", getConfig().getAccessKeyId());
        body.put("mobile", phone);
        body.put("tpl_id", tplId);
        if (message != null && !message.isEmpty()) {
            body.put("tpl_value", formattingMap(message));
        } else {
            body.put("tpl_value", "");
        }
        if (getConfig().getCallbackUrl() != null && !getConfig().getCallbackUrl().isEmpty()) {
            body.put("callback_url", getConfig().getCallbackUrl());
        }
        return body;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", Constant.APPLICATION_JSON_UTF8);
        headers.put("Content-Type", Constant.FROM_URLENCODED);
        return headers;
    }
}
