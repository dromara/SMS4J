package org.dromara.sms4j.cloopen.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * 容联云短信实现
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@Slf4j
public class CloopenSmsImpl implements SmsBlend {

    private final CCPRestSmsSDK client;

    private final CloopenConfig config;

    private final Executor pool;

    private final DelayedTime delayed;

    public CloopenSmsImpl(CCPRestSmsSDK client, CloopenConfig config, Executor pool, DelayedTime delayed) {
        this.client = client;
        this.config = config;
        this.pool = pool;
        this.delayed = delayed;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        return massTexting(Collections.singletonList(phone), message);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return massTexting(Collections.singletonList(phone), templateId, messages);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(IdUtil.fastSimpleUUID(), message);
        return massTexting(phones, config.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        SmsResponse smsResponse = new SmsResponse();
        Map<String, Object> result = null;
        try {
            String[] datas = messages.keySet().stream().map(messages::get).toArray(String[]::new);
            result = client.sendTemplateSMS(String.join(",", phones), templateId, datas);

            String statusCode = Convert.toStr(result.get("statusCode"));
            String statusMsg = Convert.toStr(result.get("statusMsg"));
            smsResponse.setData(result.get("data"));
            smsResponse.setCode(statusCode);
            smsResponse.setMessage(statusMsg);
            boolean isSuccess = "000000".equals(statusCode);
            if (!isSuccess) {
                smsResponse.setErrMessage(statusMsg);
                smsResponse.setErrorCode(statusCode);
            } else {
                Object bizId = JSONUtil.getByPath(JSONUtil.parse(result.get("data")), "templateSMS.smsMessageSid");
                smsResponse.setBizId(Convert.toStr(bizId));
            }
        } catch (Exception e) {
            if (result != null) {
                smsResponse.setErrMessage(Convert.toStr(result.get("statusMsg")));
                smsResponse.setErrorCode(Convert.toStr(result.get("statusCode")));
            } else {
                throw new SmsBlendException(e.getMessage());
            }
        }
        return smsResponse;
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String message, CallBack callBack) {
        pool.execute(() -> {
            SmsResponse smsResponse = sendMessage(phone, message);
            callBack.callBack(smsResponse);
        });
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String message) {
        pool.execute(() -> sendMessage(phone, message));
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack) {
        pool.execute(() -> {
            SmsResponse smsResponse = sendMessage(phone, templateId, messages);
            callBack.callBack(smsResponse);
        });
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
}
