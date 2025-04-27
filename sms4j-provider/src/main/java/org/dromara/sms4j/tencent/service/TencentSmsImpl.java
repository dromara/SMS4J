package org.dromara.sms4j.tencent.service;

import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.tencent.utils.TencentUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author wind
 */
@Slf4j
public class TencentSmsImpl extends AbstractSmsBlend<TencentConfig> {

    private int retry = 0;

    public TencentSmsImpl(TencentConfig tencentSmsConfig, Executor pool, DelayedTime delayed) {
        super(tencentSmsConfig, pool, delayed);
    }

    public TencentSmsImpl(TencentConfig tencentSmsConfig) {
        super(tencentSmsConfig);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.TENCENT;
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
        // 如果包含 - 的话，则认为是国际短信 ，不进行+86拼接
        if (phone.contains("-")) {
            String result = phone.replace("-", "");
            return getSmsResponse(new String[]{result}, SmsUtils.toArray(messages), templateId);
        } else {
            return getSmsResponse(new String[]{StrUtil.addPrefixIfNot(phone, "+86")}, SmsUtils.toArray(messages), templateId);
        }
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return massTexting(phones, getConfig().getTemplateId(), SmsUtils.buildMessageByAmpersand(message));
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        List<String> list = new ArrayList<>();
        for (String phone : phones) {
            if (phone.contains("-")) {
                String result = phone.replace("-", "");
                list.add(result);
            } else {
                list.add(StrUtil.addPrefixIfNot(phone, "+86"));
            }
        }
        return getSmsResponse(list.toArray(new String[0]), SmsUtils.toArray(messages), templateId);
    }

    private SmsResponse getSmsResponse(String[] phones, String[] messages, String templateId) {
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
        String signature;
        try {
            signature = TencentUtils.generateSignature(this.getConfig(), templateId, messages, phones, timestamp);
        } catch (Exception e) {
            log.error("tencent send message error", e);
            throw new SmsBlendException(e.getMessage());
        }
        Map<String, String> headsMap = TencentUtils.generateHeadsMap(signature, timestamp, getConfig().getAction(),
                getConfig().getVersion(), getConfig().getTerritory(), getConfig().getRequestUrl());
        Map<String, Object> requestBody = TencentUtils.generateRequestBody(phones, getConfig().getSdkAppId(),
                getConfig().getSignature(), templateId, messages);
        String url = Constant.HTTPS_PREFIX + getConfig().getRequestUrl();

        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postJson(url, headsMap, requestBody));
        } catch (SmsBlendException e) {
            smsResponse = errorResp(e.message);
        }
        if (smsResponse.isSuccess() || retry >= getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phones, messages, templateId);
    }

    private SmsResponse requestRetry(String[] phones, String[] messages, String templateId) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponse(phones, messages, templateId);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        JSONObject response = resJson.getJSONObject("Response");
        // 根据 Error 判断是否配置错误
        boolean success = StrUtil.isBlank(response.getStr("Error"));
        // 根据 SendStatusSet 判断是否不为Ok
        JSONArray sendStatusSet = response.getJSONArray("SendStatusSet");
        if (sendStatusSet != null) {
            for (Object obj : sendStatusSet) {
                JSONObject jsonObject = (JSONObject) obj;
                String code = jsonObject.getStr("Code");
                if (!"Ok".equals(code)) {
                    success = false;
                    break;
                }
            }
        }
        return SmsRespUtils.resp(resJson, success, getConfigId());
    }

}