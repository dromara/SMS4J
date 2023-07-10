package org.dromara.sms4j.cloopen.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.SecureUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 容联云 Helper
 *
 * @author Charles7c
 * @since 2023/4/17 20:57
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CloopenHelper {

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
    public static String generateSign(String accessKeyId, String accessKeySecret, String timestamp) {
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
    public static String generateAuthorization(String accessKeyId, String timestamp) {
        return Base64.encode(accessKeyId + ":" + timestamp);
    }
}
