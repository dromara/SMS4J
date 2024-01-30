package org.dromara.sms4j.qiniu.util;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.HMac;
import lombok.Data;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.qiniu.config.QiNiuConfig;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;

import static org.apache.commons.codec.digest.HmacUtils.hmacSha1;

/**
 * @author Administrator
 * @Date: 2024/1/30 16:37 50
 * @描述: QiNiuUtils
 **/
@Data
public class QiNiuUtils {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    public static String getSignature(String method, String url, QiNiuConfig qiNiuConfig, String body) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException, MalformedURLException {
        URL reqUrl = new URL(url);
        StringBuilder dataToSign = new StringBuilder();
        dataToSign.append(method.toUpperCase()).append(" ").append(reqUrl.getPath()).append('\n');
        dataToSign.append("Host: ").append(reqUrl.getHost()).append('\n');
        dataToSign.append("Content-Type: ").append(Constant.ACCEPT).append("\n").append("\n");
        if (body != null && !body.isEmpty()) {
            dataToSign.append(body);
        }

        System.out.println(dataToSign.toString());

        Mac sha1Mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(qiNiuConfig.getAccessKeySecret().getBytes(StandardCharsets.UTF_8), HMAC_SHA1_ALGORITHM);
        sha1Mac.init(secretKeySpec);
        byte[] signData = sha1Mac.doFinal(dataToSign.toString().getBytes(StandardCharsets.UTF_8));
        String encodedSignature = Base64.getEncoder().encodeToString(signData);

        System.out.println("Qiniu " + qiNiuConfig.getAccessKeyId() + ":" + encodedSignature);

        return "Qiniu " + qiNiuConfig.getAccessKeyId() + ":" + encodedSignature;
    }

}
