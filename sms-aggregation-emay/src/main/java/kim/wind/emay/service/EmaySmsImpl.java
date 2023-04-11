package kim.wind.emay.service;

import com.alibaba.fastjson.JSONObject;
import com.dtflys.forest.config.ForestConfiguration;
import kim.wind.emay.config.EmayConfig;
import kim.wind.emay.util.EmayBuilder;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms.api.SmsBlend;
import org.dromara.sms.api.callback.CallBack;
import org.dromara.sms.api.entity.SmsResponse;
import org.dromara.sms.comm.annotation.Restricted;
import org.dromara.sms.comm.delayedTime.DelayedTime;
import org.dromara.sms.comm.exception.SmsBlendException;
import org.dromara.sms.comm.factory.BeanFactory;

import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

import static org.dromara.sms.comm.utils.SmsUtil.listToString;

/**
 * @author Richard
 * @date 2023-04-11 12:00
 */
@Slf4j
public class EmaySmsImpl implements SmsBlend {
    public EmaySmsImpl(EmayConfig config, Executor pool, DelayedTime delayed) {
        this.config = config;
        this.pool = pool;
        this.delayed = delayed;
    }

    private EmayConfig config;

    private Executor pool;

    private DelayedTime delayed;

    private final ForestConfiguration http = BeanFactory.getForestConfiguration();

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        String url = config.getRequestUrl();
        Map<String, Object> params;
        try {
            params = EmayBuilder.buildRequestBody(config.getAppId(), config.getSecretKey(), phone, message);
        } catch (SmsBlendException e) {
            SmsResponse smsResponse = new SmsResponse();
            smsResponse.setErrMessage(e.getMessage());
            return smsResponse;
        }
        return getSendResponse(params, url);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        return sendMessage(phone, EmayBuilder.listToString(list));
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        if (phones.size() > 500) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于500");
        }
        return sendMessage(listToString(phones), message);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (phones.size() > 500) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于500");
        }
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        return sendMessage(listToString(phones), EmayBuilder.listToString(list));
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

    private SmsResponse getSendResponse(Map<String, Object> body, String requestUrl) {
        AtomicReference<SmsResponse> smsResponse = new AtomicReference<>();
        http.post(requestUrl)
                .addBody(body)
                .onSuccess(((data, req, res) -> {
                    JSONObject jsonBody = res.get(JSONObject.class);
                    smsResponse.set(getSmsResponse(jsonBody));
                }))
                .onError((ex, req, res) -> {
                    JSONObject jsonBody = res.get(JSONObject.class);
                    smsResponse.set(getSmsResponse(jsonBody));
                })
                .execute();

        return smsResponse.get();
    }


    private static SmsResponse getSmsResponse(JSONObject execute) {
        SmsResponse smsResponse = new SmsResponse();
        String code = execute.getString("code");
        smsResponse.setCode(code);
        if ("success".equalsIgnoreCase(code)) {
            JSONObject data = execute.getJSONObject("data");
            String smsId = data.getString("smsId");
            smsResponse.setBizId(smsId);
        }
        smsResponse.setData(execute);
        return smsResponse;
    }
}
