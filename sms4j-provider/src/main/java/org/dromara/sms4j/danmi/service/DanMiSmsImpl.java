package org.dromara.sms4j.danmi.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.danmi.config.DanMiConfig;
import org.dromara.sms4j.danmi.utils.DanMiUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * <p>类名: DanMiSmsImpl
 *
 * @author :bleachtred
 * 2024/6/23  17:06
 **/
@Slf4j
public class DanMiSmsImpl extends AbstractSmsBlend<DanMiConfig> {

    private int retry = 0;

    public DanMiSmsImpl(DanMiConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public DanMiSmsImpl(DanMiConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.DAN_MI;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        if (StrUtil.isBlank(phone)){
            log.error("手机号不能为空");
            throw new SmsBlendException("手机号不能为空");
        }
        List<String> phones = phone.contains(StrUtil.COMMA) ? SmsUtils.splitTrimComma(phone) : Collections.singletonList(phone);
        return massTexting(phones, message);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        throw new SmsBlendException("不支持此方法");
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        throw new SmsBlendException("不支持此方法");
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return getSmsResponse(phones, message, getConfig().getTemplateId());
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        throw new SmsBlendException("不支持此方法");
    }

    /**
     * 短信余额查询
     * 请设置action为 distributor/user/query
     *
     * @return SmsResponse
     */
    public SmsResponse queryBalance() {
        return getSmsResponse(null, null, null);
    }

    /**
     * 语音验证码发送
     * 请设置action为 voice/voiceCode
     *
     * @param called 被叫号码
     * @param verifyCode 验证码内容(1-8位数字)
     * @return SmsResponse
     */
    public SmsResponse voiceCode(String called, String verifyCode) {
        return getSmsResponse(Collections.singletonList(called), verifyCode, null);
    }

    /**
     * 语音通知文件发送
     * 请设置action为 voice/voiceNotify
     *
     * @param called 被叫号码
     * @param notifyFileId 语音文件ID
     * @return SmsResponse
     */
    public SmsResponse voiceNotify(String called, String notifyFileId) {
        return getSmsResponse(Collections.singletonList(called), notifyFileId, null);
    }

    /**
     * 语音模板通知发送
     * 请设置action为 voice/voiceTemplate
     *
     * @param called 被叫号码
     * @param templateId 文字模板Id(用户中心创建后产生)
     * @param param 模板变量替换的参数(多个变量按英文逗号分开)
     * @return SmsResponse
     */
    public SmsResponse voiceTemplate(String called, String templateId, String param) {
        return getSmsResponse(Collections.singletonList(called), param, templateId);
    }

    private SmsResponse getSmsResponse(List<String> phones, String message, String templateId) {
        DanMiConfig config = getConfig();
        SmsResponse smsResponse;
        try {
            String url = config.getHost() + config.getAction();
            smsResponse = getResponse(http.postJson(url,
                    DanMiUtils.buildHeaders(),
                    DanMiUtils.buildBody(config, phones, message, templateId)));
        } catch (SmsBlendException e) {
            smsResponse = errorResp(e.message);
        }
        if (smsResponse.isSuccess() || retry == config.getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phones, message, templateId);
    }

    private SmsResponse requestRetry(List<String> phones, String message, String templateId) {
        http.safeSleep(getConfig().getRetryInterval());
        retry ++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponse(phones, message, templateId);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        return SmsRespUtils.resp(resJson, "00000".equals(resJson.getStr("respCode")), getConfigId());
    }

}