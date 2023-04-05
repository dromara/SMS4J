package kim.wind.sms.yunpian.service;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.config.ForestConfiguration;
import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.api.callback.CallBack;
import kim.wind.sms.comm.annotation.Restricted;
import kim.wind.sms.comm.constant.Constant;
import kim.wind.sms.comm.delayedTime.DelayedTime;
import kim.wind.sms.comm.entity.SmsResponse;
import kim.wind.sms.comm.exception.SmsBlendException;
import kim.wind.sms.yunpian.config.YunPianSmsConfig;
import kim.wind.sms.yunpian.config.YunpianConfig;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import static kim.wind.sms.comm.utils.SmsUtil.listToString;

public class YunPianSmsImpl implements SmsBlend {

    @Autowired
    @Qualifier("smsExecutor")
    private Executor pool;

    @Autowired
    private DelayedTime delayed;

    @Autowired
    private YunpianConfig config;

    @Autowired
    private ForestConfiguration http;

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        Map<String, String> body = setBody(phone, message, null);
        return getSendResponse(body);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        Map<String, String> body = setBody(phone, "", messages);
        return getSendResponse(body);
    }


    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        if (phones.size() > 1000) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
        }
       return sendMessage(listToString(phones),message);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (phones.size() > 1000) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
        }
        return sendMessage(listToString(phones), templateId, messages);
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

    private String formattingMap(Map<String, String> messages) {
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            str.append("#");
            str.append(entry.getKey());
            str.append("#=");
            str.append(entry.getValue());
            str.append("&");
        }
        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    private Map<String, String> setBody(String phone, String mes, LinkedHashMap<String, String> messages) {
        LinkedHashMap<String, String> message = new LinkedHashMap<>();
        if (mes.isEmpty()) {
            message = messages;
        } else {
            message.put(config.getTemplateName(), mes);
        }
        Map<String, String> body = new HashMap<>();
        body.put("apikey", config.getApikey());
        body.put("mobile", phone);
        body.put("tpl_id", config.getTemplateId());
        body.put("tpl_value", formattingMap(message));
        if (!config.getCallbackUrl().isEmpty()) body.put("callback_url", config.getCallbackUrl());
        return body;
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json;charset=utf-8");
        headers.put("Content-Type", Constant.FROM_URLENCODED);
        return headers;
    }

    @NotNull
    private static SmsResponse getSmsResponse(JSONObject execute) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setCode(execute.getString("code"));
        smsResponse.setMessage(execute.getString("msg"));
        smsResponse.setBizId(execute.getString("sid"));
        if (execute.getInteger("code") != 0) {
            smsResponse.setErrMessage(execute.getString("msg"));
        }
        smsResponse.setData(execute);
        return smsResponse;
    }

    private SmsResponse getSendResponse(Map<String, String> body) {
        Map<String, String> headers = getHeaders();
        AtomicReference<SmsResponse> smsResponse = null;
        http.post(Constant.YUNPIAN_URL + "/sms/tpl_single_send.json")
                .addHeader(headers)
                .addBody(body)
                .onSuccess(((data,req,res)->{
                    JSONObject jsonBody = res.get(JSONObject.class);
                    smsResponse.set(getSmsResponse(jsonBody));
                }))
                .onError((ex,req,res)->{
                    JSONObject jsonBody = res.get(JSONObject.class);
                    smsResponse.set(getSmsResponse(jsonBody));
                })
                .execute();

        return smsResponse.get();
    }
}
