package org.dromara.sms4j.lianlu.utils;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class LianLuUtils {
    private static final Set<String> IGNORE_FILED_SET = new HashSet<>();

    public static final String SIGN_TYPE_MD5 = "MD5";
    public static final String SIGN_TYPE_HMACSHA256 = "HMACSHA256";

    static {
        IGNORE_FILED_SET.add("Signature");
        IGNORE_FILED_SET.add("PhoneList");
        IGNORE_FILED_SET.add("phoneSet");
        IGNORE_FILED_SET.add("PhoneNumberSet");
        IGNORE_FILED_SET.add("TemplateParamSet");
        IGNORE_FILED_SET.add("SessionContext");
        IGNORE_FILED_SET.add("SessionContextSet");
        IGNORE_FILED_SET.add("ContextParamSet");
    }

    /**
     * 生成签名. 注意，若含有sign_type字段，必须和signType参数保持一致。
     * <a href="https://console.shlianlu.com/#/document/Signature">签名机制</a>
     *
     * @param data     待签名数据 联麓签名机制要求参数按英文字母顺序排序,因此使用treeMap
     * @param key      API密钥
     * @param signType 签名方式
     * @return 签名
     */
    public static String generateSignature(TreeMap<String, Object> data, String key, String signType) {
        Set<String> keySet = data.keySet();
        StringBuilder sb = new StringBuilder();
        for (String k : keySet) {
            if (IGNORE_FILED_SET.contains(k)) {
                continue;
            }
            Object v = data.get(k);
            if (v == null) {
                continue;
            }
            String value = v.toString().trim();
            if (StrUtil.isNotBlank(value)) {
                sb.append(k).append("=").append(value).append("&");
            }
        }
        sb.append("key=").append(key);
        if (SIGN_TYPE_HMACSHA256.equals(signType)) {
            return SecureUtil.hmacSha256(key).digestHex(sb.toString(), StandardCharsets.UTF_8).toUpperCase();
        } else if (SIGN_TYPE_MD5.equals(signType)) {
            return SecureUtil.md5(sb.toString()).toUpperCase();
        } else {
            throw new IllegalArgumentException("不支持的加密方式:" + signType);
        }
    }

}
