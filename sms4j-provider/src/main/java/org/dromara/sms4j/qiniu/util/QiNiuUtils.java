package org.dromara.sms4j.qiniu.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.qiniu.config.QiNiuConfig;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * @author YYM
 * @Date: 2024/1/30 16:37 50
 * @描述: QiNiuUtils
 **/
@Data
@Slf4j
public class QiNiuUtils {

    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String getSignature(String method, String url, QiNiuConfig qiNiuConfig, String body, String signDate) {
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
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA1, qiNiuConfig.getAccessKeySecret().getBytes(StandardCharsets.UTF_8));
        byte[] signData = hMac.digest(dataToSign.toString().getBytes(StandardCharsets.UTF_8));
        String encodedSignature = Base64.getEncoder().encodeToString(signData);

        return "Qiniu " + qiNiuConfig.getAccessKeyId() + ":" + encodedSignature;
    }

    public static Map<String, String> getHeaderAndSign(String url, HashMap<String, Object> hashMap, QiNiuConfig qiNiuConfig) {
        String signature;
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
        Map<String, String> header = new HashMap<>(3);
        header.put("Authorization", signature);
        header.put("X-Qiniu-Date", signDate);
        header.put("Content-Type", "application/json");
        return header;
    }
}
