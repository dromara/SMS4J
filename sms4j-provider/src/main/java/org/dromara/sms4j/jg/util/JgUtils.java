package org.dromara.sms4j.jg.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.jg.config.JgConfig;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>类名: JgHelper
 * <p>说明：极光 sms
 *
 * @author :SmartFire
 * 2024/3/15
 **/
@Slf4j
public class JgUtils {

    /**
     * 构造请求地址
     * @param baseUrl 配置的baseUrl
     * @param action 请求方法
     * @param msgId 验证验证码是否有效时使用 msgId 为调用发送验证码 API 的返回值
     * @return url
     */
    public static String buildUrl(String baseUrl, String action, String msgId) {
        if ("valid".equals(action)){
            check(msgId);
            return baseUrl + "codes/" + msgId +  "/" + action;
        }else {
            return baseUrl + action;
        }
    }

    /**
     * 构造请求头
     * @param accessKeyId appKey
     * @param accessKeySecret appKey
     * @return 请求头
     */
    public static Map<String, String> buildHeaders(String accessKeyId, String accessKeySecret){
        check(accessKeyId);
        check(accessKeySecret);
        Map<String, String> headers = new LinkedHashMap<>(3);
        headers.put(Constant.ACCEPT, Constant.APPLICATION_JSON);
        headers.put(Constant.CONTENT_TYPE, Constant.APPLICATION_JSON_UTF8);
        headers.put(Constant.AUTHORIZATION, "Basic " + Base64.encode(accessKeyId + ":" + accessKeySecret, StandardCharsets.UTF_8));
        return headers;
    }

    /**
     * 构造请求body
     * @param phone 手机号
     * @param messages 消息体
     * @param templateId 模板 ID
     * @param config 配置
     * @param code 验证码
     * @return 请求body
     */
    public static Map<String, Object> buildBody(String phone, LinkedHashMap<String, String> messages, 
                                                String templateId, JgConfig config, String code) {
        checkAction(config.getAction());
        switch (config.getAction()){
            case "codes":
                return buildBody(phone, config.getSignId(), templateId);
            case "voice_codes":
                return buildBody(phone, code, config.getVoice(), config.getTtl());
            case "valid":
                return buildBody(code);
            case "messages/batch":
                return buildBody(phone, config.getSignId(), templateId, config.getTag(), messages);
            default:
                return buildBody(phone, config.getSignId(), templateId, messages);
        }
    }

    /**
     * 构造返回json验证Key值
     * @param action 请求方法
     * @return 返回json验证Key值
     */
    public static String buildJsonKey(String action){
        checkAction(action);
        switch (action){
            case "valid":
                return "is_valid";
            case "messages/batch":
                return "success_count";
            default:
                return "msg_id";
        }
    }

    /**
     * 构造请求body 发送文本验证码短信
     * @param phone 手机号
     * @param signId 签名 ID，该字段为空则使用应用默认签名
     * @param templateId 模板 ID
     * @return 请求body
     */
    private static Map<String, Object> buildBody(String phone, String signId, String templateId) {
        checkSingle(phone);
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("mobile", phone);
        check(templateId);
        map.put("temp_id", templateId);
        if (SmsUtils.isNotEmpty(signId)){
            map.put("sign_id", signId);
        }
        return map;
    }

    /**
     * 构造请求body 发送语音验证码短信
     * @param phone 手机号
     * @param code 语音验证码的值，验证码仅支持 4-8 个数字 可为空
     * @param voice 语音验证码播报语言选择，0：中文播报，1：英文播报，2：中英混合播报
     * @param ttl 验证码有效期，默认为 60 秒
     * @return 请求body
     */
    private static Map<String, Object> buildBody(String phone, String code, String voice, Integer ttl) {
        checkSingle(phone);
        Map<String, Object> map = new LinkedHashMap<>(1);
        map.put("mobile", phone);
        if (SmsUtils.isNotEmpty(code)) {
            map.put("code", code);
        }
        if (SmsUtils.isNotEmpty(voice)){
            checkVoice(voice);
            map.put("voice_lang", voice);
        }
        if (ttl == null || ttl <= 0){
            map.put("ttl", 60);
        }else {
            map.put("ttl", ttl);
        }
        return map;
    }

    /**
     * 构造请求body 验证验证码是否有效
     * @param code 验证码
     * @return 请求body
     */
    private static Map<String, Object> buildBody(String code) {
        check(code);
        Map<String, Object> map = new LinkedHashMap<>(1);
        map.put("code", code);
        return map;
    }

    /**
     * 构造请求body 发送单条模板短信
     * @param phone 手机号码
     * @param signId 签名 ID，该字段为空则使用应用默认签名
     * @param templateId 模板 ID
     * @param messages 模板参数,需要替换的参数名和 value 的键值对 可为空
     * @return 请求body
     */
    private static Map<String, Object> buildBody(String phone, String signId, String templateId, LinkedHashMap<String, String> messages) {
        checkSingle(phone);
        Map<String, Object> map = new LinkedHashMap<>(1);
        map.put("mobile", phone);
        if (SmsUtils.isNotEmpty(signId)){
            map.put("sign_id", signId);
        }
        check(templateId);
        map.put("temp_id", templateId);
        checkMessages(messages);
        map.put("temp_para", messages);
        return map;
    }

    /**
     * 构造请求body 发送批量模板短信
     * @param phone 手机号码列表
     * @param signId 签名 ID，该字段为空则使用应用默认签名
     * @param templateId 模板 ID
     * @param tag 标签 可为空
     * @param messages 模板参数,需要替换的参数名和 value 的键值对
     * @return 请求body
     */
    private static Map<String, Object> buildBody(String phone, String signId, String templateId,
                                                String tag, LinkedHashMap<String, String> messages) {
        Set<String> phones = build(phone);
        Map<String, Object> map = new LinkedHashMap<>(1);
        if (SmsUtils.isNotEmpty(signId)){
            map.put("sign_id", signId);
        }
        if (SmsUtils.isNotEmpty(tag)){
            map.put("tag", tag);
        }
        if (SmsUtils.isEmpty(templateId)){
            log.error("templateId is required");
            throw new SmsBlendException("templateId is required");
        }
        map.put("temp_id", templateId);
        if (SmsUtils.isEmpty(messages)){
            log.error("temp_para is required");
            throw new SmsBlendException("temp_para is required");
        }
        List<Map<String, Object>> recipients = new ArrayList<>(phones.size());
        phones.forEach(mobile -> {
            Map<String, Object> params = new LinkedHashMap<>(1);
            params.put("mobile", StrUtil.addPrefixIfNot(mobile, "+86"));
            params.put("temp_para", messages);
            recipients.add(params);
        });
        map.put("recipients", recipients);
        return map;
    }

    private static Set<String> build(String phone){
        check(phone);
        return Arrays.stream(phone.split(","))
                .filter(SmsUtils::isNotEmpty)
                .map(String::trim)
                .collect(Collectors.toSet());
    }

    private static void checkSingle(String phone){
        Set<String> phones = build(phone);
        if (phones.size() > 1) {
            log.error("Only a single mobile number is supported");
            throw new SmsBlendException("Only a single mobile number is supported");
        }
    }

    private static void checkMessages(LinkedHashMap<String, String> messages){
        if (SmsUtils.isEmpty(messages)){
            log.error("temp_para is required");
            throw new SmsBlendException("temp_para is required");
        }
    }

    private static void checkVoice(String voice){
        if (!StrUtil.equalsAny(voice, "0", "1", "2")){
            log.error("voice_lang is error, the value of an is only [1,2,3]");
            throw new SmsBlendException("voice_lang is error, the value of an is only [1,2,3]");
        }
    }

    private static void checkAction(String action){
        if (SmsUtils.isEmpty(action) || !StrUtil.equalsAny(action, "codes", "voice_codes", "valid", "messages", "messages/batch")){
            log.error("Unknown action method");
            throw new SmsBlendException("Unknown action method");
        }
    }

    private static void check(String str){
        if (SmsUtils.isEmpty(str)){
            String error = str + " is required";
            log.error(error);
            throw new SmsBlendException(error);
        }
    }
}
