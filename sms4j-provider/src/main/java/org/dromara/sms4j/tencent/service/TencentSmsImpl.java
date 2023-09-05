package org.dromara.sms4j.tencent.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.jdcloud.sdk.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.tencent.utils.TencentUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author wind
 */
@Slf4j
public class TencentSmsImpl extends AbstractSmsBlend<TencentConfig> {

    public static final String SUPPLIER = "tencent";
    private int retry = 0;

    public TencentSmsImpl(TencentConfig tencentSmsConfig, Executor pool, DelayedTime delayed) {
        super(tencentSmsConfig, pool, delayed);
    }

    public TencentSmsImpl(TencentConfig tencentSmsConfig) {
        super(tencentSmsConfig);
    }

    @Override
    public String getSupplier() {
        return SUPPLIER;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        String[] split = message.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < split.length; i++) {
            map.put(String.valueOf(i), split[i]);
        }
        return sendMessage(phone, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        String[] s = new String[list.size()];
        return getSmsResponse(new String[]{StrUtil.addPrefixIfNot(phone, "+86")}, list.toArray(s), templateId);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        String[] split = message.split("&");
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        for (int i = 0; i < split.length; i++) {
            map.put(String.valueOf(i), split[i]);
        }
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        String[] s = new String[list.size()];
        return getSmsResponse(SmsUtil.listToArray(phones), list.toArray(s), templateId);
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

        try {
            SmsResponse smsResponse = getResponse(http.postJson(url, headsMap, requestBody));
            if(smsResponse.isSuccess() || retry == getConfig().getMaxRetries()){
                retry = 0;
                return smsResponse;
            }
            return requestRetry(phones, messages, templateId);
        }catch (SmsBlendException e){
            return requestRetry(phones, messages, templateId);
        }
    }

    private SmsResponse requestRetry(String[] phones, String[] messages, String templateId) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {" + retry + "} 次重新发送");
        return getSmsResponse(phones, messages, templateId);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        JSONObject response = resJson.getJSONObject("Response");
        String error = response.getStr("Error");
        smsResponse.setSuccess(StringUtils.isBlank(error));
        smsResponse.setData(resJson);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }
}