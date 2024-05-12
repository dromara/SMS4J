package org.dromara.sms4j.zhutong.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import org.dromara.sms4j.zhutong.config.ZhutongConfig;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * <p>助通短信发送
 * <p>1. 自定义短信发送 无需定义模板 https://doc.zthysms.com/web/#/1/14
 * <p>2. 模板短信发送  需定义模板   https://doc.zthysms.com/web/#/1/13
 */
@Slf4j
public class ZhutongSmsImpl extends AbstractSmsBlend<ZhutongConfig> {

    private int retry = 0;

    /**
     * ZhutongSmsImpl
     * <p>构造器，用于构造短信实现模块
     */
    public ZhutongSmsImpl(ZhutongConfig zhutongConfig, Executor pool, DelayedTime delayedTime) {
        super(zhutongConfig, pool, delayedTime);
    }

    /**
     * ZhutongSmsImpl
     * <p>构造器，用于构造短信实现模块
     */
    public ZhutongSmsImpl(ZhutongConfig zhutongConfig) {
        super(zhutongConfig);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.ZHUTONG;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        ZhutongConfig config = getConfig();
        //如果模板id为空 or 模板变量名称为空，使用无模板的自定义短信发送
        if (StrUtil.hasBlank(config.getSignature(), config.getTemplateId(), config.getTemplateName())) {
            return getSmsResponse(phone, message);
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(config.getTemplateName(), message);
        return sendMessage(phone, config.getTemplateId(), map);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<>();
        }
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<>();
        }
        return getSmsResponseTemplate(templateId, phone, messages);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        ZhutongConfig config = getConfig();
        //如果模板id为空 or 模板变量名称为空，使用无模板的自定义短信发送
        if (StrUtil.hasBlank(config.getSignature(), config.getTemplateId(), config.getTemplateName())) {
            return getSmsResponse(phones, message);
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(config.getTemplateName(), message);
        return massTexting(phones, config.getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<>();
        }
        return getSmsResponseTemplate(templateId, phones, messages);
    }

    /**
     * 发送 自定义短信：https://doc.zthysms.com/web/#/1/14
     */
    protected SmsResponse getSmsResponse(List<String> phones, String content) {
        ZhutongConfig config = getConfig();
        String requestUrl = config.getRequestUrl();
        String username = config.getAccessKeyId();
        String password = config.getAccessKeySecret();

        validator(requestUrl, username, password);
        if (CollectionUtil.isEmpty(phones)) {
            throw new SmsBlendException("助通短信：手机号不能为空！");
        }
        if (phones.size() >= 2000) {
            throw new SmsBlendException("助通短信：手机号码最多支持2000个！");
        }
        if (StrUtil.isBlank(content)) {
            throw new SmsBlendException("助通短信：发送内容不能为空！");
        }
        if (content.length() >= 1000) {
            throw new SmsBlendException("助通短信：发送内容不能超过1000个字符！");
        }
        if (!content.contains("【")) {
            throw new SmsBlendException("助通短信：自定义短信发送内容必须包含签名信息，如：【助通科技】您的验证码是8888！");
        }


        String url = requestUrl + "v2/sendSms";
        long tKey = System.currentTimeMillis() / 1000;
        Map<String, Object> json = new HashMap<>(5);
        //账号
        json.put("username", username);
        //密码
        json.put("password", SecureUtil.md5(SecureUtil.md5(password) + tKey));
        //tKey
        json.put("tKey", tKey + "");
        //手机号
        json.put("mobile", StrUtil.join(StrPool.COMMA, phones));
        //内容
        json.put("content", content);

        Map<String, String> headers = MapUtil.newHashMap(1, true);
        headers.put("Content-Type", Constant.APPLICATION_JSON_UTF8);
        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postJson(url, headers, json));
        } catch (SmsBlendException e) {
            smsResponse = SmsRespUtils.error(e.getMessage(), getConfigId());
        }
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phones, content);
    }

    private SmsResponse requestRetry(List<String> phones, String content) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponse(phones, content);
    }

    protected SmsResponse getSmsResponse(String mobile, String content) {
        return getSmsResponse(ListUtil.of(mobile), content);
    }

    /**
     * 发送 模板短信：https://doc.zthysms.com/web/#/1/13
     */
    protected SmsResponse getSmsResponseTemplate(String templateId, List<String> phones, LinkedHashMap<String, String> messages) {
        ZhutongConfig config = getConfig();
        String requestUrl = config.getRequestUrl();
        String username = config.getAccessKeyId();
        String password = config.getAccessKeySecret();
        String signature = config.getSignature();

        validator(requestUrl, username, password);
        if (StrUtil.isBlank(signature)) {
            throw new SmsBlendException("助通短信：模板短信中已报备的签名signature不能为空！");
        }
        //助通短信签名，需要包含：【】，这里自助添加
        if (!signature.startsWith("【")) {
            signature = "【" + signature;
        }
        if (!signature.endsWith("】")) {
            signature = signature + "】";
        }
        if (StrUtil.isBlank(templateId)) {
            throw new SmsBlendException("助通短信：模板短信模板id不能为空！！");
        }

        //地址
        String url = requestUrl + "v2/sendSmsTp";
        //请求入参
        JSONObject requestJson = new JSONObject();
        //账号
        requestJson.set("username", username);
        //tKey
        long tKey = System.currentTimeMillis() / 1000;
        requestJson.set("tKey", tKey);
        //明文密码
        requestJson.set("password", SecureUtil.md5(SecureUtil.md5(password) + tKey));
        //模板ID
        requestJson.set("tpId", templateId);
        //签名
        requestJson.set("signature", signature);
        //扩展号
        requestJson.set("ext", "");
        //自定义参数
        requestJson.set("extend", "");
        //发送记录集合
        JSONArray records = new JSONArray();
        {
            for (String mobile : phones) {
                JSONObject record = new JSONObject();
                //手机号
                record.set("mobile", mobile);
                //替换变量
                JSONObject param = new JSONObject();
                param.putAll(messages);
                record.set("tpContent", param);
                records.add(record);
            }
        }
        requestJson.set("records", records);

        Map<String, String> headers = MapUtil.newHashMap(1, true);
        headers.put("Content-Type", Constant.APPLICATION_JSON_UTF8);
        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postJson(url, headers, requestJson.toString()));
        } catch (SmsBlendException e) {
            smsResponse = SmsRespUtils.error(e.getMessage(), getConfigId());
        }
        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(templateId, phones, messages);
    }

    private SmsResponse requestRetry(String templateId, List<String> phones, LinkedHashMap<String, String> messages) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponseTemplate(templateId, phones, messages);
    }

    protected SmsResponse getSmsResponseTemplate(String templateId, String mobile, LinkedHashMap<String, String> content) {
        return getSmsResponseTemplate(templateId, ListUtil.of(mobile), content);
    }

    private SmsResponse getResponse(JSONObject jsonObject) {
        return SmsRespUtils.resp(jsonObject, jsonObject.getInt("code", -1) <= 200, getConfigId());
    }

    private void validator(String requestUrl, String username, String password) {
        if (StrUtil.isBlank(requestUrl)) {
            throw new SmsBlendException("助通短信：requestUrl不能为空！");
        }
        if (!requestUrl.endsWith("/")) {
            throw new SmsBlendException("助通短信：requestUrl必须以'/'结尾!");
        }
        if (StrUtil.hasBlank(username, password)) {
            throw new SmsBlendException("助通短信：账号username、密码password不能为空！");
        }
    }
}
