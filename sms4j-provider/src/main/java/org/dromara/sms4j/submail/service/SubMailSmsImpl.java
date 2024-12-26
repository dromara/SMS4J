package org.dromara.sms4j.submail.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import org.dromara.sms4j.submail.config.SubMailConfig;
import org.dromara.sms4j.submail.utils.SubMailUtils;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * <p>类名: SubMailSmsImpl
 *
 * @author :bleachtred
 * 2023/5/12  15:06
 **/
@Slf4j
public class SubMailSmsImpl extends AbstractSmsBlend<SubMailConfig> {

    private int retry = 0;

    public SubMailSmsImpl(SubMailConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    public SubMailSmsImpl(SubMailConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.MY_SUBMAIL;
    }

    @Override
    public SmsResponse sendMessage(String phone, String content) {
        return getSmsResponse(Collections.singletonList(phone), content, getConfig().getTemplateId(), null);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> vars) {
        if (MapUtil.isEmpty(vars)){
            log.error("vars or content must be not null");
            throw new SmsBlendException("vars or content must be not null");
        }
        String content = vars.get("content");
        vars.remove("content");
        return getSmsResponse(Collections.singletonList(phone), content, getConfig().getTemplateId(), vars);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> vars) {
        if (MapUtil.isEmpty(vars)){
            log.error("vars or content must be not null");
            throw new SmsBlendException("vars or content must be not null");
        }
        String content = vars.get("content");
        vars.remove("content");
        return getSmsResponse(Collections.singletonList(phone), content, templateId, vars);
    }

    @SuppressWarnings("unchecked")
    @Override
    public SmsResponse massTexting(List<String> phones, String content) {
        if (StrUtil.isBlank(content)){
            log.error("vars or content must be not null");
            throw new SmsBlendException("vars or content must be not null");
        }
        return massTexting(phones, getConfig().getTemplateId(), BeanUtil.copyProperties(content, LinkedHashMap.class));
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> vars) {
        if (MapUtil.isEmpty(vars)){
            log.error("vars or content must be not null");
            throw new SmsBlendException("vars or content must be not null");
        }
        String content = vars.get("content");
        vars.remove("content");
        return getSmsResponse(phones, content, templateId, vars);
    }

    private SmsResponse getSmsResponse(List<String> phones, String content, String templateId, LinkedHashMap<String, String> vars) {
        if (CollUtil.isEmpty(phones)){
            log.error("phones must be not null");
            throw new SmsBlendException("phones must be not null");
        }
        SubMailConfig config = getConfig();
        SmsResponse smsResponse;
        String url = config.getHost() + config.getAction();
        LinkedHashMap<String, Object> body;
        switch (config.getAction()){
            case "send.json":
                if (StrUtil.isBlank(content)){
                    log.error("content must be not null");
                    throw new SmsBlendException("content must be not null");
                }
                body = buildSend(phones.get(0), content);
                break;
            case "xsend.json":
                body = buildXSend(phones.get(0), templateId, vars);
                break;
            case "multisend.json":
                if (StrUtil.isBlank(content)){
                    log.error("content must be not null");
                    throw new SmsBlendException("content must be not null");
                }
                if (MapUtil.isEmpty(vars)){
                    log.error("vars be not null");
                    throw new SmsBlendException("vars must be not null");
                }
                body = buildMultiSend(phones, content, vars);
                break;
            case "multixsend.json":
                if (MapUtil.isEmpty(vars)){
                    log.error("vars be not null");
                    throw new SmsBlendException("vars must be not null");
                }
                body = buildMultiXSend(phones, templateId, vars);
                break;
            case "batchsend.json":
                if (StrUtil.isBlank(content)){
                    log.error("vars or content must be not null");
                    throw new SmsBlendException("vars or content must be not null");
                }
                body = buildBatchSend(phones, content);
                break;
            case "batchxsend.json":
                if (MapUtil.isEmpty(vars)){
                    log.error("vars be not null");
                    throw new SmsBlendException("vars must be not null");
                }
                body = buildBatchXSend(phones, templateId, vars);
                break;
            default:
                log.error("不支持的短信发送类型");
                throw new SmsBlendException("不支持的短信发送类型");
        }
        try {
            smsResponse = getResponse(http.postJson(url, SubMailUtils.buildHeaders(), body));
            log.debug("短信发送结果: {}", JSONUtil.toJsonStr(smsResponse));
        } catch (SmsBlendException e) {
            log.error(e.message, e);
            smsResponse = errorResp(e.message);
        }
        if (smsResponse.isSuccess() || retry == config.getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(phones, content, templateId, vars);
    }

    private SmsResponse requestRetry(List<String> phones, String content, String templateId, LinkedHashMap<String, String> vars) {
        http.safeSleep(getConfig().getRetryInterval());
        retry ++;
        log.warn("短信第 {} 次重新发送", retry);
        return getSmsResponse(phones, content, templateId, vars);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        return SmsRespUtils.resp(resJson, "success".equals(resJson.getStr("status")), getConfigId());
    }

    /**
     * SMS/Send - 短信发送
     * @param phone 单个手机号
     * @param content 短信内容
     * @return 参数组装
     */
    private LinkedHashMap<String, Object> buildSend(String phone, String content){
        SubMailConfig config = getConfig();
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("appid", config.getAccessKeyId());
        body.put("to", StrUtil.addPrefixIfNot(phone, "+86"));
        if (StrUtil.isNotBlank(config.getSignature())){
            content = StrUtil.addPrefixIfNot(content, "【 " + config.getSignature() + "】");
        }
        content = StrUtil.sub(content, 0, 1000);
        body.put("content", content);
        body.put("timestamp", timestamp());
        body.put("sign_type", config.getSignType());
        if (StrUtil.isNotBlank(config.getSignVersion())){
            body.put("sign_version", config.getSignVersion());
        }
        body.put("sign_type", config.getSignType());
        String signature = SubMailUtils.signature(body, config.getSignType(), config.getAccessKeyId(), config.getAccessKeySecret(), "content");
        body.put("signature", signature);
        return body;
    }

    /**
     * SMS/XSend - 短信模板发送
     * @param phone 单个手机号
     * @param templateId 短信模板ID
     * @param vars 使用文本变量动态控制短信中的文本
     * @return 参数组装
     */
    private LinkedHashMap<String, Object> buildXSend(String phone, String templateId, LinkedHashMap<String, String> vars){
        SubMailConfig config = getConfig();
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("appid", config.getAccessKeyId());
        body.put("to", StrUtil.addPrefixIfNot(phone, "+86"));
        body.put("project", templateId);
        if (MapUtil.isNotEmpty(vars)){
            body.put("vars", JSONUtil.toJsonStr(vars));
        }
        body.put("timestamp", timestamp());
        body.put("sign_type", config.getSignType());
        if (StrUtil.isNotBlank(config.getSignVersion())){
            body.put("sign_version", config.getSignVersion());
        }
        body.put("sign_type", config.getSignType());
        String signature = SubMailUtils.signature(body, config.getSignType(), config.getAccessKeyId(), config.getAccessKeySecret(), "vars");
        body.put("signature", signature);
        return body;
    }

    /**
     * SMS/MultiSend - 短信一对多发送
     * 建议：单线程提交数量控制在50个联系人， 可以开多个线程同时发送
     * @param phones N手机号
     * @param content 短信内容
     * @param vars 使用文本变量动态控制短信中的文本
     * @return 参数组装
     */
    private LinkedHashMap<String, Object> buildMultiSend(List<String> phones, String content, LinkedHashMap<String, String> vars){
        SubMailConfig config = getConfig();
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("appid", config.getAccessKeyId());
        if (StrUtil.isNotBlank(config.getSignature())){
            content = StrUtil.addPrefixIfNot(content, "【 " + config.getSignature() + "】");
        }
        content = StrUtil.sub(content, 0, 1000);
        body.put("content", content);
        phones = CollUtil.sub(phones, 0, 50);
        List<LinkedHashMap<String, Object>> multi = new ArrayList<>(phones.size());
        phones.forEach(phone -> {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("to", StrUtil.addPrefixIfNot(phone, "+86"));
            map.put("vars", vars);
            multi.add(map);
        });
        body.put("multi", JSONUtil.toJsonStr(multi));
        body.put("timestamp", timestamp());
        body.put("sign_type", config.getSignType());
        if (StrUtil.isNotBlank(config.getSignVersion())){
            body.put("sign_version", config.getSignVersion());
        }
        body.put("sign_type", config.getSignType());
        String signature = SubMailUtils.signature(body, config.getSignType(), config.getAccessKeyId(), config.getAccessKeySecret(), "multi", "content");
        body.put("signature", signature);
        return body;
    }

    /**
     * SMS/MultiXSend - 短信模板一对多发送
     * 建议： 单线程提交数量控制在50—200个联系人， 可以开多个线程同时发送
     * @param phones N手机号
     * @param templateId 短信模板ID
     * @param vars 使用文本变量动态控制短信中的文本
     * @return 参数组装
     */
    private LinkedHashMap<String, Object> buildMultiXSend(List<String> phones, String templateId, LinkedHashMap<String, String> vars){
        SubMailConfig config = getConfig();
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("appid", config.getAccessKeyId());
        body.put("project", templateId);
        phones = CollUtil.sub(phones, 0, 200);
        List<LinkedHashMap<String, Object>> multi = new ArrayList<>(phones.size());
        phones.forEach(phone -> {
            LinkedHashMap<String, Object> map = new LinkedHashMap<>();
            map.put("to", StrUtil.addPrefixIfNot(phone, "+86"));
            map.put("vars", vars);
            multi.add(map);
        });
        body.put("multi", JSONUtil.toJsonStr(multi));
        body.put("timestamp", timestamp());
        body.put("sign_type", config.getSignType());
        if (StrUtil.isNotBlank(config.getSignVersion())){
            body.put("sign_version", config.getSignVersion());
        }
        body.put("sign_type", config.getSignType());
        String signature = SubMailUtils.signature(body, config.getSignType(), config.getAccessKeyId(), config.getAccessKeySecret(), "multi", "content");
        body.put("signature", signature);
        return body;
    }

    /**
     * SMS/BatchSend - 短信批量群发
     * 单次请求最大支持 10000 个
     * @param phones N手机号
     * @param content 短信内容
     * @return 参数组装
     */
    private LinkedHashMap<String, Object> buildBatchSend(List<String> phones, String content){
        SubMailConfig config = getConfig();
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("appid", config.getAccessKeyId());
        phones = CollUtil.sub(phones, 0, 10000);
        body.put("to", SmsUtils.addCodePrefixIfNot(phones));
        if (StrUtil.isNotBlank(config.getSignature())){
            content = StrUtil.addPrefixIfNot(content, "【 " + config.getSignature() + "】");
        }
        content = StrUtil.sub(content, 0, 1000);
        body.put("content", content);
        body.put("timestamp", timestamp());
        body.put("sign_type", config.getSignType());
        if (StrUtil.isNotBlank(config.getSignVersion())){
            body.put("sign_version", config.getSignVersion());
        }
        body.put("sign_type", config.getSignType());
        String signature = SubMailUtils.signature(body, config.getSignType(), config.getAccessKeyId(), config.getAccessKeySecret(), "content");
        body.put("signature", signature);
        return body;
    }

    /**
     * SMS/BatchXSend - 短信批量模板群发
     * 单次请求最大支持 10000 个
     * @param phones N手机号
     * @param templateId 短信模板ID
     * @param vars 使用文本变量动态控制短信中的文本
     * @return 参数组装
     */
    private LinkedHashMap<String, Object> buildBatchXSend(List<String> phones, String templateId, LinkedHashMap<String, String> vars){
        SubMailConfig config = getConfig();
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("appid", config.getAccessKeyId());
        phones = CollUtil.sub(phones, 0, 10000);
        body.put("to", SmsUtils.addCodePrefixIfNot(phones));
        body.put("project", templateId);
        if (MapUtil.isNotEmpty(vars)){
            body.put("vars", JSONUtil.toJsonStr(vars));
        }
        body.put("timestamp", timestamp());
        body.put("sign_type", config.getSignType());
        if (StrUtil.isNotBlank(config.getSignVersion())){
            body.put("sign_version", config.getSignVersion());
        }
        body.put("sign_type", config.getSignType());
        String signature = SubMailUtils.signature(body, config.getSignType(), config.getAccessKeyId(), config.getAccessKeySecret(), "vars");
        body.put("signature", signature);
        return body;
    }

    private String timestamp(){
        JSONObject resp = http.getUrl("https://api-v4.mysubmail.com/service/timestamp");
        return resp.getStr("timestamp");
    }
}