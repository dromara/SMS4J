package org.dromara.sms4j.qiniu.service;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
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
        //获取url
        String url = getConfig().getBaseUrl() + getConfig().getSingleMsgUrl();

        Map<String, String> msg = new LinkedHashMap<>();
        msg.put(getConfig().getTemplateName(), message);
        //签名id
        String signatureId = getConfig().getSignatureId();
        //模型id
        String templateId = getConfig().getTemplateId();
        //手机号
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", phone);
        hashMap.put("template_id", templateId);
        hashMap.put("signature_id", signatureId);
        hashMap.put("parameters", JSONUtil.toJsonStr(msg));
        String jsonBody = JSONUtil.toJsonStr(hashMap);
        String signature = null;
        try {
            signature = QiNiuUtils.getSignature("POST",url, getConfig(), jsonBody);
        } catch (Exception e) {
            log.error("签名失败", e);
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String signDate = dateFormat.format(new Date());
        //请求头
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", signature);
        header.put("X-Qiniu-Date", signDate);
        header.put("Content-Type", "application/json");
        //请求体
        JSONObject jsonObject = http.postJson(url, header, jsonBody);
        System.out.println(jsonObject.toString());
        System.out.println(jsonObject.toString());
        System.out.println(jsonObject.toString());
        System.out.println(jsonObject.toString());
        System.out.println(jsonObject.toString());
        return null;
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        return null;
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return null;
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return null;
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        return null;
    }
}
