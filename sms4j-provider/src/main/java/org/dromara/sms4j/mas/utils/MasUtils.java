package org.dromara.sms4j.mas.utils;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.json.JSONUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.mas.config.MasConfig;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MasUtils {

    public static String base64Code(MasConfig config, String phone, String message, String templateId) {
        Map<String, String> map = new HashMap<>(1);
        StringBuilder sb = new StringBuilder();
        if (StrUtil.isNotEmpty(config.getEcName())){
            map.put("ecName", config.getEcName().trim());
            sb.append(config.getEcName().trim());
        }
        if (StrUtil.isNotEmpty(config.getSdkAppId())){
            map.put("apId", config.getSdkAppId().trim());
            sb.append(config.getSdkAppId().trim());
        }
        if (StrUtil.isNotEmpty(config.getAccessKeySecret())){
            map.put("secretKey", config.getAccessKeySecret().trim());
            sb.append(config.getAccessKeySecret().trim());
        }
        if ("norsubmit".equals(config.getAction()) || "submit".equals(config.getAction())){
            if (StrUtil.isNotEmpty(phone)){
                map.put("mobiles", phone.trim());
                sb.append(phone.trim());
            }
            if (StrUtil.isNotEmpty(message)){
                map.put("content", message.trim());
                sb.append(message.trim());
            }
        }else if ("tmpsubmit".equals(config.getAction())){
            if (StrUtil.isNotEmpty(templateId)){
                sb.append(templateId.trim());
                map.put("templateId", templateId);
            }
            if (StrUtil.isNotEmpty(phone)){
                map.put("mobiles", phone.trim());
                sb.append(phone.trim());
            }
            if (StrUtil.isNotEmpty(message)){
                map.put("params", message.trim());
                sb.append(message.trim());
            }else {
                String emptyParams = JSONUtil.toJsonStr(new String[]{""});
                map.put("params", emptyParams);
                sb.append(emptyParams);
            }
        }

        if (StrUtil.isNotEmpty(config.getSignature())){
            map.put("sign", config.getSignature().trim());
            sb.append(config.getSignature().trim());
        }
        if (StrUtil.isNotEmpty(config.getAddSerial())){
            map.put("addSerial", config.getAddSerial().trim());
            sb.append(config.getAddSerial().trim());
        }else {
            map.put("addSerial", "");
        }

        map.put("mac", DigestUtil.md5Hex(sb.toString(), StandardCharsets.UTF_8));
        return Base64.encode(JSONUtil.toJsonStr(map), StandardCharsets.UTF_8);
    }
}
