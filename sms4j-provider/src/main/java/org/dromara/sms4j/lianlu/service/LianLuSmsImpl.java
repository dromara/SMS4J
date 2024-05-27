package org.dromara.sms4j.lianlu.service;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.lianlu.config.LianLuConfig;
import org.dromara.sms4j.lianlu.req.LianLuRequest;
import org.dromara.sms4j.lianlu.utils.LianLuUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.Executor;

@Slf4j
public class LianLuSmsImpl extends AbstractSmsBlend<LianLuConfig> {
    private int retry = 0;
    private static final String NORMAL_MSG = "1";
    private static final String TEMPLATE_MSG = "3";

    public LianLuSmsImpl(LianLuConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public LianLuSmsImpl(LianLuConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.LIANLU;
    }

    /**
     * <p>说明：发送固定消息模板短信
     * <p>此方法将使用配置文件中预设的短信模板进行短信发送
     * <p>该方法指定的模板变量只能存在一个（配置文件中）
     *
     * @param phone         接收短信的手机号
     * @param templateParam 模板变量
     * @return
     */
    @Override
    public SmsResponse sendMessage(String phone, String templateParam) {
        return this.massTexting(Collections.singletonList(phone), templateParam);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<>();
        }
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    /**
     * 模板信息
     *
     * @param phone
     * @param templateId 模板id
     * @param messages   key无实际意义，value为模板变量值
     * @return
     */
    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return this.massTexting(Collections.singletonList(phone), templateId, messages);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateParam) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put("", templateParam);
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<>();
        }
        LianLuRequest req = getRequest();
        req.setType(TEMPLATE_MSG)
                .setPhoneNumberSet(phones)
                .setTemplateId(templateId)
                .setTemplateParamSet(messages.values());
        return getSmsResponse(req);
    }

    private LianLuRequest getRequest() {
        return getConfig().toLianLuRequest();
    }

    private String getRequestUrl(String type) {
        String url;
        switch (type) {
            case NORMAL_MSG:
                url = "/normal/send";
                break;
            case TEMPLATE_MSG:
                url = "/template/send";
                break;
            default:
                throw new IllegalArgumentException("暂不支持的短信类型:" + type);
        }
        return getConfig().getRequestUrl() + url;
    }

    /**
     * 不使用模板，直接发送短信
     *
     * @param phone
     * @param message 短信内容
     * @return
     */
    public SmsResponse sendNormalMessage(String phone, String message) {
        return sendNormalMessage(Collections.singletonList(phone), message);
    }

    /**
     * 不使用模板，直接发送短信
     *
     * @param phone
     * @param message  短信内容
     * @param signName 短信签名
     * @return
     */
    public SmsResponse sendNormalMessage(String phone, String message, String signName) {
        return sendNormalMessage(Collections.singletonList(phone), message, signName);
    }

    /**
     * 不使用模板，群发短信
     *
     * @param phones
     * @param message 短信内容
     * @return
     */
    public SmsResponse sendNormalMessage(List<String> phones, String message) {
        return sendNormalMessage(phones, message, getConfig().getSignature());
    }

    /**
     * 不使用模板，群发短信
     *
     * @param phones
     * @param message  短信内容
     * @param signName 短信签名
     * @return
     */
    public SmsResponse sendNormalMessage(List<String> phones, String message, String signName) {
        LianLuRequest req = getRequest();
        req.setType(NORMAL_MSG)
                .setPhoneNumberSet(phones)
                .setSessionContext(message)
                .setSignName(signName);
        return getSmsResponse(req);
    }

    private SmsResponse getSmsResponse(LianLuRequest req) {
        if (req.getPhoneNumberSet().size() > 10000) {
            throw new SmsBlendException("联麓短信:手机号数量最多为10000！");
        }

        req.setTimeStamp(System.currentTimeMillis());
        TreeMap<String, Object> requestBody = req.toMap();
        requestBody.put("Signature", LianLuUtils.generateSignature(requestBody, getConfig().getAppKey(), req.getSignType()));
        String reqUrl = getRequestUrl(req.getType());
        log.debug("requestBody:{}", requestBody);

        try {
            Map<String, String> headers = new HashMap<>(2);
            headers.put("Content-Type", Constant.APPLICATION_JSON_UTF8);
            headers.put("Accept", Constant.ACCEPT);
            SmsResponse smsResponse = this.getResponse(this.http.postJson(reqUrl, headers, requestBody));
            if (!smsResponse.isSuccess() && this.retry != this.getConfig().getMaxRetries()) {
                return this.requestRetry(req);
            } else {
                this.retry = 0;
                return smsResponse;
            }
        } catch (SmsBlendException e) {
            return this.requestRetry(req);
        }
    }

    private SmsResponse requestRetry(LianLuRequest req) {
        this.http.safeSleep(this.getConfig().getRetryInterval());
        ++this.retry;
        log.warn("短信第{}次重新发送, 请求参数:{}", this.retry, req);
        return this.getSmsResponse(req);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        return SmsRespUtils.resp(resJson, "00".equals(resJson.getStr("status")), getConfigId());
    }
}
