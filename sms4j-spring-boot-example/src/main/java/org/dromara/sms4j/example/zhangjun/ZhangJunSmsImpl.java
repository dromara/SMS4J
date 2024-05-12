package org.dromara.sms4j.example.zhangjun;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author :4n
 **/
@Slf4j
public class ZhangJunSmsImpl extends AbstractSmsBlend<ZhangJunConfig> {

    private int retry = 0;

    /**
     * ZhangJunSmsImpl
     * <p>构造器，用于构造短信实现模块
     *
     * @author :Wind
     */
    public ZhangJunSmsImpl(ZhangJunConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    /**
     * ZhangJunSmsImpl
     * <p>构造器，用于构造短信实现模块
     */
    public ZhangJunSmsImpl(ZhangJunConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return "zhangjun";
    }
    private LinkedHashMap<String, String> buildBody(String phone, String message){
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("appId", getConfig().getAppId());
        map.put("sid", getConfig().getSid());
        map.put("templateId", getConfig().getTemplateId());
        map.put("phone", phone);
        Map<String, Object> data = new HashMap<>();
        data.put("code", message);
        map.put("data", JSONUtil.toJsonStr(data));
        return map;
    }
    @Override
    public SmsResponse sendMessage(String phone, String message) {
        return sendMessage(phone, getConfig().getTemplateId(), buildBody(phone,message));
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSONUtil.toJsonStr(messages);
        return getSmsResponse(phone, messageStr, templateId);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
//        map.put(getConfig().getTemplateName(), message);
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSONUtil.toJsonStr(messages);
        return getSmsResponse(SmsUtils.arrayToString(phones), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postJson(getConfig().getUrl(), null, message));
        } catch (SmsBlendException e) {
            smsResponse = SmsRespUtils.error(e.getMessage(), getConfigId());
        }
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phone, message, templateId);
    }

    private SmsResponse requestRetry(String phone, String message, String templateId) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponse(phone, message, templateId);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        return SmsRespUtils.resp(resJson, "OK".equals(resJson.getStr("Code")), getConfigId());
    }

}