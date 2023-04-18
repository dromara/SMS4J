package org.dromara.sms4j.cloopen.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.json.JSONUtil;
import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * 容联云 Helper
 *
 * @author Charles7c
 * @since 2023/4/17 20:57
 */
public class CloopenHelper {

    private final CloopenConfig cloopenConfig;

    public CloopenHelper(CloopenConfig cloopenConfig) {
        this.cloopenConfig = cloopenConfig;
    }

    /**
     * 发起 REST 请求
     *
     * @param restApiFunction REST API 函数式接口
     * @param paramMap        请求参数
     * @param <R>             响应类型
     * @return 响应信息
     */
    public <R> SmsResponse request(CloopenRestApiFunction<Map<String, Object>, R> restApiFunction, Map<String, Object> paramMap) {
        SmsResponse smsResponse = new SmsResponse();
        try {
            String timestamp = DateUtil.format(new Date(), DatePattern.PURE_DATETIME_PATTERN);
            // 设置全局变量
            ForestConfiguration forestConfiguration = Forest.config();
            forestConfiguration.setVariableValue("baseUrl", (method) -> cloopenConfig.getBaseUrl());
            forestConfiguration.setVariableValue("accessKeyId", (method) -> cloopenConfig.getAccessKeyId());
            forestConfiguration.setVariableValue("sign", this.generateSign(cloopenConfig.getAccessKeyId(), cloopenConfig.getAccessKeySecret(), timestamp));
            forestConfiguration.setVariableValue("authorization", this.generateAuthorization(cloopenConfig.getAccessKeyId(), timestamp));

            // 调用请求
            R response = restApiFunction.apply(paramMap);

            // 解析结果
            Map<String, Object> responseMap = Optional.ofNullable(response)
                    .map(JSONUtil::parseObj)
                    .map(obj -> (Map<String, Object>) obj)
                    .orElse(Collections.emptyMap());
            String statusCode = Convert.toStr(responseMap.get("statusCode"));
            String statusMsg = Convert.toStr(responseMap.get("statusMsg"));
            boolean isSuccess = "000000".equals(statusCode);
            if (isSuccess) {
                smsResponse.setCode(statusCode);
                smsResponse.setMessage(statusMsg);
                smsResponse.setData(response);
                Object bizId = JSONUtil.getByPath(JSONUtil.parse(responseMap), "templateSMS.smsMessageSid");
                smsResponse.setBizId(Convert.toStr(bizId));
            } else {
                smsResponse.setErrMessage(statusMsg);
                smsResponse.setErrorCode(statusCode);
            }
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
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
