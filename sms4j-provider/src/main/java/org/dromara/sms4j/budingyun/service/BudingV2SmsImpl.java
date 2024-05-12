package org.dromara.sms4j.budingyun.service;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.budingyun.config.BudingV2Config;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * BudingV2SmsImpl 布丁云V2短信实现
 * @author NicholasLD
 * @createTime 2024/3/21 01:28
 */
@Slf4j
public class BudingV2SmsImpl extends AbstractSmsBlend<BudingV2Config> {

    /**
     * 重试次数
     */
    private int retry = 0;

    private static final String URL = "https://smsapi.idcbdy.com";

    protected BudingV2SmsImpl(BudingV2Config config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
    }

    public BudingV2SmsImpl(BudingV2Config config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.BUDING_V2;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        Map<String, Object> body = new HashMap<>();

        System.out.println(getConfig().getSignKey());
        System.out.println(getConfig().getSignature());

        if (getConfig().getSignKey() == null && getConfig().getSignature() == null) {
            throw new SmsBlendException("签名秘钥不能为空");
        }

        if (getConfig().getSignKey() == null) {
            body.put("sign", getConfig().getSignature());
        }

        body.put("key", getConfig().getAccessKeyId());
        body.put("to", phone);
        body.put("content", message);

        Map<String, String> headers = getHeaders();

        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postFrom(URL + "/Api/Sent", headers, body));
        } catch (SmsBlendException e) {
            smsResponse = SmsRespUtils.error(e.getMessage(), getConfigId());
        }
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phone, message);
    }

    private SmsResponse requestRetry(String phone, String message) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {" + retry + "} 次重新发送");
        return sendMessage(phone, message);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        if (resJson == null) {
            return SmsRespUtils.error(getConfigId());
        }
        return SmsRespUtils.resp(resJson, resJson.getBool("bool"), getConfigId());
    }

    /**
     * 发送多条短信
     * @param phone 手机号
     * @param messages 消息内容
     * @return 发送结果
     */
    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        int failed = 0;
        for (String message : messages.values()) {
            SmsResponse smsResponse = sendMessage(phone, message);
            if (!smsResponse.isSuccess()) {
                failed++;
            }
        }
        return SmsRespUtils.resp(failed == 0, getConfigId());
    }

    /**
     * 发送多条短信 (布丁云V2暂不支持模板短信)
     * @param phone 手机号
     * @param templateId 模板ID (布丁云V2暂不支持模板短信，此参数无效)
     * @param messages 模板参数
     * @return 发送结果
     */
    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return sendMessage(phone, messages);
    }

    /**
     * 群发短信
     * @param phones 手机号列表
     * @param message 消息内容
     * @return 发送结果
     */
    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        int failed = 0;
        for (String phone : phones) {
            SmsResponse smsResponse = sendMessage(phone, message);
            if (!smsResponse.isSuccess()) {
                failed++;
            }
        }
        return SmsRespUtils.resp(failed == 0, getConfigId());
    }

    /**
     * 群发短信 (布丁云V2暂不支持模板短信，此方法无效)
     * @param phones 手机号列表
     * @param templateId 模板ID (布丁云V2暂不支持模板短信，此参数无效)
     * @param messages 模板参数
     * @return 发送结果
     */
    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        throw new SmsBlendException("布丁云V2暂不支持多条短信发送");
    }

    private Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", Constant.APPLICATION_JSON_UTF8);
        headers.put("Content-Type", Constant.FROM_URLENCODED);
        return headers;
    }
}
