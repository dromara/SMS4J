package org.dromara.sms4j.montnets.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.montnets.config.MontnetsConfig;
import org.dromara.sms4j.montnets.utils.MontnetsUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * <p>类名: MontnetsSmsImpl
 * <p>说明：  梦网短信实现
 *
 * @author :SU
 * 2025/4/23  10:32
 **/
@Slf4j
public class MontnetsSmsImpl extends AbstractSmsBlend<MontnetsConfig> {

    private int retry = 0;

    /**
     * MontnetsSmsImpl
     * <p>构造器，用于构造短信实现模块
     */
    public MontnetsSmsImpl(MontnetsConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    /**
     * MontnetsSmsImpl
     * <p>构造器，用于构造短信实现模块
     */
    public MontnetsSmsImpl(MontnetsConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.MONTNETS;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(getConfig().getTemplateParam(), message);
        return sendMessage(phone, this.getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)) {
            messages = new LinkedHashMap<>();
        }
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)) {
            messages = new LinkedHashMap<>();
        }
        String messageStr = formatMessage(messages);
        return getSmsResponse(phone, messageStr, templateId);
    }


    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(getConfig().getTemplateParam(), message);
        return massTexting(phones, getConfig().getTemplateId(), map);
    }


    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)) {
            messages = new LinkedHashMap<>();
        }
        String messageStr = formatMessage(messages);
        return getSmsResponse(SmsUtils.addCodePrefixIfNot(phones), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
        String requestUrl;
        String paramStr;
        try {
            requestUrl = MontnetsUtils.generateSendSmsRequestUrl(this.getConfig());
            paramStr = MontnetsUtils.generateParamBody(this.getConfig(), phone, message, templateId);
        } catch (Exception e) {
            log.error("montnets send message error", e);
            throw new SmsBlendException(e.getMessage());
        }

        log.debug("requestUrl {}", requestUrl);
        Map<String, String> headers = MapUtil.newHashMap(1, true);
        headers.put(Constant.CONTENT_TYPE, Constant.APPLICATION_JSON_UTF8);

        SmsResponse smsResponse;
        try {
            smsResponse = this.getResponse(this.http.postJson(requestUrl, headers, paramStr));
        } catch (SmsBlendException e) {
            smsResponse = this.errorResp(e.message);
        }

        if (!smsResponse.isSuccess() && this.retry != this.getConfig().getMaxRetries()) {
            return this.requestRetry(phone, message, templateId);
        } else {
            this.retry = 0;
            return smsResponse;
        }
    }

    private SmsResponse requestRetry(String phone, String message, String templateId) {
        this.http.safeSleep(this.getConfig().getRetryInterval());
        ++this.retry;
        log.warn("短信第 {} 次重新发送", this.retry);
        return this.getSmsResponse(phone, message, templateId);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        return SmsRespUtils.resp(URLDecoder.decode(resJson.getStr("desc"), StandardCharsets.UTF_8), "0".equals(resJson.getStr("result")), getConfigId());
    }

    /**
     * <p>说明：格式化消息
     *
     * @param messages 模板参数key-value集合
     * @return 格式化后的消息 key1=urlEncode(value1)&key2=urlEncode(value2)
     */
    private String formatMessage(LinkedHashMap<String, String> messages) {
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            joiner.add(entry.getKey() + "=" + URLEncodeUtil.encode(entry.getValue(), StandardCharsets.UTF_8));
        }
        return URLEncodeUtil.encode(joiner.toString());
    }
}
