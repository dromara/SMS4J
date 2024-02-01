package org.dromara.sms4j.qiniu.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.gson.Gson;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import org.dromara.sms4j.qiniu.config.QiNiuConfig;
import org.dromara.sms4j.qiniu.util.QiNiuUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author Administrator
 * @Date: 2024/1/30 16:06 59
 * @描述: QiNiuSmsImpl
 **/
@Slf4j
public class QiNiuSmsImpl extends AbstractSmsBlend<QiNiuConfig> {

    private int retry = 0;

    @Override
    public String getSupplier() {
        return SupplierConstant.QINIU;
    }

    public QiNiuSmsImpl(QiNiuConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
    }

    public QiNiuSmsImpl(QiNiuConfig config) {
        super(config);
    }


    @Override
    public SmsResponse sendMessage(String phone, String message) {
        return sendSingleMsg(phone, getConfig().getTemplateId(), new LinkedHashMap<String, String>() {{
            put(getConfig().getTemplateName(), message);
        }});
    }


    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        return sendSingleMsg(phone, getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return sendSingleMsg(phone, templateId, messages);
    }


    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> params = new LinkedHashMap<>();
        params.put(getConfig().getTemplateName(), message);
        return senMassMsg(phones, getConfig().getTemplateId(), params);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        return senMassMsg(phones, templateId, messages);
    }


    /**
     * @return SmsResponse
     * @author 初拥。
     * @date 2024/1/31 8:54
     * @Description: 统一处理返回结果
     */
    public SmsResponse handleRes(String url, HashMap<String, Object> params) {
        JSONObject jsonObject = http.postJson(url, QiNiuUtils.getHeaderAndSign(url, params, getConfig()), params);
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(ObjectUtil.isEmpty(jsonObject.getStr("error")));
        smsResponse.setData(jsonObject);
        smsResponse.setConfigId(getConfigId());
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {" + retry + "} 次重新发送");
        return handleRes(url, params);
    }

    /**
     * @param phones
     * @param templateId
     * @param messages
     * @return SmsResponse
     * @author 初拥。
     * @date 2024/1/31 9:20
     * @Description: 发送群发短信
     */
    private SmsResponse senMassMsg(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        String url = getConfig().getBaseUrl() + getConfig().getMassMsgUrl();
        HashMap<String, Object> params = new HashMap<>();
        params.put("template_id", templateId);
        params.put("mobiles", phones.toArray());
        params.put("parameters", messages);
        return handleRes(url, params);
    }


    private SmsResponse sendSingleMsg(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String url = getConfig().getBaseUrl() + getConfig().getSingleMsgUrl();
        //手机号
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("mobile", phone);
        hashMap.put("template_id", templateId);
        hashMap.put("parameters", messages);

        log.info("hashMap:{}", hashMap);

        return handleRes(url, hashMap);
    }

}
