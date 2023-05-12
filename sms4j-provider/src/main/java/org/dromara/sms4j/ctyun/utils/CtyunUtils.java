package org.dromara.sms4j.ctyun.utils;

import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.alibaba.fastjson.JSONObject;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.ctyun.config.CtyunConfig;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CtyunUtils {

    /**
     * 获取签名时间戳
     */
    private static String signatureTime(){
        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        return timeFormat.format(new Date());
    }

    /**
     * 获取签名请求头
     */
    public static Map<String, String> signHeader(String body, String key, String secret){
        Map<String, String> map = new ConcurrentHashMap<>();

        // 构造时间戳
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date now = new Date();
        String signatureDate = dateFormat.format(now);
        String signatureTime = signatureTime();
        // 构造请求流水号
        String uuid = UUID.randomUUID().toString();

        String calculateContentHash = getSHA256(body);

        byte[] kTime = hmacSHA256(signatureTime.getBytes(), secret.getBytes());
        byte[] kAk = hmacSHA256(key.getBytes(), kTime);
        byte[] kDate = hmacSHA256(signatureDate.getBytes(), kAk);

        // 构造待签名字符串

        // 报文原封不动进行sha256摘要
        String signatureStr = String.format("ctyun-eop-request-id:%s\neop-date:%s\n", uuid, signatureTime) + "\n\n"  + calculateContentHash;
        // 构造签名
        String signature = Base64.getEncoder().encodeToString(hmacSHA256(signatureStr.getBytes(StandardCharsets.UTF_8), kDate));
        String signHeader =  String.format("%s Headers=ctyun-eop-request-id;eop-date Signature=%s", key, signature);
        map.put("Content-Type", "application/json;charset=UTF-8");
        map.put("ctyun-eop-request-id", uuid);
        map.put("Eop-date", signatureTime);
        map.put("Eop-Authorization", signHeader);
        return map;
    }

    /**
     * 生成请求body参数
     *
     * @param ctyunConfig 配置数据
     * @param phone         手机号
     * @param message       短信内容
     * @param templateId    模板id
     */
    public static String generateParamJsonStr(CtyunConfig ctyunConfig, String phone, String message, String templateId) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("action", ctyunConfig.getAction());
        paramMap.put("phoneNumber", phone);
        paramMap.put("signName", ctyunConfig.getSignature());
        paramMap.put("templateParam", message);
        paramMap.put("templateCode", templateId);
        return JSONObject.toJSONString(paramMap);
    }

    private static String toHex(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data) {
            String hex = Integer.toHexString(b);
            if (hex.length() == 1) {
                sb.append("0");
            } else if (hex.length() == 8) {
                hex = hex.substring(6);
            }
            sb.append(hex);
        }
        return sb.toString().toLowerCase(Locale.getDefault());
    }

    private static String getSHA256(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(text.getBytes(StandardCharsets.UTF_8));
            return toHex(md.digest());
        } catch (NoSuchAlgorithmException var3) {
            return null;
        }
    }

    private static byte[] hmacSHA256(byte[] data, byte[] key){
        try {
            HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, key);
            return hMac.digest(data);
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
    }
}
