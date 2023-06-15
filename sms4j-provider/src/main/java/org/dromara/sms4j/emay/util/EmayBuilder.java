package org.dromara.sms4j.emay.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.net.URLEncoder;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Richard
 * @version 1.0
 * @date 2023/4/11 15:11
 */
@Slf4j
public class EmayBuilder {

    public static Map<String, Object> buildRequestBody(String appId, String secretKey, String phone,
                                                       String message) throws SmsBlendException {
        return getParamsMap(appId, secretKey, phone, message);
    }

    /**
     * @param appId       用户Appid(必填)
     * @param secretKey   加密key
     * @param phone
     * @param message
     * @param customSmsId 自定义消息ID(选填) 最长64位
     * @return
     * @throws SmsBlendException
     */
    public static Map<String, Object> buildRequestBody(String appId, String secretKey, String phone,
                                                       String message, String customSmsId) throws SmsBlendException {
        Map<String, Object> params = getParamsMap(appId, secretKey, phone, message);
        params.put("customSmsId", customSmsId);
        return params;
    }

    private static Map<String, Object> getParamsMap(String appId, String secretKey, String phone, String message) {
        Map<String, Object> params = new HashMap<>();
        // 时间戳(必填)  格式：yyyyMMddHHmmss
        String timestamp = DateUtil.format(new Date(), DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String sign = SecureUtil.md5(appId + secretKey + timestamp);
        params.put("appId", appId);
        params.put("timestamp", timestamp);
        params.put("sign", sign);
        params.put("mobiles", phone);
        try {
            params.put("content", URLEncoder.encode(message, "utf-8"));
        } catch (Exception e) {
            log.error("EmaySmsImpl urlEncode content error", e);
            throw new SmsBlendException(e.getMessage());
        }
        return params;
    }

    public static String listToString(List<String> list) {
        StringBuilder stringBuffer = new StringBuilder();
        stringBuffer.append("[\"");
        for (String s : list) {
            stringBuffer.append(s);
            stringBuffer.append("\"");
            stringBuffer.append(",");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

}
