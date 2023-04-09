package org.dromara.sms.aliyun.service;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendBatchSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendBatchSmsResponse;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teautil.models.RuntimeOptions;
import org.dromara.sms.aliyun.config.AlibabaConfig;
import org.dromara.sms.api.SmsBlend;
import org.dromara.sms.api.callback.CallBack;
import org.dromara.sms.api.entity.SmsResponse;
import org.dromara.sms.comm.annotation.Restricted;
import org.dromara.sms.comm.delayedTime.DelayedTime;
import org.dromara.sms.comm.exception.SmsBlendException;
import org.dromara.sms.comm.utils.http.HttpJsonTool;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executor;

/**
 * <p>类名: AlibabaSmsImpl
 * <p>说明：  阿里云短信实现
 * @author :Wind
 * 2023/3/26  17:16
 **/

@Slf4j
public class AlibabaSmsImpl implements SmsBlend {

    private final Client client;

    private final AlibabaConfig alibabaSmsConfig;

    private final Executor pool;

    private final DelayedTime delayed;

    /**
     *  AlibabaSmsImpl
     * <p>构造器，用于构造短信实现模块
     * @author :Wind
    */

    public AlibabaSmsImpl(Client client, AlibabaConfig alibabaSmsConfig,Executor pool,DelayedTime delayedTime){
        this.client = client;
        this.alibabaSmsConfig = alibabaSmsConfig;
        this.pool = pool;
        this.delayed = delayedTime;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(alibabaSmsConfig.getTemplateName(), message);
        return sendMessage(phone, alibabaSmsConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        SendSmsRequest sendSmsRequest = new SendSmsRequest();
        String s = JSONObject.toJSONString(messages);
        sendSmsRequest.setPhoneNumbers(phone)
                .setTemplateCode(alibabaSmsConfig.getTemplateId())
                .setTemplateParam(s)
                .setSignName(alibabaSmsConfig.getSignature());
        RuntimeOptions runtime = new RuntimeOptions();
        SmsResponse smsResponse = new SmsResponse();
        try {
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtime);
            smsResponse.setBizId(sendSmsResponse.body.getBizId());
            smsResponse.setData(sendSmsResponse.body);
            smsResponse.setCode(String.valueOf(sendSmsResponse.statusCode));
            if (!"OK".equals(sendSmsResponse.body.code)) {
                smsResponse.setErrMessage((sendSmsResponse.body.message));
                smsResponse.setErrorCode(sendSmsResponse.body.code);
            } else {
                smsResponse.setMessage(sendSmsResponse.body.message);
            }
        } catch (TeaException error) {
            log.error(error.getMessage());
            throw new SmsBlendException(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            log.error(_error.getMessage());
            throw new SmsBlendException(error.message);
        }
        return smsResponse;
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(alibabaSmsConfig.getTemplateName(), message);
        return massTexting(phones, alibabaSmsConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        SendBatchSmsRequest sendBatchSmsRequest = new SendBatchSmsRequest();
        sendBatchSmsRequest.setPhoneNumberJson(JSONObject.toJSONString(phones))//群发的手机号
                .setTemplateCode(alibabaSmsConfig.getTemplateId())//模板id
                .setTemplateParamJson(JSONObject.toJSONString(messages))//消息内容
                .setSignNameJson(alibabaSmsConfig.getSignature());//短信签名
        RuntimeOptions runtime = new RuntimeOptions();
        SmsResponse smsResponse = new SmsResponse();
        try {
            SendBatchSmsResponse sendBatchSmsResponse = client.sendBatchSmsWithOptions(sendBatchSmsRequest, runtime);
            smsResponse.setBizId(sendBatchSmsResponse.body.getBizId());
            smsResponse.setData(HttpJsonTool.getJSONObject(sendBatchSmsResponse.body));
            smsResponse.setCode(String.valueOf(sendBatchSmsResponse.statusCode));
            if (!"OK".equals(sendBatchSmsResponse.body.code)) {
                smsResponse.setErrMessage((sendBatchSmsResponse.body.message));
                smsResponse.setErrorCode(sendBatchSmsResponse.body.code);
            } else {
                smsResponse.setMessage(sendBatchSmsResponse.body.message);
            }
        } catch (TeaException error) {
            log.error(error.getMessage());
            throw new SmsBlendException(error.message);
        } catch (Exception _error) {
            TeaException error = new TeaException(_error.getMessage(), _error);
            log.error(error.getMessage());
            throw new SmsBlendException(error.message);
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
}