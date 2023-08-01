package org.dromara.sms4j.netease.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.netease.utils.NeteaseUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author adam
 * @create 2023/5/29 17:34
 */

@Slf4j
public class NeteaseSmsImpl extends AbstractSmsBlend<NeteaseConfig> {

    public static final String SUPPLIER = "netease";

    public NeteaseSmsImpl(NeteaseConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
    }

    public NeteaseSmsImpl(NeteaseConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SUPPLIER;
    }

    /**
     * @param phone   接收短信的手机号
     * @param message 短信变量 ["xxx","yyy"]
     * @return
     */
    @Override
    public SmsResponse sendMessage(String phone, String message) {
        Optional.ofNullable(phone).orElseThrow(() -> new SmsBlendException("手机号不能为空"));
        Optional.ofNullable(getConfig().getTemplateId()).orElseThrow(() -> new SmsBlendException("模板ID不能为空"));
        return getSmsResponse(getConfig().getTemplateUrl(), Collections.singletonList(phone), message, getConfig().getTemplateId());
    }


    /**
     * @param phone
     * @param templateId 模板id
     * @param messages   短信变量 key为默认 params value为模板变量值 ["xxx","yyy"]
     * @return
     */
    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        Optional.ofNullable(phone).orElseThrow(() -> new SmsBlendException("手机号不能为空"));
        Optional.ofNullable(getConfig().getTemplateId()).orElseThrow(() -> new SmsBlendException("模板ID不能为空"));
        String messageStr = messages.get("params");
        return getSmsResponse(getConfig().getTemplateUrl(), Collections.singletonList(phone), messageStr, templateId);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        if (phones.size() < 1) {
            throw new SmsBlendException("手机号不能为空");
        }
        if (phones.size() > 100) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于100");
        }
        Optional.ofNullable(getConfig().getTemplateId()).orElseThrow(() -> new SmsBlendException("模板ID不能为空"));
        return getSmsResponse(getConfig().getTemplateUrl(), phones, getConfig().getTemplateId(), message);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (phones.size() > 100) {
            throw new SmsBlendException("单次发送超过最大发送上限，建议每次群发短信人数低于100");
        }
        Optional.ofNullable(getConfig().getTemplateId()).orElseThrow(() -> new SmsBlendException("模板ID不能为空"));
        String messageStr = messages.get("message");
        return getSmsResponse(getConfig().getTemplateUrl(), phones, messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String requestUrl, List<String> phones, String message, String templateId) {
        String nonce = IdUtil.fastSimpleUUID();
        String curTime = String.valueOf(DateUtil.currentSeconds());
        String checkSum = NeteaseUtils.getCheckSum(getConfig().getAccessKeySecret(), nonce, curTime);
        Map<String, Object> body = new LinkedHashMap<>(4);
        body.put("templateid", templateId);
        JSONArray jsonArray = JSONUtil.createArray();
        jsonArray.addAll(phones);
        body.put("mobiles", jsonArray.toString());
        body.put("params", message);
        body.put("needUp", getConfig().getNeedUp());
        try(HttpResponse response = HttpRequest.post(requestUrl)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("AppKey", getConfig().getAccessKeyId())
                .header("Nonce", nonce)
                .header("CurTime", curTime)
                .header("CheckSum", checkSum)
                .body(JSONUtil.toJsonStr(body))
                .execute()){
            JSONObject res = JSONUtil.parseObj(response.body());
            return this.getResponse(res);
        }
    }

    private SmsResponse getResponse(JSONObject jsonObject) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(jsonObject.getInt("code") <= 200);
        smsResponse.setData(jsonObject);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }

}
