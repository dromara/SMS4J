package org.dromara.sms4j.tencent.utils;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.utils.SmsDateUtils;
import org.dromara.sms4j.tencent.config.TencentConfig;

import javax.xml.bind.DatatypeConverter;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Richard
 * @date 2023-04-18 19:50
 */
@Slf4j
public class TencentUtils {
    /**
     * 加密方式
     */
    private static final String ALGORITHM = "TC3-HMAC-SHA256";
    /**
     * 请求方式
     */
    private static final String HTTP_REQUEST_METHOD = "POST";


    private static byte[] hmac256(byte[] key, String msg) {
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, key);
        return hMac.digest(msg);
    }

    private static String sha256Hex(String s) throws Exception {
        return DatatypeConverter.printHexBinary(DigestUtil.sha256(s)).toLowerCase();
    }

    /**
     * 生成腾讯云发送短信接口签名
     *
     * @param templateId 模板id
     * @param messages   短信内容
     * @param phones     手机号
     * @param timestamp  时间戳
     * @throws Exception Exception
     */
    public static String generateSignature(TencentConfig tencentConfig, String templateId, String[] messages, String[] phones,
                                           String timestamp) throws Exception {
        String date = SmsDateUtils.normDateGmt(new Date(Long.parseLong(timestamp + "000")));
        String canonicalUri = "/";
        String canonicalQueryString = "";
        String canonicalHeaders = "content-type:application/json; charset=utf-8\nhost:" + tencentConfig.getRequestUrl() + "\n";
        String signedHeaders = "content-type;host";
        Map<String, Object> params = new HashMap<>();
        params.put("PhoneNumberSet", phones);
        params.put("SmsSdkAppId", tencentConfig.getSdkAppId());
        params.put("SignName", tencentConfig.getSignature());
        params.put("TemplateId", templateId);
        params.put("TemplateParamSet", messages);
        String payload = JSONUtil.toJsonStr(params);
        String hashedRequestPayload = sha256Hex(payload);
        String canonicalRequest = HTTP_REQUEST_METHOD + "\n" + canonicalUri + "\n" + canonicalQueryString + "\n" + canonicalHeaders + "\n" + signedHeaders + "\n" + hashedRequestPayload;
        String credentialScope = date + "/" + tencentConfig.getService() + "/tc3_request";
        String hashedCanonicalRequest = sha256Hex(canonicalRequest);
        String stringToSign = ALGORITHM + "\n" + timestamp + "\n" + credentialScope + "\n" + hashedCanonicalRequest;
        byte[] secretDate = hmac256(("TC3" + tencentConfig.getAccessKeySecret()).getBytes(StandardCharsets.UTF_8), date);
        byte[] secretService = hmac256(secretDate, tencentConfig.getService());
        byte[] secretSigning = hmac256(secretService, "tc3_request");
        String signature = DatatypeConverter.printHexBinary(hmac256(secretSigning, stringToSign)).toLowerCase();
        return ALGORITHM + " Credential=" + tencentConfig.getAccessKeyId() + "/" + credentialScope + ", SignedHeaders=" + signedHeaders + ", Signature=" + signature;
    }

    /**
     * 生成腾讯云短信请求头map
     *
     * @param authorization 签名信息
     * @param timestamp     时间戳
     * @param action        接口名称
     * @param version       接口版本
     * @param territory     服务器地区
     * @param requestUrl    请求地址
     */
    public static Map<String, String> generateHeadsMap(String authorization, String timestamp, String action,
                                                       String version, String territory, String requestUrl) {
        Map<String, String> headers = new HashMap<>();
        headers.put(Constant.AUTHORIZATION, authorization);
        headers.put(Constant.CONTENT_TYPE, Constant.APPLICATION_JSON_UTF8);
        headers.put("Host", requestUrl);
        headers.put("X-TC-Action", action);
        headers.put("X-TC-Timestamp", timestamp);
        headers.put("X-TC-Version", version);
        headers.put("X-TC-Region", territory);
        return headers;
    }

    /**
     * 生成腾讯云短信请求body
     *
     * @param phones           手机号
     * @param sdkAppId         appid
     * @param signatureName    短信签名
     * @param templateId       模板id
     * @param templateParamSet 模板参数
     * @return Map
     */
    public static Map<String, Object> generateRequestBody(String[] phones, String sdkAppId, String signatureName,
                                                          String templateId, String[] templateParamSet) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("PhoneNumberSet", phones);
        requestBody.put("SmsSdkAppId", sdkAppId);
        requestBody.put("SignName", signatureName);
        requestBody.put("TemplateId", templateId);
        requestBody.put("TemplateParamSet", templateParamSet);
        return requestBody;
    }

}