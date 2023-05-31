package org.dromara.sms4j.tencent.service;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.jdcloud.sdk.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.tencent.utils.TencentUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author wind
 */
@Slf4j
public class TencentSmsImpl extends AbstractSmsBlend {

    private final TencentConfig tencentSmsConfig;

    public TencentSmsImpl(TencentConfig tencentSmsConfig, Executor pool, DelayedTime delayed) {
        super(pool, delayed);
        this.tencentSmsConfig = tencentSmsConfig;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        String[] split = message.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < split.length; i++) {
            map.put(String.valueOf(i), split[i]);
        }
        return sendMessage(phone, tencentSmsConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        String[] s = new String[list.size()];
        return getSmsResponse(new String[]{"+86" + phone}, list.toArray(s), templateId);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        String[] split = message.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < split.length; i++) {
            map.put(String.valueOf(i), split[i]);
        }
        return massTexting(phones, tencentSmsConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        String[] s = new String[list.size()];
        return getSmsResponse(SmsUtil.listToArray(phones), list.toArray(s), templateId);
    }

    private SmsResponse getSmsResponse(String[] phones, String[] messages, String templateId) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String signature;
        try {
            signature = TencentUtils.generateSignature(this.tencentSmsConfig, templateId, messages, phones, timestamp);
        } catch (Exception e) {
            log.error("tencent send message error", e);
            throw new SmsBlendException(e.getMessage());
        }
        Map<String, Object> headsMap = TencentUtils.generateHeadsMap(signature, timestamp, tencentSmsConfig.getAction(),
                tencentSmsConfig.getVersion(), tencentSmsConfig.getTerritory(), tencentSmsConfig.getRequestUrl());
        Map<String, Object> requestBody = TencentUtils.generateRequestBody(phones, tencentSmsConfig.getSdkAppId(),
                tencentSmsConfig.getSignature(), templateId, messages);
        SmsResponse smsResponse = new SmsResponse();
        String url = Constant.HTTPS_PREFIX + tencentSmsConfig.getRequestUrl();
        http.post(url)
                .addHeader(headsMap)
                .addBody(requestBody)
                .onSuccess(((data, req, res) -> {
                    JSONObject jsonBody = res.get(JSONObject.class);
                    JSONObject response = jsonBody.getJSONObject("Response");
                    String error = response.getStr("Error");
                    if (StringUtils.isNotBlank(error)){
                        smsResponse.setErrorCode("500");
                        smsResponse.setErrMessage(error);
                    }else {
                        JSONArray sendStatusSet = response.getJSONArray("SendStatusSet");
                        smsResponse.setBizId(sendStatusSet.getJSONObject(0).getStr("SerialNo"));
                        smsResponse.setMessage(sendStatusSet.getJSONObject(0).getStr("Message"));
                        smsResponse.setCode(sendStatusSet.getJSONObject(0).getStr("Code"));
                    }
                }))
                .onError((ex, req, res) -> {
                    JSONObject jsonBody = res.get(JSONObject.class);
                    if (jsonBody == null) {
                        smsResponse.setErrorCode("500");
                        smsResponse.setErrMessage("tencent send sms response is null.check param");
                    } else {
                        JSONObject response = jsonBody.getJSONObject("Response");
                        JSONArray sendStatusSet = response.getJSONArray("SendStatusSet");
                        smsResponse.setErrMessage(sendStatusSet.getJSONObject(0).getStr("Message"));
                        smsResponse.setErrorCode(sendStatusSet.getJSONObject(0).getStr("Code"));
                    }
                })
                .execute();
        return smsResponse;
    }

}