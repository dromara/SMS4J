package org.dromara.sms4j.chuanglan.service;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.chuanglan.config.ChuangLanConfig;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * @author YYM
 * @Date: 2024/2/1 9:04 27
 * @描述: ChuangLanSmsImpl
 **/
@Slf4j
public class ChuangLanSmsImpl extends AbstractSmsBlend<ChuangLanConfig> {

    private int retry = 0;

    public ChuangLanSmsImpl(ChuangLanConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
    }

    public ChuangLanSmsImpl(ChuangLanConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.CHUANGLAN;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        return sendMessage(phone, getConfig().getTemplateId(), SmsUtils.buildMessageByAmpersand(message));
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        if (SmsUtils.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        String message = String.join(",", messages.values());
        ChuangLanConfig config = getConfig();
        LinkedHashMap<String, Object> body = buildBody(config.getAccessKeyId(), config.getAccessKeySecret(), templateId);
        body.put("params", phone + "," + message);
        return getSmsResponse(body);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return massTexting(phones, getConfig().getTemplateId(), SmsUtils.buildMessageByAmpersand(message));
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (SmsUtils.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        String message = String.join(",", messages.values());
        StringBuilder param = new StringBuilder();
        phones.forEach(phone -> param.append(phone).append(",").append(message).append(";"));
        ChuangLanConfig config = getConfig();
        LinkedHashMap<String, Object> params = buildBody(config.getAccessKeyId(), config.getAccessKeySecret(), templateId);
        params.put("params", param.toString());
        return getSmsResponse(params);
    }

    private static String buildUrl(String baseUrl, String msgUrl){
        return baseUrl + msgUrl;
    }

    private static LinkedHashMap<String, String> buildHeaders(){
        LinkedHashMap<String, String> headers = new LinkedHashMap<>(1);
        headers.put(Constant.CONTENT_TYPE, Constant.APPLICATION_JSON);
        return headers;
    }

    private static LinkedHashMap<String, Object> buildBody(String accessKeyId, String accessKeySecret, String templateId){
        LinkedHashMap<String, Object> body = new LinkedHashMap<>(3);
        body.put("account", accessKeyId);
        body.put("password", accessKeySecret);
        body.put("msg", templateId);
        return body;
    }

    private SmsResponse getSmsResponse(LinkedHashMap<String, Object> body) {
        ChuangLanConfig config = getConfig();
        SmsResponse smsResponse;
        String reqUrl = buildUrl(config.getBaseUrl(), config.getMsgUrl());
        try {
            smsResponse = getResponse(http.postJson(reqUrl, buildHeaders(), body));
        }catch (SmsBlendException e) {
            smsResponse = errorResp(e.message);
        }
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {" + retry + "} 次重新发送");
        return requestRetry(body);
    }

    private SmsResponse requestRetry(LinkedHashMap<String, Object> body) {
        http.safeSleep(getConfig().getRetryInterval());
        retry ++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponse(body);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        return SmsRespUtils.resp(resJson, resJson.containsKey("code") && "0".equals(resJson.getStr("code")), getConfigId());
    }

}