package org.dromara.sms4j.chuanglan.service;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.chuanglan.config.ChuangLanConfig;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.Collection;
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

    public ChuangLanSmsImpl(ChuangLanConfig chuangLanConfig, Executor pool, DelayedTime delayed) {
        super(chuangLanConfig, pool, delayed);
    }

    public ChuangLanSmsImpl(ChuangLanConfig chuangLanConfig) {
        super(chuangLanConfig);
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
        String reqUrl = getConfig().getBaseUrl() + getConfig().getMsgUrl();
        Collection<String> values = messages.values();
        String message = String.join(",", values);
        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", getConfig().getAccessKeyId());
        params.put("password", getConfig().getAccessKeySecret());
        params.put("msg", templateId);
        params.put("params", phone + "," + message);

        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json");

        return resProcessing(reqUrl, headers, params);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return massTexting(phones, getConfig().getTemplateId(), SmsUtils.buildMessageByAmpersand(message));
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        String reqUrl = getConfig().getBaseUrl() + getConfig().getMsgUrl();

        Collection<String> values = messages.values();
        String message = String.join(",", values);

        StringBuilder param = new StringBuilder();
        phones.forEach(phone -> param.append(phone).append(",").append(message).append(";"));

        LinkedHashMap<String, Object> params = new LinkedHashMap<>();
        params.put("account", getConfig().getAccessKeyId());
        params.put("password", getConfig().getAccessKeySecret());
        params.put("msg", templateId);
        params.put("params", param.toString());

        LinkedHashMap<String, String> headers = new LinkedHashMap<>();
        headers.put("Content-Type", "application/json");

        return resProcessing(reqUrl, headers, params);
    }

    private SmsResponse resProcessing(String reqUrl, LinkedHashMap<String, String> headers, LinkedHashMap<String, Object> params) {
        JSONObject jsonObject = http.postJson(reqUrl, headers, params);
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(jsonObject.containsKey("code") && "0".equals(jsonObject.getStr("code")));
        smsResponse.setData(jsonObject);
        smsResponse.setConfigId(getConfigId());
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {" + retry + "} 次重新发送");
        return resProcessing(reqUrl, headers, params);
    }

}