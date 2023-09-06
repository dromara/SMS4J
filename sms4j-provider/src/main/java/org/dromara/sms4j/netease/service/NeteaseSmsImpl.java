package org.dromara.sms4j.netease.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.netease.utils.NeteaseUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;

/**
 * Created with IntelliJ IDEA.
 *
 * @author adam
 * @create 2023/5/29 17:34
 */

@Slf4j
public class NeteaseSmsImpl extends AbstractSmsBlend<NeteaseConfig> {

    private int retry = 0;

    public NeteaseSmsImpl(NeteaseConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
    }

    public NeteaseSmsImpl(NeteaseConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.NETEASE;
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
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(phones);
        body.put("mobiles", jsonArray.toString());
        body.put("params", message);
        body.put("needUp", getConfig().getNeedUp());

        try {
            Map<String, String> headers = new LinkedHashMap<>(5);
            headers.put("Content-Type", Constant.FROM_URLENCODED);
            headers.put("AppKey", getConfig().getAccessKeyId());
            headers.put("Nonce", nonce);
            headers.put("CurTime", curTime);
            headers.put("CheckSum", checkSum);
            SmsResponse smsResponse = getResponse(http.postJson(requestUrl, headers, body));
            if(smsResponse.isSuccess() || retry == getConfig().getMaxRetries()){
                retry = 0;
                return smsResponse;
            }
            return requestRetry(requestUrl, phones, message, templateId);
        }catch (SmsBlendException e){
            return requestRetry(requestUrl, phones, message, templateId);
        }
    }

    private SmsResponse requestRetry(String requestUrl, List<String> phones, String message, String templateId) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {" + retry + "} 次重新发送");
        return getSmsResponse(requestUrl, phones, message, templateId);
    }

    private SmsResponse getResponse(JSONObject jsonObject) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(jsonObject.getInt("code") <= 200);
        smsResponse.setData(jsonObject);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }

}
