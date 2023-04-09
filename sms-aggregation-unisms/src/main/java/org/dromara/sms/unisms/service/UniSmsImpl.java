package org.dromara.sms.unisms.service;

import com.apistd.uni.UniResponse;
import com.apistd.uni.sms.UniMessage;
import com.apistd.uni.sms.UniSMS;
import org.dromara.sms.api.SmsBlend;
import org.dromara.sms.api.callback.CallBack;
import org.dromara.sms.api.entity.SmsResponse;
import org.dromara.sms.comm.annotation.Restricted;
import org.dromara.sms.comm.delayedTime.DelayedTime;
import org.dromara.sms.comm.exception.SmsBlendException;
import org.dromara.sms.comm.utils.http.HttpJsonTool;
import org.dromara.sms.unisms.config.UniConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executor;


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
        UniMessage uniMes = UniSMS.buildMessage().setSignature(config.getSignature()).setTo(phone)
                .setTemplateId(templateId)
                .setTemplateData(messages);
        return getSmsResponse(uniMes);
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
        String[] s = new String[phones.size()];
        UniMessage uniMes = UniSMS.buildMessage().setSignature(config.getSignature()).setTo(phones.toArray(s))
                .setTemplateId(templateId)
                .setTemplateData(messages);
        return getSmsResponse(uniMes);
    }

    @Override
    @Restricted
    public void sendMessageAsync(String phone, String message, CallBack callBack) {
        pool.execute(()->{
            SmsResponse smsResponse = sendMessage(phone, message);
            callBack.callBack(smsResponse);
        });
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
        pool.execute(()->{
            SmsResponse smsResponse = sendMessage(phone,templateId,messages);
            callBack.callBack(smsResponse);
        });
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

    private SmsResponse getSmsResponse(UniMessage uniMes) {
        SmsResponse smsResponse = new SmsResponse();
        try {
            UniResponse send = uniMes.send();
            smsResponse.setCode(String.valueOf(send.status));
            smsResponse.setErrorCode(send.code);
            smsResponse.setMessage(send.message);
            smsResponse.setBizId(send.requestId);
            smsResponse.setData(HttpJsonTool.getJSONObject(send));
        }catch(Exception e){
            smsResponse.setErrMessage(e.getMessage());
        }

        return smsResponse;
    }
}
