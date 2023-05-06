package org.dromara.sms4j.tencent.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.config.ForestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.tencent.utils.TencentUtils;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Slf4j
public class TencentSmsImpl implements SmsBlend {

    private TencentConfig tencentSmsConfig;

    private Executor pool;

    private DelayedTime delayed;

    private final ForestConfiguration http = BeanFactory.getForestConfiguration();

    public TencentSmsImpl(TencentConfig tencentSmsConfig, Executor pool, DelayedTime delayed) {
        this.tencentSmsConfig = tencentSmsConfig;
        this.pool = pool;
        this.delayed = delayed;
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
        return getSmsResponse(arrayToString(phones), list.toArray(s), templateId);
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
                    JSONArray sendStatusSet = response.getJSONArray("SendStatusSet");
                    smsResponse.setBizId(sendStatusSet.getJSONObject(0).getString("SerialNo"));
                    smsResponse.setMessage(sendStatusSet.getJSONObject(0).getString("Message"));
                    smsResponse.setCode(sendStatusSet.getJSONObject(0).getString("Code"));
                }))
                .onError((ex, req, res) -> {
                    JSONObject jsonBody = res.get(JSONObject.class);
                    JSONObject response = jsonBody.getJSONObject("Response");
                    JSONArray sendStatusSet = response.getJSONArray("SendStatusSet");
                    smsResponse.setErrMessage(sendStatusSet.getJSONObject(0).getString("Message"));
                    smsResponse.setErrorCode(sendStatusSet.getJSONObject(0).getString("Code"));
                })
                .execute();
        return smsResponse;
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String message, CallBack callBack) {
        CompletableFuture<SmsResponse> smsResponseCompletableFuture = CompletableFuture.supplyAsync(() -> sendMessage(phone, message), pool);
        smsResponseCompletableFuture.thenAcceptAsync(callBack::callBack);
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String message) {
        pool.execute(() -> {
            sendMessage(phone, message);
        });
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack) {
        CompletableFuture<SmsResponse> smsResponseCompletableFuture = CompletableFuture.supplyAsync(() -> sendMessage(phone, templateId, messages), pool);
        smsResponseCompletableFuture.thenAcceptAsync(callBack::callBack);
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages) {
        pool.execute(() -> {
            sendMessage(phone, templateId, messages);
        });
    }

    @Override
    @Restricted
    public void delayedMessage(String phone, String message, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(phone, message);
            }
        }, delayedTime);
    }

    @Override
    @Restricted
    public void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(phone, templateId, messages);
            }
        }, delayedTime);
    }

    @Override
    @Restricted
    public void delayMassTexting(List<String> phones, String message, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                massTexting(phones, message);
            }
        }, delayedTime);
    }

    @Override
    @Restricted
    public void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                massTexting(phones, templateId, messages);
            }
        }, delayedTime);
    }

    private String[] arrayToString(List<String> list) {
        String[] strs = new String[list.size()];
        List<String> toStr = new ArrayList<>();
        for (String s : list) {
            toStr.add("+86" + s);
        }
        return toStr.toArray(strs);
    }
}
