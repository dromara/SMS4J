package org.dromara.sms4j.zhutong.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.text.StrPool;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.zhutong.config.ZhutongConfig;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>助通短信发送
 * <p>1. 自定义短信发送 无需定义模板 https://doc.zthysms.com/web/#/1/14
 * <p>2. 模板短信发送  需定义模板   https://doc.zthysms.com/web/#/1/13
 */
@Slf4j
public class ZhutongSmsImpl extends AbstractSmsBlend {

    private final ZhutongConfig zhutongConfig;

    /**
     * ZhutongSmsImpl
     * <p>构造器，用于构造短信实现模块
     */
    public ZhutongSmsImpl(ZhutongConfig zhutongConfig, Executor pool, DelayedTime delayedTime) {
        super(pool, delayedTime);
        this.zhutongConfig = zhutongConfig;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        //如果模板id为空 or 模板变量名称为空，使用无模板的自定义短信发送
        if (StrUtil.hasBlank(zhutongConfig.getSignature(), zhutongConfig.getTemplateId(), zhutongConfig.getTemplateName())) {
            return getSmsResponse(phone, message);
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(zhutongConfig.getTemplateName(), message);
        return sendMessage(phone, zhutongConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return getSmsResponseTemplate(templateId, phone, messages);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        //如果模板id为空 or 模板变量名称为空，使用无模板的自定义短信发送
        if (StrUtil.hasBlank(zhutongConfig.getSignature(), zhutongConfig.getTemplateId(), zhutongConfig.getTemplateName())) {
            return getSmsResponse(phones, message);
        }

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(zhutongConfig.getTemplateName(), message);
        return massTexting(phones, zhutongConfig.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        return getSmsResponseTemplate(templateId, phones, messages);
    }

    /**
     * 发送 自定义短信：https://doc.zthysms.com/web/#/1/14
     */
    protected SmsResponse getSmsResponse(List<String> phones, String content) {
        String requestUrl = zhutongConfig.getRequestUrl();
        String username = zhutongConfig.getAccessKeyId();
        String password = zhutongConfig.getAccessKeySecret();

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


        String urls = requestUrl + "v2/sendSms";
        long tKey = System.currentTimeMillis() / 1000;
        Map<String, String> json = new HashMap<>(5);
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

        AtomicReference<SmsResponse> reference = new AtomicReference<>();
        http.post(urls)
                .addHeader("Content-Type", Constant.APPLICATION_JSON_UTF8)
                .addBody(JSONUtil.toJsonStr(json))
                .onSuccess(((data, req, res) -> reference.set(this.getResponse(res.get(JSONObject.class)))))
                .onError((ex, req, res) -> reference.set(this.getResponse(res.get(JSONObject.class))))
                .execute();
        log.info("助通短信 URL={} json={} 响应值为={}", urls, json, reference.get());
        return reference.get();
    }

    protected SmsResponse getSmsResponse(String mobile, String content) {
        return getSmsResponse(ListUtil.of(mobile), content);
    }

    /**
     * 发送 模板短信：https://doc.zthysms.com/web/#/1/13
     */
    protected SmsResponse getSmsResponseTemplate(String templateId, List<String> phones, LinkedHashMap<String, String> messages) {
        String requestUrl = zhutongConfig.getRequestUrl();
        String username = zhutongConfig.getAccessKeyId();
        String password = zhutongConfig.getAccessKeySecret();
        String signature = zhutongConfig.getSignature();

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
        String urls = requestUrl + "v2/sendSmsTp";
        //请求入参
        JSONObject requestJson = new JSONObject();
        //账号
        requestJson.put("username", username);
        //tKey
        long tKey = System.currentTimeMillis() / 1000;
        requestJson.put("tKey", tKey);
        //明文密码
        requestJson.put("password", SecureUtil.md5(SecureUtil.md5(password) + tKey));
        //模板ID
        requestJson.put("tpId", templateId);
        //签名
        requestJson.put("signature", signature);
        //扩展号
        requestJson.put("ext", "");
        //自定义参数
        requestJson.put("extend", "");
        //发送记录集合
        JSONArray records = new JSONArray();
        {
            for (String mobile : phones) {
                JSONObject record = new JSONObject();
                //手机号
                record.put("mobile", mobile);
                //替换变量
                JSONObject param = new JSONObject();
                param.putAll(messages);
                record.put("tpContent", param);
                records.add(record);
            }
        }
        requestJson.put("records", records);

        AtomicReference<SmsResponse> reference = new AtomicReference<>();
        http.post(urls)
                .addHeader("Content-Type", Constant.APPLICATION_JSON_UTF8)
                .addBody(requestJson)
                .onSuccess(((data, req, res) -> reference.set(this.getResponse(res.get(JSONObject.class)))))
                .onError((ex, req, res) -> reference.set(this.getResponse(res.get(JSONObject.class))))
                .execute();
        log.info("助通短信 URL={} json={} 响应值为={}", urls, requestJson, reference.get());
        return reference.get();
    }

    protected SmsResponse getSmsResponseTemplate(String templateId, String mobile, LinkedHashMap<String, String> content) {
        return getSmsResponseTemplate(templateId, ListUtil.of(mobile), content);
    }

    private SmsResponse getResponse(JSONObject jsonObject) {
        SmsResponse response = new SmsResponse();
        response.setSuccess(jsonObject.getInt("code", -1) <= 200);
        response.setData(jsonObject);
        return response;
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
