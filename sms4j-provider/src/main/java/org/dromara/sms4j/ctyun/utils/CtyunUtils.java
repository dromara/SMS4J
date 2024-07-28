package org.dromara.sms4j.ctyun.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.utils.SmsDateUtils;
import org.dromara.sms4j.ctyun.config.CtyunConfig;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CtyunUtils {

    /**
     * 获取签名时间戳
     */
    private static String signatureTime(){
        return SmsDateUtils.pureDateUtcGmt(new Date());
    }

    /**
     * 获取签名请求头
     */
    public static Map<String, String> signHeader(String body, String key, String secret){
        Map<String, String> map = new ConcurrentHashMap<>(4);

        // 构造时间戳
        String signatureDate = SmsDateUtils.pureDateGmt(new Date());
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
        String signature = Base64.encode(hmacSHA256(signatureStr.getBytes(StandardCharsets.UTF_8), kDate));
        String signHeader =  String.format("%s Headers=ctyun-eop-request-id;eop-date Signature=%s", key, signature);
        map.put(Constant.CONTENT_TYPE, Constant.APPLICATION_JSON_UTF8);
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
        Map<String, String> paramMap = new HashMap<>(5);
        paramMap.put("action", ctyunConfig.getAction());
        paramMap.put("phoneNumber", phone);
        paramMap.put("signName", ctyunConfig.getSignature());
        paramMap.put("templateParam", message);
        paramMap.put("templateCode", templateId);
        return JSONUtil.toJsonStr(paramMap);
    }

    private static String getSHA256(String text) {
        return DigestUtil.sha256Hex(text);
    }

    private static byte[] hmacSHA256(byte[] data, byte[] key){
        return SecureUtil.hmacSha256(key).digest(data);
    }
}
