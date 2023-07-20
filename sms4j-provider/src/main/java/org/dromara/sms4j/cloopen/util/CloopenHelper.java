package org.dromara.sms4j.cloopen.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.cloopen.config.CloopenConfig;

import java.util.Date;
import java.util.Map;

/**
 * 容联云 Helper
 *
 * @author Charles7c
 * @since 2023/4/17 20:57
 */
public class CloopenHelper {

    private final CloopenConfig config;

    public CloopenHelper(CloopenConfig config) {
        this.config = config;
    }

    public SmsResponse smsResponse(Map<String, Object> paramMap){
        String timestamp = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);

        String url = String.format("%s/Accounts/%s/SMS/TemplateSMS?sig=%s",
                config.getBaseUrl(),
                config.getAccessKeyId(),
                this.generateSign(config.getAccessKeyId(), config.getAccessKeySecret(), timestamp));

        try(HttpResponse response = HttpRequest.post(url)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json;charset=utf-8")
                .header("Authorization", this.generateAuthorization(config.getAccessKeyId(), timestamp))
                .body(JSONUtil.toJsonStr(paramMap))
                .execute()){
            JSONObject body = JSONUtil.parseObj(response.body());
            return this.getResponse(body);
        }
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess("000000".equals(resJson.getStr("statusCode")));
        smsResponse.setData(resJson);
        smsResponse.setConfigId(this.config.getConfigId());
        return smsResponse;
    }

    /**
     * 生成签名
     * <p>
     * 1.使用 MD5 加密（账户 Id + 账户授权令牌 + 时间戳）。其中账户 Id 和账户授权令牌根据 url 的验证级别对应主账户。<br>
     * 时间戳是当前系统时间，格式 "yyyyMMddHHmmss"。时间戳有效时间为 24 小时，如：20140416142030 <br>
     * 2.参数需要大写
     * </p>
     *
     * @param accessKeyId     /
     * @param accessKeySecret /
     * @param timestamp       时间戳
     * @return 签名
     */
    private String generateSign(String accessKeyId, String accessKeySecret, String timestamp) {
        return SecureUtil.md5(accessKeyId + accessKeySecret + timestamp).toUpperCase();
    }

    /**
     * 生成验证信息
     * <p>
     * 1.使用 Base64 编码（账户 Id + 冒号 + 时间戳）其中账户 Id 根据 url 的验证级别对应主账户<br>
     * 2.冒号为英文冒号<br>
     * 3.时间戳是当前系统时间，格式 "yyyyMMddHHmmss"，需与签名中时间戳相同。
     * </p>
     *
     * @param accessKeyId /
     * @param timestamp   时间戳
     * @return 验证信息
     */
    private String generateAuthorization(String accessKeyId, String timestamp) {
        return Base64.encode(accessKeyId + ":" + timestamp);
    }
}
