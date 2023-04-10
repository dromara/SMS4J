package org.dromara.sms.jdcloud.service;

import cn.hutool.core.util.IdUtil;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import com.jdcloud.sdk.service.sms.model.BatchSendRequest;
import com.jdcloud.sdk.service.sms.model.BatchSendResult;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms.api.SmsBlend;
import org.dromara.sms.api.callback.CallBack;
import org.dromara.sms.api.entity.SmsResponse;
import org.dromara.sms.comm.annotation.Restricted;
import org.dromara.sms.comm.delayedTime.DelayedTime;
import org.dromara.sms.comm.exception.SmsBlendException;
import org.dromara.sms.jdcloud.config.JdCloudConfig;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 京东云短信实现
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
@Slf4j
public class JdCloudSmsImpl implements SmsBlend {

    private final SmsClient client;

    private final JdCloudConfig config;

    private final Executor pool;

    private final DelayedTime delayed;

    public JdCloudSmsImpl(SmsClient client, JdCloudConfig config, Executor pool, DelayedTime delayed) {
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
        try {
            BatchSendRequest request = new BatchSendRequest();
            request.setPhoneList(phones);
            request.setRegionId(config.getRegion());
            request.setTemplateId(templateId);
            request.setSignId(config.getSignature());
            List<String> params = messages.entrySet().stream().map(messages::get)
                    .collect(Collectors.toList());
            request.setParams(params);

            BatchSendResult result = client.batchSend(request).getResult();
            return getSmsResponse(result);
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
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

    /**
     * 获取短信返回信息
     *
     * @param res 云商原始响应信息
     * @return 发送短信返回信息
     */
    private SmsResponse getSmsResponse(BatchSendResult res) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setBizId(res.getData().getSequenceNumber());
        smsResponse.setData(res.getData());
        smsResponse.setCode(String.valueOf(res.getCode()));
        smsResponse.setMessage(res.getMessage());
        Boolean status = res.getStatus();
        boolean isSuccess = status != null && status;
        if (!isSuccess) {
            smsResponse.setErrMessage(res.getMessage());
            smsResponse.setErrorCode(String.valueOf(res.getCode()));
        }
        return smsResponse;
    }
}
