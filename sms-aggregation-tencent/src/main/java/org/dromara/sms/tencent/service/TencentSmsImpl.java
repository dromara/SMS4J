package org.dromara.sms.tencent.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import com.tencentcloudapi.sms.v20210111.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20210111.models.SendSmsResponse;
import org.dromara.sms.api.SmsBlend;
import org.dromara.sms.api.callback.CallBack;
import org.dromara.sms.api.entity.SmsResponse;
import org.dromara.sms.comm.annotation.Restricted;
import org.dromara.sms.comm.delayedTime.DelayedTime;
import org.dromara.sms.comm.exception.SmsBlendException;
import org.dromara.sms.tencent.config.TencentConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.Executor;
@Slf4j
public class TencentSmsImpl implements SmsBlend {

    private TencentConfig tencentSmsConfig;

    private SmsClient client;

    private Executor pool;

    private DelayedTime delayed;

    public TencentSmsImpl(TencentConfig tencentSmsConfig, SmsClient client, Executor pool, DelayedTime delayed) {
        this.tencentSmsConfig = tencentSmsConfig;
        this.client = client;
        this.pool = pool;
        this.delayed = delayed;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        String[] split = message.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < split.length; i++) {
            map.put(String.valueOf(i),split[i]);
        }
        return sendMessage(phone, tencentSmsConfig.getTemplateId(),map);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        SmsResponse smsResponse = new SmsResponse();
        try {
            SendSmsRequest req = new SendSmsRequest();
            req.setSignName(tencentSmsConfig.getSignature());
            req.setTemplateId(templateId);
            req.setSmsSdkAppId(tencentSmsConfig.getSdkAppId());
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : messages.entrySet()) {
                list.add(entry.getValue());
            }
            String[] s = new String[list.size()];
            req.setTemplateParamSet(list.toArray(s));
            req.setPhoneNumberSet(new String[]{"+86" + phone});
            SendSmsResponse res = client.SendSms(req);
            String s1 = SendSmsResponse.toJsonString(res);
            JSONObject jsonObject = JSON.parseObject(s1);
            if (!"Ok".equals(jsonObject.getString("Code"))) {
                smsResponse.setErrMessage(jsonObject.getString("Message"));
                log.debug(smsResponse.getErrMessage());
            }
            smsResponse.setMessage(jsonObject.getString("Message"));
            smsResponse.setBizId(res.getRequestId());
            smsResponse.setData(jsonObject);
        } catch (TencentCloudSDKException e) {
            throw new SmsBlendException(e.getMessage());
        }
        return smsResponse;
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        String[] split = message.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < split.length; i++) {
            map.put(String.valueOf(i),split[i]);
        }
        return massTexting(phones,tencentSmsConfig.getTemplateId(),map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        SmsResponse smsResponse = new SmsResponse();
        try {
            SendSmsRequest req = new SendSmsRequest();
            req.setSignName(tencentSmsConfig.getSignature());
            req.setTemplateId(templateId);
            req.setSmsSdkAppId(tencentSmsConfig.getSdkAppId());
            List<String> list = new ArrayList<>();
            for (Map.Entry<String, String> entry : messages.entrySet()) {
                list.add(entry.getValue());
            }
            String[] s = new String[list.size()];
            req.setTemplateParamSet(list.toArray(s));
            req.setPhoneNumberSet(arrayToString(phones));
            SendSmsResponse res = client.SendSms(req);
            String s1 = SendSmsResponse.toJsonString(res);
            JSONObject jsonObject = JSON.parseObject(s1);
            if (!"Ok".equals(jsonObject.getString("Code"))) {
                smsResponse.setErrMessage(jsonObject.getString("Message"));
                log.debug(jsonObject.getString("Message"));
            }
            smsResponse.setMessage(jsonObject.getString("Message"));
            smsResponse.setBizId(res.getRequestId());
            smsResponse.setData(jsonObject);
        } catch (TencentCloudSDKException e) {
            throw new SmsBlendException(e.getMessage());
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
        pool.execute(() -> {
            sendMessage(phone, message);
        });
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack) {
        pool.execute(()->{
            SmsResponse smsResponse = sendMessage(phone,templateId,messages);
            callBack.callBack(smsResponse);
        });
    }

    @Override
    @Restricted
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
    @Restricted
    public void delayMassTexting(List<String> phones, String message, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                massTexting(phones,message);
            }
        },delayedTime);
    }

    @Override
    @Restricted
    public void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                massTexting(phones,templateId,messages);
            }
        },delayedTime);
    }

    private String[] arrayToString(List<String> list){
        String[] strs = new String[list.size()];
        List<String> toStr = new ArrayList<>();
        for (String s : list) {
            toStr.add("+86"+s);
        }
        return toStr.toArray(strs);
    }
}
