package org.dromara.sms4j.unisms.service;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;
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
import java.util.TimerTask;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;


/**
 * <p>类名: UniSmsImpl
 * <p>说明：  uniSms短信实现
 * @author :Wind
 * 2023/3/26  17:10
 **/

@Slf4j
public class UniSmsImpl implements SmsBlend {

    private UniConfig config;
    private Executor pool;
    private DelayedTime delayed;

    public UniSmsImpl(UniConfig config, Executor pool, DelayedTime delayed) {
        this.config = config;
        this.pool = pool;
        this.delayed = delayed;
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

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String message, CallBack callBack) {
        CompletableFuture<SmsResponse> smsResponseCompletableFuture = CompletableFuture.supplyAsync(() -> sendMessage(phone, message), pool);
        smsResponseCompletableFuture.thenAcceptAsync(callBack::callBack);
    }

    @Override
    public void sendMessageAsync(String phone, String message) {
        pool.execute(()->{
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
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages) {
        pool.execute(()->{
            sendMessage(phone,templateId,messages);
        });
    }

    @Override
    @Restricted
    public void delayedMessage(String phone, String message, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(phone,message);
            }
        },delayedTime);
    }

    @Override
    @Restricted
    public void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(phone,templateId,messages);
            }
        },delayedTime);
    }

    @Override
    public void delayMassTexting(List<String> phones, String message, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                massTexting(phones,message);
            }
        },delayedTime);
    }

    @Override
    public void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                massTexting(phones,templateId,messages);
            }
        },delayedTime);
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
