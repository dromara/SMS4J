package org.dromara.sms4j.yixintong.service;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import org.dromara.sms4j.yixintong.config.YiXintongConfig;
import org.dromara.sms4j.yixintong.utils.YiXintongUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * <p>类名: YiXintongSmsImpl
 * <p>说明：联通一信通 sms
 *
 * @author moat
 * @create 2024-07-30 16:59
 */
@Slf4j
public class YiXintongSmsImpl extends AbstractSmsBlend<YiXintongConfig> {

    private int retry = 0;

    public YiXintongSmsImpl(YiXintongConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public YiXintongSmsImpl(YiXintongConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.YIXINTONG;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        return getSmsResponse(phone, message, getConfig().getTemplateId());
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return getSmsResponse(SmsUtils.joinComma(phones), message, getConfig().getTemplateId());
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
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        throw new SmsBlendException("不支持此方法");
    }


    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
        final YiXintongConfig config = getConfig();
        if (StrUtil.isBlank(phone)){
            log.error("phone is required.");
            throw new SmsBlendException("phone is required.");
        }
        if (StrUtil.isBlank(message)){
            log.error("message is required.");
            throw new SmsBlendException("message is required.");
        }
        // 生成20位流水号
        String serialNumber = SmsUtils.getRandomInt(20);

        Map<String, Object> forms = new HashMap<>();
        forms.put("SpCode", config.getSpCode());
        forms.put("LoginName", config.getAccessKeyId());
        forms.put("Password", config.getAccessKeySecret());
        forms.put("MessageContent", message);
        forms.put("UserNumber", phone);
        forms.put("templateId", templateId);
        forms.put("SerialNumber", serialNumber);
        forms.put("ScheduleTime", ""); // 立即发送
        forms.put("f", config.getF());
        forms.put("signCode", config.getSignCode());

        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(YiXintongUtils.postForm(config.getRequestUrl(), forms));
        } catch (SmsBlendException e) {
            smsResponse = errorResp(e.message);
        }
        if (smsResponse.isSuccess() || retry == config.getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phone, message, templateId);
    }



    private SmsResponse requestRetry(String phone, String message, String templateId) {
        http.safeSleep(getConfig().getRetryInterval());
        retry ++;
        log.warn("The SMS has been resent for the {}th time.", retry);
        return getSmsResponse(phone, message, templateId);
    }


    /**
     *  构造统一短信返回信息
     * @param body 原始响应信息
     * @return 短信返回信息
     */
    private SmsResponse getResponse(String body) {
        return SmsRespUtils.resp(body, StrUtil.contains(body, "result=0&"), getConfigId());
    }

}
