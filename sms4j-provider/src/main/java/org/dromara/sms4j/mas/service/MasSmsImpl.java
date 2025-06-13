package org.dromara.sms4j.mas.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;

import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.mas.config.MasConfig;
import org.dromara.sms4j.mas.utils.MasUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>类名: MasSmsImpl
 * <p>说明：中国移动 云MAS短信实现
 *
 * @author :bleachtred
 * 2024/4/22  13:40
 **/
@Slf4j
public class MasSmsImpl extends AbstractSmsBlend<MasConfig> {

    private int retry = 0;

    public MasSmsImpl(MasConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public MasSmsImpl(MasConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.MAS;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        return getSmsResponse(phone, message, getConfig().getTemplateId());
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (CollUtil.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }

        return getSmsResponse(phone, JSONUtil.toJsonStr(messages.values()), getConfig().getTemplateId());
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        if (CollUtil.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        String messageStr = JSONUtil.toJsonStr(messages.values());
        return getSmsResponse(phone, messageStr, templateId);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return getSmsResponse(SmsUtils.addCodePrefixIfNot(phones, false), message, getConfig().getTemplateId());
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (CollUtil.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        String messageStr = JSONUtil.toJsonStr(messages.values());
        return getSmsResponse(SmsUtils.addCodePrefixIfNot(phones, false), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
        String requestUrl;
        String base64Code;
        try {
            MasConfig config = getConfig();
            requestUrl = config.getRequestUrl() + config.getAction();
            base64Code = MasUtils.base64Code(getConfig(), phone, message, templateId);
        } catch (Exception e) {
            log.error("mas 10086 send message error", e);
            throw new SmsBlendException(e.getMessage());
        }
        log.debug("requestUrl {}", requestUrl);
        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postJson(requestUrl, null, base64Code));
        } catch (SmsBlendException e) {
            smsResponse = errorResp(e.message);
        }
        if (smsResponse.isSuccess() || retry >= getConfig().getMaxRetries()) {
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
        return SmsRespUtils.resp(resJson, "success".equals(resJson.getStr("rspcod")) && resJson.getBool("success"), getConfigId());
    }

}