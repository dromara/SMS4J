package org.dromara.sms4j.ctyun.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.ctyun.utils.CtyunUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * <p>类名: CtyunSmsImpl
 * <p>说明： 天翼云短信实现
 *
 * @author :bleachhtred
 * 2023/5/12  15:06
 **/
@Slf4j
public class CtyunSmsImpl extends AbstractSmsBlend {

    private final CtyunConfig ctyunConfig;

    public CtyunSmsImpl(CtyunConfig ctyunConfig, Executor pool, DelayedTime delayedTime) {
        super(pool, delayedTime);
        this.ctyunConfig = ctyunConfig;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ctyunConfig.getTemplateName(), message);
        return sendMessage(phone, ctyunConfig.getTemplateId(), map);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSONUtil.toJsonStr(messages);
        return getSmsResponse(phone, messageStr, templateId);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(ctyunConfig.getTemplateName(), message);
        return massTexting(phones, ctyunConfig.getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSONUtil.toJsonStr(messages);
        return getSmsResponse(SmsUtil.arrayToString(phones), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
        String requestUrl;
        String paramStr;
        try {
            requestUrl = ctyunConfig.getRequestUrl();
            paramStr = CtyunUtils.generateParamJsonStr(ctyunConfig, phone, message, templateId);
        } catch (Exception e) {
            log.error("ctyun send message error", e);
            throw new SmsBlendException(e.getMessage());
        }
        return this.getResponse(http.postJson(requestUrl,
                CtyunUtils.signHeader(paramStr, ctyunConfig.getAccessKeyId(), ctyunConfig.getAccessKeySecret()),
                paramStr));
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess("OK".equals(resJson.getStr("code")));
        smsResponse.setData(resJson);
        return smsResponse;
    }

}