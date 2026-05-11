package org.dromara.sms4j.huyi.service;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.huyi.config.HuYiConfig;
import org.dromara.sms4j.huyi.util.HuYiUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 互亿无限短信实现
 *
 * @author 初心
 * 2025/7/5
 */
@Slf4j
public class HuYiSmsImpl extends AbstractSmsBlend<HuYiConfig> {

    private final ThreadLocal<Integer> retry = ThreadLocal.withInitial(() -> 0);

    protected HuYiSmsImpl(HuYiConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
    }

    public HuYiSmsImpl(HuYiConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.HUYI;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        return sendSingleMsg(phone, null, message);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        throw new SmsBlendException("不支持此方法");
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        StringJoiner stringJoiner = new StringJoiner("|");
        for (String value : messages.values()) {
            stringJoiner.add(value);
        }
        messages.forEach((key, value) -> stringJoiner.add(value));
        String sendMessages = String.valueOf(stringJoiner);
        return sendSingleMsg(phone, templateId, sendMessages);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return sendMassMsg(phones, null, message);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        StringJoiner stringJoiner = new StringJoiner("|");
        for (String value : messages.values()) {
            stringJoiner.add(value);
        }
        messages.forEach((key, value) -> stringJoiner.add(value));
        String sendMessages = String.valueOf(stringJoiner);
        return sendMassMsg(phones, templateId, sendMessages);
    }

    /**
     * 发送单条短信
     *
     * @param phone      手机号
     * @param templateId 模板 ID
     * @param message    消息
     * @return 短信发送响应
     */
    private SmsResponse sendSingleMsg(String phone, String templateId, String message) {
        Map<String, String> params = HuYiUtils.getParams(getConfig());
        params.put("content", message);
        params.put("mobile", phone);
        params.put("templateid", templateId);
        String url = getConfig().getBaseUrl() + getConfig().getSingleMsgUrl();
        return handleRes(url, params);
    }

    /**
     * 群发短信
     *
     * @param phones     手机号列表
     * @param templateId 模板 ID
     * @param message    消息
     * @return 短信发送响应
     * @Description: 根据互亿无线短信群发接口的接口文档，带参数变量的需要放在 mobile 中，而不是放在 content 中
     */
    private SmsResponse sendMassMsg(List<String> phones, String templateId, String message) {
        String sendPhones = phones.stream().map(phone -> {
            if (message != null) {
                return phone + "|" + message;
            } else {
                return phone;
            }
        }).collect(Collectors.joining(","));
        Map<String, String> params = HuYiUtils.getParams(getConfig());
        params.put("content", message);
        params.put("mobile", sendPhones);
        params.put("templateid", templateId);
        String url = getConfig().getBaseUrl() + getConfig().getMassMsgUrl();
        return handleRes(url, params);
    }

    /**
     * 处理响应
     *
     * @param url    请求地址
     * @param params 参数
     * @return 短信发送响应
     */
    private SmsResponse handleRes(String url, Map<String, String> params) {
        if (getConfig().getEnableMd5()) {
            long unix = System.currentTimeMillis() / 1000;
            params.put("time", String.valueOf(unix));
            params.replace("password", SecureUtil.md5(params.get("account") + params.get("password") + params.get("mobile") + params.get("content") + unix));
        }
        JSONObject jsonObject;
        SmsResponse smsResponse;
        try {
            jsonObject = http.getUrl(url + "&" + HttpUtil.toParams(params));
            smsResponse = SmsRespUtils.resp(jsonObject, "2".equals(jsonObject.getStr("code")), getConfigId());
        } catch (SmsBlendException e) {
            smsResponse = errorResp(e.message);
        }
        if (smsResponse.isSuccess() || retry.get() >= getConfig().getMaxRetries()) {
            retry.remove();
            return smsResponse;
        }
        return requestRetry(url, params);
    }

    /**
     * 请求重试
     *
     * @param url    发送地址
     * @param params 参数
     * @return 短信发送响应
     */
    private SmsResponse requestRetry(String url, Map<String, String> params) {
        http.safeSleep(getConfig().getRetryInterval());
        retry.set(retry.get() + 1);
        log.warn("短信第 {} 次重新发送", retry.get());
        return handleRes(url, params);
    }
}
