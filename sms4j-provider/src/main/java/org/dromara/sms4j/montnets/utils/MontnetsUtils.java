package org.dromara.sms4j.montnets.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import org.dromara.sms4j.comm.utils.SmsDateUtils;
import org.dromara.sms4j.montnets.config.MontnetsConfig;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author SU
 * 2025/4/23  10:32
 */
public class MontnetsUtils {

    public static String generateSendSmsRequestUrl(MontnetsConfig montnetsConfig) {
        String url = montnetsConfig.getUrl();
        String api = montnetsConfig.getApi();
        return url + api;
    }

    /**
     * 生成请求body参数
     *
     * @param montnetsConfig 短信配置
     * @param phone          手机号
     * @param message        短信内容
     * @param templateId     模板ID
     */
    public static Map<String, String> generateParamMap(MontnetsConfig montnetsConfig, String phone, String message, String templateId) {
        Map<String, String> paramMap = new HashMap<>();

        String accessKeyId = montnetsConfig.getAccessKeyId();
        String accessKeySecret = montnetsConfig.getAccessKeySecret();
        String timeStamp = SmsDateUtils.formatDateToStr(new Date(), "MMddHHmmss", SmsDateUtils.gmt8());

        paramMap.put("userid", accessKeyId);
        paramMap.put("pwd", DigestUtil.md5Hex(accessKeyId.toUpperCase(Locale.ENGLISH) + "00000000" + accessKeySecret + timeStamp));
        paramMap.put("timestamp", timeStamp);
        paramMap.put("mobile", phone);
        paramMap.put("content", message);
        paramMap.put("tmplid", templateId);

        return paramMap;
    }

    /**
     * 生成请求参数body字符串
     *
     * @param montnetsConfig 短信配置
     * @param phone          手机号
     * @param message        短信内容
     * @param templateId     模板ID
     */
    public static String generateParamBody(MontnetsConfig montnetsConfig, String phone, String message, String templateId) {
        Map<String, String> paramMap = generateParamMap(montnetsConfig, phone, message, templateId);
        return JSONUtil.toJsonStr(paramMap);
    }
}