package org.dromara.sms4j.netease.utils;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.netease.config.NeteaseConfig;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author adam
 * @create 2023/5/29 17:49
 */

public class NeteaseUtils {

    /**
     * 计算并获取CheckSum
     * @param appSecret
     * @param nonce
     * @param curTime
     * @return
     */
    public static String getCheckSum(String appSecret, String nonce, String curTime) {
        return encode("sha1", appSecret + nonce + curTime);
    }

    private static String encode(String algorithm, String value) {
        if (value == null) {
            return null;
        }
        try {
            MessageDigest messageDigest
                    = MessageDigest.getInstance(algorithm);
            messageDigest.update(value.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
    }
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        for (byte aByte : bytes) {
            buf.append(HEX_DIGITS[(aByte >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[aByte & 0x0f]);
        }
        return buf.toString();
    }

    /**
     * url编码
     */
    private static String specialUrlEncode(String value)  {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name()).replace("+", "%20")
                    .replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateParamBody(Map<String, String> body) {
        StringBuilder sortQueryString = new StringBuilder();
        for (String key : body.keySet()) {
            sortQueryString.append("&").append(specialUrlEncode(key)).append("=")
                    .append(specialUrlEncode(body.get(key)));
        }
        return sortQueryString.substring(1);
    }

    private static Map<String, String> generateParamMap(NeteaseConfig neteaseConfig, List<String> phone, String message, String templateId) {
        Map<String, String> paramMap = new HashMap<>();
        JSONArray messageArray = JSONUtil.createArray();
        messageArray.add(message);
        JSONArray phoneArray = JSONUtil.createArray();
        phoneArray.add(phone);
        paramMap.put("mobiles", phoneArray.toString());
        paramMap.put("SignName", neteaseConfig.getSignature());
        paramMap.put("params", messageArray.toString());
        paramMap.put("templateid", templateId);
        paramMap.put("needUp", neteaseConfig.getNeedUp().toString());
        return paramMap;
    }

    private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
}
