package org.dromara.sms4j.ctyun.service;

import com.alibaba.fastjson.JSON;
import com.dtflys.forest.config.ForestConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.ctyun.utils.CtyunUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>类名: CtyunSmsImpl
 * <p>说明： 天翼云短信实现
 *
 * @author :bleachhtred
 * 2023/5/12  15:06
 **/
@Slf4j
public class CtyunSmsImpl implements SmsBlend {

    private final CtyunConfig ctyunConfig;

    private final Executor pool;

    private final DelayedTime delayed;

    private final ForestConfiguration http = BeanFactory.getForestConfiguration();

    /**
     * AlibabaSmsImpl
     * <p>构造器，用于构造短信实现模块
     *
     * @author :Wind
     */

    public CtyunSmsImpl(CtyunConfig ctyunConfig, Executor pool, DelayedTime delayedTime) {
        this.ctyunConfig = ctyunConfig;
        this.pool = pool;
        this.delayed = delayedTime;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ctyunConfig.getTemplateName(), message);
        return sendMessage(phone, ctyunConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSON.toJSONString(messages);
        return getSmsResponse(phone, messageStr, templateId);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ctyunConfig.getTemplateName(), message);
        return massTexting(phones, ctyunConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSON.toJSONString(messages);
        return getSmsResponse(arrayToString(phones), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
        AtomicReference<SmsResponse> smsResponse = new AtomicReference<>();
        String requestUrl;
        String paramStr;
        try {
            requestUrl = ctyunConfig.getRequestUrl();
            paramStr = CtyunUtils.generateParamJsonStr(ctyunConfig, phone, message, templateId);
        } catch (Exception e) {
            log.error("ctyun send message error", e);
            throw new SmsBlendException(e.getMessage());
        }
        log.debug("requestUrl {}", requestUrl);
        http.post(requestUrl)
                .addHeader(CtyunUtils.signHeader(paramStr, ctyunConfig.getAccessKeyId(), ctyunConfig.getAccessKeySecret()))
                .addBody(paramStr)
                .onSuccess(((data, req, res) -> {
                    Map map = res.get(Map.class);
                    smsResponse.set(getResponse(map));
                }))
                .onError((ex, req, res) -> {
                    Map map = res.get(Map.class);
                    smsResponse.set(getResponse(map));
                })
                .execute();
        return smsResponse.get();
    }

    private static SmsResponse getResponse(Map map) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setCode((String) map.get("code"));
        smsResponse.setMessage((String) map.get("message"));
        smsResponse.setBizId((String) map.get("requestId"));
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
        pool.execute(() -> sendMessage(phone, message));
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack) {
        CompletableFuture<SmsResponse> smsResponseCompletableFuture = CompletableFuture.supplyAsync(() -> sendMessage(phone,templateId, messages), pool);
        smsResponseCompletableFuture.thenAcceptAsync(callBack::callBack);
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages) {
        pool.execute(() -> sendMessage(phone, templateId, messages));
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

    private String arrayToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(",").append("+86").append(s);
        }
        return sb.substring(1);
    }
}
