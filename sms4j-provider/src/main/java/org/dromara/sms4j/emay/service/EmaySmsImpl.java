package org.dromara.sms4j.emay.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.emay.util.EmayBuilder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import static org.dromara.sms4j.comm.utils.SmsUtil.listToString;

/**
 * @author Richard
 * @date 2023-04-11 12:00
 */
@Slf4j
public class EmaySmsImpl extends AbstractSmsBlend {
    public EmaySmsImpl(EmayConfig config, Executor pool, DelayedTime delayed) {
        super(pool,delayed);
        this.config = config;
    }

    private EmayConfig config;

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        String url = config.getRequestUrl();
        Map<String, Object> params;
        try {
            params = EmayBuilder.buildRequestBody(config.getAppId(), config.getSecretKey(), phone, message);
        } catch (SmsBlendException e) {
            SmsResponse smsResponse = new SmsResponse();
            smsResponse.setErrMessage(e.getMessage());
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
        return sendMessage(listToString(phones), message);
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
        return sendMessage(listToString(phones), EmayBuilder.listToString(list));
    }

    private SmsResponse getSendResponse(Map<String, Object> body, String requestUrl) {
        AtomicReference<SmsResponse> smsResponse = new AtomicReference<>();
        http.post(requestUrl)
                .addBody(body)
                .onSuccess(((data, req, res) -> {
                    JSONObject jsonBody = res.get(JSONObject.class);
                    smsResponse.set(getSmsResponse(jsonBody));
                }))
                .onError((ex, req, res) -> {
                    JSONObject jsonBody = res.get(JSONObject.class);
                    smsResponse.set(getSmsResponse(jsonBody));
                })
                .execute();

        return smsResponse.get();
    }


    private static SmsResponse getSmsResponse(JSONObject execute) {
        SmsResponse smsResponse = new SmsResponse();
        if (execute == null ){
            smsResponse.setErrorCode("500");
            smsResponse.setErrMessage("emay send sms response is null.check param");
            return smsResponse;
        }
        String code = execute.getString("code");
        if (SmsUtil.isEmpty(code)) {
            smsResponse.setErrorCode("emay response code is null");
            smsResponse.setErrMessage("emay is error");
        } else {
            smsResponse.setCode(code);
            if ("success".equalsIgnoreCase(code)) {
                JSONArray data = execute.getJSONArray("data");
                JSONObject result = (JSONObject) data.get(0);
                String smsId = result.getString("smsId");
                smsResponse.setBizId(smsId);
            }
            smsResponse.setData(execute);
        }
        return smsResponse;
    }
}
