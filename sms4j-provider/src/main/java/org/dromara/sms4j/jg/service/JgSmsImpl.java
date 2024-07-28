package org.dromara.sms4j.jg.service;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.jg.config.JgConfig;
import org.dromara.sms4j.jg.util.JgUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * <p>类名: JgSmsImpl
 * <p>说明：极光 sms
 *
 * @author :SmartFire
 * 2024/3/15
 **/
@Slf4j
public class JgSmsImpl extends AbstractSmsBlend<JgConfig> {
    private int retry = 0;

    public JgSmsImpl(JgConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public JgSmsImpl(JgConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.JIGUANG;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        if (SmsUtils.isNotEmpty(getConfig().getTemplateName()) &&
                SmsUtils.isNotEmpty(message)){
            map.put(getConfig().getTemplateName(), message);
        }
        return sendMessage(phone, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (SmsUtils.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        if (SmsUtils.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        return getSmsResponse(phone, messages, templateId, null, null);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        if (SmsUtils.isNotEmpty(getConfig().getTemplateName()) &&
                SmsUtils.isNotEmpty(message)){
            map.put(getConfig().getTemplateName(), message);
        }
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (SmsUtils.isEmpty(messages)){
            messages = new LinkedHashMap<>();
        }
        return getSmsResponse(SmsUtils.addCodePrefixIfNot(phones), messages, templateId, null, null);
    }

    /**
     * 自定义方法
     * 发送语音验证码短信 请确保action配置为voice_codes
     * @param phone 手机号
     * @param code 语音验证码 可不填
     */
    public SmsResponse sendVoiceCode(String phone, String code){
        return getSmsResponse(phone, null, null, code, null);
    }

    /**
     * 自定义方法
     * 验证验证码是否有效 请确保action配置为voice_codes
     * @param msgId 为调用发送验证码 API 的返回值
     * @param code 验证码
     */
    public SmsResponse verifyCode(String code, String msgId){
        return getSmsResponse(null, null, null, code, msgId);
    }

    private SmsResponse getSmsResponse(String phone, LinkedHashMap<String, String> messages,
                                       String templateId, String code, String msgId) {
        SmsResponse smsResponse;
        JgConfig config = getConfig();
        String url = JgUtils.buildUrl(config.getRequestUrl(), config.getAction(), msgId);
        Map<String, String> headers = JgUtils.buildHeaders(config.getAccessKeyId(), config.getAccessKeySecret());
        Map<String, Object> body= JgUtils.buildBody(phone, messages, templateId, config, code);
        String jsonKey = JgUtils.buildJsonKey(config.getAction());
        try {
            smsResponse = getResponse(http.postJson(url, headers, body), jsonKey);
        } catch (SmsBlendException e) {
            smsResponse = errorResp(e.message);
        }

        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phone, messages, templateId, code, msgId);
    }

    private SmsResponse requestRetry(String phone, LinkedHashMap<String, String> messages,
                                     String templateId, String code, String msgId) {
        http.safeSleep(getConfig().getRetryInterval());
        retry ++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponse(phone, messages, templateId, code, msgId);
    }

    private SmsResponse getResponse(JSONObject resJson, String jsonKey) {
        return SmsRespUtils.resp(resJson, resJson.getObj(jsonKey) != null, getConfigId());
    }
}
