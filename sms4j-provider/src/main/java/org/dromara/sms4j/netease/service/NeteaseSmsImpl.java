package org.dromara.sms4j.netease.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.netease.utils.NeteaseUtils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created with IntelliJ IDEA.
 *
 * @author adam
 * @create 2023/5/29 17:34
 */

@Slf4j
public class NeteaseSmsImpl extends AbstractSmsBlend {

    private NeteaseConfig config;

    public NeteaseSmsImpl(NeteaseConfig config, Executor pool, DelayedTime delayed) {
        super(pool, delayed);
        this.config = config;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        return getSmsResponse(config.getTemplateUrl(), Collections.singletonList(phone), message, config.getTemplateId());
    }


    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String url = config.getTemplateUrl();
        String messageStr = messages.get("message");
        return getSmsResponse(url, Collections.singletonList(phone), messageStr, templateId);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        if (phones.size() > 100) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于100");
        }
        return getSmsResponse(config.getTemplateUrl(), phones, config.getTemplateId(), message);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (phones.size() > 100) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于100");
        }
        String url = config.getTemplateUrl();
        String messageStr = messages.get("message");
        return getSmsResponse(url, phones, messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String requestUrl, List<String> phones, String message, String templateId) {
        AtomicReference<SmsResponse> reference = new AtomicReference<>();
        String nonce = IdUtil.fastSimpleUUID();
        String curTime = String.valueOf(DateUtil.currentSeconds());
        String checkSum = NeteaseUtils.getCheckSum(config.getSecret(), nonce, curTime);
        Map<String, Object> body = new LinkedHashMap<>(4);
        body.put("templateid", templateId);
        body.put("mobiles", JSONArray.parseArray(JSON.toJSONString(phones)).toJSONString());
        body.put("params", message);
        body.put("needUp", config.getNeedUp());
        http.post(requestUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("AppKey", config.getAppKey())
                .addHeader("Nonce", nonce)
                .addHeader("CurTime", curTime)
                .addHeader("CheckSum", checkSum)
                .addBody(body)
                .onSuccess(((data, req, res) -> {
                    reference.set(this.getResponse(res.get(JSONObject.class)));
                }))
                .onError((ex, req, res) -> {
                    reference.set(this.getResponse(res.get(JSONObject.class)));
                })
                .execute();
        return reference.get();
    }

    private SmsResponse getResponse(JSONObject jsonObject) {
        SmsResponse response = new SmsResponse();
        Integer code = jsonObject.getInteger("code");
        if (code > 200) {
            response.setErrorCode(String.valueOf(code));
            response.setErrMessage(jsonObject.getString("msg"));
        } else {
            response.setCode(String.valueOf(code));
            response.setMessage(jsonObject.getString("msg"));
            response.setData(jsonObject.get("obj"));
        }
        return response;
    }

}
