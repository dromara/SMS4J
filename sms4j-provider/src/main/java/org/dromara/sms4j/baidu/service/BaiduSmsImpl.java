package org.dromara.sms4j.baidu.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.baidu.config.BaiduConfig;
import org.dromara.sms4j.baidu.utils.BaiduUtils;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * <p>类名: BaiduSmsImpl
 * <p>说明：百度智能云 sms
 *
 * @author :bleachtred
 * 2024/4/25  13:40
 **/
@Slf4j
public class BaiduSmsImpl extends AbstractSmsBlend<BaiduConfig> {

    private int retry = 0;

    public BaiduSmsImpl(BaiduConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public BaiduSmsImpl(BaiduConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.BAIDU;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(getConfig().getTemplateName(), message);
        return sendMessage(phone, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (CollUtil.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        if (CollUtil.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        return getSmsResponse(phone, templateId, messages);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(getConfig().getTemplateName(), message);
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (CollUtil.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        return getSmsResponse(SmsUtils.addCodePrefixIfNot(phones), templateId, messages);
    }

    private SmsResponse getSmsResponse(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return getSmsResponseWithClientToken(phone, templateId, messages, null);
    }

    private void checkClientToken(String clientToken){
        if (StrUtil.isBlank(clientToken)){
            log.error("clientToken is required.");
            throw new SmsBlendException("clientToken is required.");
        }
    }

    public SmsResponse sendMessageWithClientToken(String phone, String message, String clientToken) {
        checkClientToken(clientToken);
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(getConfig().getTemplateName(), message);
        return sendMessageWithClientToken(phone, getConfig().getTemplateId(), map, clientToken);
    }

    public SmsResponse sendMessageWithClientToken(String phone, LinkedHashMap<String, String> messages, String clientToken) {
        checkClientToken(clientToken);
        if (CollUtil.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        return sendMessageWithClientToken(phone, getConfig().getTemplateId(), messages, clientToken);
    }

    public SmsResponse sendMessageWithClientToken(String phone, String templateId, LinkedHashMap<String, String> messages, String clientToken) {
        checkClientToken(clientToken);
        if (CollUtil.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        return getSmsResponseWithClientToken(phone, templateId, messages, clientToken);
    }

    public SmsResponse massTextingWithClientToken(List<String> phones, String message, String clientToken) {
        checkClientToken(clientToken);
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(getConfig().getTemplateName(), message);
        return massTextingWithClientToken(phones, getConfig().getTemplateId(), map, clientToken);
    }

    public SmsResponse massTextingWithClientToken(List<String> phones, String templateId, LinkedHashMap<String, String> messages, String clientToken) {
        checkClientToken(clientToken);
        if (CollUtil.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        return getSmsResponseWithClientToken(SmsUtils.addCodePrefixIfNot(phones), templateId, messages, clientToken);
    }

    private SmsResponse getSmsResponseWithClientToken(String phone, String templateId, LinkedHashMap<String, String> messages, String clientToken) {
        BaiduConfig config = getConfig();
        if (StrUtil.isBlank(config.getSignature())){
            log.error("signatureId is required.");
            throw new SmsBlendException("signatureId is required.");
        }
        if (StrUtil.isBlank(templateId)){
            log.error("template is required.");
            throw new SmsBlendException("template is required.");
        }
        if (StrUtil.isBlank(phone)){
            log.error("mobile is required.");
            throw new SmsBlendException("mobile is required.");
        }
        Map<String, String> headers;
        Map<String, Object> body;
        try {
            headers = BaiduUtils.buildHeaders(config, clientToken);
            body = BaiduUtils.buildBody(phone, templateId, config.getSignature(), messages, config.getCustom(), config.getUserExtId());
        } catch (Exception e) {
            log.error("baidu sms buildHeaders or buildBody error", e);
            throw new SmsBlendException(e.getMessage());
        }
        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postJson(config.getHost() + config.getAction(), headers, body));
        } catch (SmsBlendException e) {
            smsResponse = errorResp(e.message);
        }
        if (smsResponse.isSuccess() || retry == config.getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phone, templateId, messages, clientToken);
    }

    private SmsResponse requestRetry(String phone, String templateId, LinkedHashMap<String, String> messages, String clientToken) {
        http.safeSleep(getConfig().getRetryInterval());
        retry ++;
        log.warn("The SMS has been resent for the {}th time.", retry);
        return getSmsResponseWithClientToken(phone, templateId, messages, clientToken);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        return SmsRespUtils.resp(resJson, "1000".equals(resJson.getStr("code")), getConfigId());
    }

}