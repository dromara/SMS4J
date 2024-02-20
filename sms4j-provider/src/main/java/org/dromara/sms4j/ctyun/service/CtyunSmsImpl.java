package org.dromara.sms4j.ctyun.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.ctyun.utils.CtyunUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * <p>类名: CtyunSmsImpl
 * <p>说明： 天翼云短信实现
 *
 * @author :bleachhtred
 * 2023/5/12  15:06
 **/
@Slf4j
public class CtyunSmsImpl extends AbstractSmsBlend<CtyunConfig> {

    private int retry = 0;

    public CtyunSmsImpl(CtyunConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public CtyunSmsImpl(CtyunConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.CTYUN;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(getConfig().getTemplateName(), message);
        return sendMessage(phone, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<>();
        }
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<String, String>();
        }
        String messageStr = JSONUtil.toJsonStr(messages);
        return getSmsResponse(phone, messageStr, templateId);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(getConfig().getTemplateName(), message);
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<String, String>();
        }
        String messageStr = JSONUtil.toJsonStr(messages);
        return getSmsResponse(SmsUtils.arrayToString(phones), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
        String requestUrl;
        String paramStr;
        try {
            requestUrl = getConfig().getRequestUrl();
            paramStr = CtyunUtils.generateParamJsonStr(getConfig(), phone, message, templateId);
        } catch (Exception e) {
            log.error("ctyun send message error", e);
            throw new SmsBlendException(e.getMessage());
        }
        log.debug("requestUrl {}", requestUrl);
        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postJson(requestUrl,
                    CtyunUtils.signHeader(paramStr, getConfig().getAccessKeyId(), getConfig().getAccessKeySecret()),
                    paramStr));
        } catch (SmsBlendException e) {
            smsResponse = new SmsResponse();
            smsResponse.setSuccess(false);
            smsResponse.setData(e.getMessage());
        }
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phone, message, templateId);
    }

    private SmsResponse requestRetry(String phone, String message, String templateId) {
        http.safeSleep(getConfig().getRetryInterval());
        retry ++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponse(phone, message, templateId);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess("OK".equals(resJson.getStr("code")));
        smsResponse.setData(resJson);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }

}