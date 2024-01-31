package org.dromara.sms4j.qiniu.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.qiniu.config.QiNiuConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.codec.digest.HmacUtils.hmacSha1;

/**
 * @author Administrator
 * @Date: 2024/1/30 16:37 50
 * @描述: QiNiuUtils
 **/
@Data
@Slf4j
public class QiNiuUtils {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String getSignature(String method, String url, QiNiuConfig qiNiuConfig, String body, String signDate) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, MalformedURLException {
        URI reqUrl = URI.create(url);
        StringBuilder dataToSign = new StringBuilder();
        dataToSign.append(method.toUpperCase()).append(" ").append(reqUrl.getPath());
        dataToSign.append("\nHost: ").append(reqUrl.getHost());
        dataToSign.append("\n").append("Content-Type").append(": ").append(Constant.ACCEPT);
        dataToSign.append("\n").append("X-Qiniu-Date").append(": ").append(signDate);
        dataToSign.append("\n\n");
        if (ObjectUtil.isNotEmpty(body)) {
            dataToSign.append(body);
        }
        Mac sha1Mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(qiNiuConfig.getAccessKeySecret().getBytes(StandardCharsets.UTF_8), HMAC_SHA1_ALGORITHM);
        sha1Mac.init(secretKeySpec);
        byte[] signData = sha1Mac.doFinal(dataToSign.toString().getBytes(StandardCharsets.UTF_8));
        String encodedSignature = Base64.getEncoder().encodeToString(signData);

        System.out.println("encodedSignature = " + encodedSignature);

        return "Qiniu " + qiNiuConfig.getAccessKeyId() + ":" + encodedSignature;
    }

    public static Map<String, String> getHeaderAndSign(String url, HashMap<String, Object> hashMap, QiNiuConfig qiNiuConfig) {
        String signature = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        String signDate = dateFormat.format(new Date());
        try {
            signature = getSignature("POST", url, qiNiuConfig, JSONUtil.toJsonStr(hashMap), signDate);
        } catch (Exception e) {
            log.error("签名失败", e);
            throw new SmsBlendException(e.getMessage());
        }

        //请求头
        Map<String, String> header = new HashMap<>();
        header.put("Authorization", signature);
        header.put("X-Qiniu-Date", signDate);
        header.put("Content-Type", "application/json");
        return header;
    }
}
