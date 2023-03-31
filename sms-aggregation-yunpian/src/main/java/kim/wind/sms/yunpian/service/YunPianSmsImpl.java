package kim.wind.sms.yunpian.service;

import com.alibaba.fastjson.JSONObject;
import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.api.callback.CallBack;
import kim.wind.sms.comm.annotation.Restricted;
import kim.wind.sms.comm.delayedTime.DelayedTime;
import kim.wind.sms.comm.entity.SmsResponse;
import kim.wind.sms.comm.exception.SmsBlendException;
import kim.wind.sms.comm.utils.HTTPUtils;
import kim.wind.sms.comm.utils.http.OKResponse;
import kim.wind.sms.yunpian.config.YunPianSmsConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;
import java.util.concurrent.Executor;

public class YunPianSmsImpl implements SmsBlend {

    @Autowired
    @Qualifier("smsExecutor")
    private Executor pool;

    @Autowired
    private DelayedTime delayed;

    @Autowired
    private HTTPUtils http ;

    @Autowired
    private YunPianSmsConfig config;

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        Map<String,String> body = new HashMap<>();
        body.put("apikey",config.getApikey());
        body.put("mobile",phone);
        return getSmsResponse(message, body);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        Map<String,String> body = new HashMap<>();
        body.put("apikey",config.getApikey());
        body.put("mobile",phone);
        body.put("tpl_id",templateId);
        body.put("tpl_value",formattingMap(messages));
        Map<String,String> map = new HashMap<>();
        map.put("Accept","application/json;charset=utf-8");
        OKResponse sync = http.setBaseURL("https://sms.yunpian.com/v2").builder()
                .headers(map)
                .postOrBody("/sms/tpl_single_send.json", body)
                .sync();
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setCode(sync.getCode());
        JSONObject jsonObject = HTTPUtils.getJSONObject(sync);
        smsResponse.setData(jsonObject);
        return smsResponse;
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        if (phones.size()>1000){
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
        }
        Map<String,String> body = new HashMap<>();
        body.put("apikey",config.getApikey());
        body.put("mobile",listToString(phones));
        return getSmsResponse(message, body);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (phones.size()>1000){
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于1000");
        }
        Map<String,String> body = new HashMap<>();
        body.put("apikey",config.getApikey());
        body.put("mobile",listToString(phones));
        body.put("tpl_id",templateId);
        body.put("tpl_value",formattingMap(messages));
        Map<String,String> map = new HashMap<>();
        map.put("Accept","application/json;charset=utf-8");
        OKResponse sync = http.setBaseURL("https://sms.yunpian.com/v2").builder()
                .headers(map)
                .postOrBody("/tpl_batch_send.json", body)
                .sync();
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setCode(sync.getCode());
        JSONObject jsonObject = HTTPUtils.getJSONObject(sync);
        smsResponse.setData(jsonObject);
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

    private String formattingMap(Map<String,String> messages){
        StringBuilder str = new StringBuilder();
        for (Map.Entry<String,String> entry : messages.entrySet()) {
            str.append("#");
            str.append(entry.getKey());
            str.append("#=");
            str.append(entry.getValue());
            str.append("&");
        }
        str.deleteCharAt(str.length()-1);
        return str.toString();
    }

    private String listToString(List<String> list){
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i == list.size() - 1) {
                str.append(list.get(i));
            } else {
                str.append(list.get(i));
                str.append(",");
            }
        }
        return str.toString();
    }

    private SmsResponse getSmsResponse(String message, Map<String, String> body) {
        body.put("text",message);
        Map<String,String> map = new HashMap<>();
        map.put("Accept","application/json;charset=utf-8");
        OKResponse sync = http.setBaseURL("http://sms.yunpian.com/v2").builder()
                .headers(map)
                .postOrBody("/sms/single_send.json", body)
                .sync();
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setCode(sync.getCode());
        JSONObject jsonObject = HTTPUtils.getJSONObject(sync);
        smsResponse.setData(jsonObject);
        return smsResponse;
    }
}
