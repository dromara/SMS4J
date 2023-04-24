package org.dromara.sms4j.cloopen.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import com.dtflys.forest.Forest;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.cloopen.api.CloopenRestApi;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.cloopen.util.CloopenHelper;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;

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

    private final CloopenRestApi restApi;

    private final CloopenConfig config;

    private final Executor pool;

    private final DelayedTime delayed;

    public CloopenSmsImpl(CloopenConfig config, Executor pool, DelayedTime delayed) {
        this.config = config;
        this.pool = pool;
        this.delayed = delayed;
        restApi = Forest.client(CloopenRestApi.class);
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
        CloopenHelper helper = new CloopenHelper(config);
        Map<String, Object> paramMap = MapUtil.newHashMap(4);
        paramMap.put("to", String.join(",", phones));
        paramMap.put("appId", config.getAppId());
        paramMap.put("templateId", templateId);
        paramMap.put("datas", messages.keySet().stream().map(messages::get).toArray(String[]::new));
        return helper.request(restApi::sendSms, paramMap);
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
