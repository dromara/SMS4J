package org.dromara.sms4j.luosimao.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.luosimao.config.LuoSiMaoConfig;
import org.dromara.sms4j.luosimao.utils.LuoSiMaoUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * <p>类名: LuoSiMaoSmsImpl
 * <p>说明： 螺丝帽短信差异配置
 *
 * @author :bleachtred
 * 2024/6/21  23:59
 **/
@Slf4j
public class LuoSiMaoSmsImpl extends AbstractSmsBlend<LuoSiMaoConfig> {

    private int retry = 0;

    public LuoSiMaoSmsImpl(LuoSiMaoConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public LuoSiMaoSmsImpl(LuoSiMaoConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.LUO_SI_MAO;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        return getSmsResponse(Collections.singletonList(phone), message, null, false, false);
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
        return getSmsResponse(phones, message, null, false, false);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        throw new SmsBlendException("不支持此方法");
    }

    /**
     * 定时批量发送
     * @param phones 手机号
     * @param message 信息
     * @param date 时间
     * @return SmsResponse
     */
    public SmsResponse massTextingOnTime(List<String> phones, String message, Date date) {
        return getSmsResponse(phones, message, date, true, false);
    }

    /**
     * 查询账户余额 请将接口设置为 status.json
     *
     * @return SmsResponse
     */
    public SmsResponse queryAccountBalance() {
        return getSmsResponse(null, null, null, false, true);
    }

    private SmsResponse getSmsResponse(List<String> phones, String message, Date date, boolean batch, boolean status) {
        LuoSiMaoConfig config = getConfig();
        SmsResponse smsResponse;
        try {
            String url = config.getHost() + config.getAction();
            LinkedHashMap<String, Object> body;
            if (status){
                if ("status.json".equals(config.getAction())){
                    log.error("please set the request interface method to status.json");
                    throw new SmsBlendException("please set the request interface method to status.json");
                }
                smsResponse = getResponse(http.getBasic(url, "api", "key-" + config.getAccessKeyId()));
            } else {
                if (CollUtil.isEmpty(phones)){
                    log.error("mobile number is required");
                    throw new SmsBlendException("mobile number is required");
                }
                if (StrUtil.isBlank(message)){
                    log.error("message number is required");
                    throw new SmsBlendException("message number is required");
                }

                if (batch){
                    body = LuoSiMaoUtils.buildBody(phones, message, date);
                }else {
                    body = LuoSiMaoUtils.buildBody(phones.get(0), message);
                }
                smsResponse = getResponse(http.postBasicFrom(url, LuoSiMaoUtils.buildHeaders(), "api", "key-" + config.getAccessKeyId(), body));
            }
            log.debug("短信发送结果：{}", smsResponse);
        } catch (SmsBlendException e) {
            log.error(e.message, e);
            smsResponse = errorResp(e.message);
        }
        if (smsResponse.isSuccess() || retry == config.getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phones, message, date, batch, status);
    }

    private SmsResponse requestRetry(List<String> phones, String message, Date date, boolean batch, boolean status) {
        http.safeSleep(getConfig().getRetryInterval());
        retry ++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponse(phones, message, date, batch, status);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        return SmsRespUtils.resp(resJson, Objects.equals(0, resJson.getInt("error")), getConfigId());
    }

}