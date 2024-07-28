package org.dromara.sms4j.baidu.utils;

import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.baidu.config.BaiduConfig;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.utils.SmsDateUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaiduUtils {

    /**
     * 创建前缀字符串
     * @param accessKeyId 访问密钥ID
     * @return bce-auth-v1/{accessKeyId}/{timestamp}/{expirationPeriodInSeconds }
     */
    private static String authStringPrefix(String accessKeyId){
        return "bce-auth-v1/" + accessKeyId + "/" + SmsDateUtils.utcGmt(new Date()) + "/1800";
    }

    /**
     * 创建规范请求
     * @param host Host域
     * @param action 接口名称
     * @param clientToken 幂等性参数
     * @return HTTP Method + "\n" + CanonicalURI + "\n" + CanonicalQueryString + "\n" + CanonicalHeaders
     */
    private static String canonicalRequest(String host, String action, String clientToken){
        return "POST\n" + canonicalURI(action) + "\n" + canonicalQueryString(clientToken) + "\n" + canonicalHeaders(host);
    }

    /**
     * Formatting the URL with signing protocol.
     * @param action URI
     * @return UriEncodeExceptSlash
     */
    private static String canonicalURI(String action){
        return URLEncodeUtil.encode(action, StandardCharsets.UTF_8);
    }

    /**
     * Formatting the query string with signing protocol.
     * @param clientToken 幂等性参数
     * @return String
     */
    private static String canonicalQueryString(String clientToken){
        if (StrUtil.isBlank(clientToken)) {
            return StrUtil.EMPTY;
        }
        return "clientToken=" + URLEncodeUtil.encode(clientToken, StandardCharsets.UTF_8);
    }

    /**
     * Formatting the headers from the request based on signing protocol.
     * @param host only host
     * @return String
     */
    private static String canonicalHeaders(String host){
        return URLEncodeUtil.encode("host", StandardCharsets.UTF_8) + ":" + URLEncodeUtil.encode(host, StandardCharsets.UTF_8);
    }

    /**
     * HMAC-SHA256-HEX
     * @param key 密钥
     * @param str 要加密的字符串
     * @return 小写形式的十六进制字符串
     */
    private static String sha256Hex(String key, String str) {
        HMac hMac = new HMac(HmacAlgorithm.HmacSHA256, key.getBytes(StandardCharsets.UTF_8));
        return hMac.digestHex(str, StandardCharsets.UTF_8);
    }

    /**
     * 构造 HTTP Headers请求头
     * @param config 百度智能云配置
     * @param clientToken 幂等性参数
     * @return Headers请求头
     */
    public static Map<String, String> buildHeaders(BaiduConfig config, String clientToken) {
        // 创建前缀字符串
        String authStringPrefix = authStringPrefix(config.getAccessKeyId());
        // 生成派生密钥
        String signingKey = sha256Hex(config.getAccessKeySecret(), authStringPrefix(config.getAccessKeyId()));
        // 生成签名摘要及认证字符串
        String signature = sha256Hex(signingKey, canonicalRequest(config.getHost(), config.getAction(), clientToken));
        // 认证字符串
        String authorization = authStringPrefix + "/" + "/" + signature;

        Map<String, String> headers = new HashMap<>(2);
        headers.put(Constant.AUTHORIZATION, authorization);
        headers.put("host", config.getHost());
        return headers;
    }

    /**
     * 构造 HTTP Body 请求体
     * @param mobile 手机号码 支持单个或多个手机号，多个手机号之间以英文逗号分隔
     * @param template 短信模板ID，模板申请成功后自动创建，全局内唯一
     * @param signatureId 短信签名ID，签名表申请成功后自动创建，全局内唯一
     * @param contentVar 模板变量内容，用于替换短信模板中定义的变量
     * @param custom 用户自定义参数，格式为字符串，状态回调时会回传该值
     * @param userExtId 通道自定义扩展码，上行回调时会回传该值，其格式为纯数字串。默认为不开通，请求时无需设置该参数。如需开通请联系SMS帮助申请
     * @return Body 请求体
     */
    public static Map<String, Object> buildBody(String mobile, String template, String signatureId,
                                                LinkedHashMap<String, String> contentVar, String custom, String userExtId) {
        Map<String, Object> body = new HashMap<>(4);
        body.put("mobile", mobile);
        body.put("template", template);
        body.put("signatureId", signatureId);
        body.put("contentVar", contentVar);
        if (StrUtil.isNotBlank(custom)){
            body.put("custom", custom);
        }
        if (StrUtil.isNotBlank(userExtId)){
            body.put("userExtId", userExtId);
        }
        return body;
    }
}
