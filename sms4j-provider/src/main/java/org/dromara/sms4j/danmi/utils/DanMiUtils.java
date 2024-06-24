package org.dromara.sms4j.danmi.utils;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.danmi.config.DanMiConfig;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>类名: DanMiUtils
 *
 * @author :bleachtred
 * 2024/6/23  17:06
 **/
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DanMiUtils {

    public static LinkedHashMap<String, String> buildHeaders(){
        LinkedHashMap<String, String> headers = new LinkedHashMap<>(1);
        headers.put(Constant.CONTENT_TYPE, Constant.APPLICATION_JSON);
        return headers;
    }

    /**
     * 生成请求body参数
     *
     * @param config        配置数据
     * @param phones        手机号
     * @param message       短信内容 (or 验证码内容(1-8位数字) or 语音文件ID or 模板参数)
     * @param templateId    模板id
     */
    public static LinkedHashMap<String, Object> buildBody(DanMiConfig config, List<String> phones, String message, String templateId) {
        LinkedHashMap<String, Object> body = new LinkedHashMap<>();
        body.put("respDataType", "JSON");
        body.put("accountSid", config.getAccessKeyId());
        switch (config.getAction()){
            case "distributor/sendSMS":
                if (StrUtil.isAllBlank(message, templateId)){
                    log.error("message and templateId can not be empty at the same time");
                    throw new SmsBlendException("message and templateId can not be empty at the same time");
                }
                if (StrUtil.isNotBlank(templateId)){
                    body.put("templateid", templateId);
                }
                if (StrUtil.isNotBlank(message)){
                    body.put("smsContent", URLEncodeUtil.encode(message));
                }
                if (CollUtil.isEmpty(phones)){
                    log.error("phones can not be empty");
                    throw new SmsBlendException("phones can not be empty");
                }
                body.put("to", SmsUtils.addCodePrefixIfNot(phones));
                break;
            case "distributor/user/query":
                break;
            case "voice/voiceCode":
                if (CollUtil.isEmpty(phones) || phones.size() != 1){
                    log.error("called can not be empty or phone must be only one");
                    throw new SmsBlendException("called can not be empty or phone must be only one");
                }
                body.put("called", SmsUtils.addCodePrefixIfNot(phones.get(0)));
                if (StrUtil.isBlank(message)){
                    log.error("verifyCode can not be empty");
                    throw new SmsBlendException("verifyCode can not be empty");
                }
                body.put("verifyCode", message);
                break;
            case "voice/voiceNotify":
                if (CollUtil.isEmpty(phones) || phones.size() != 1){
                    log.error("called can not be empty or phone must be only one");
                    throw new SmsBlendException("called can not be empty or phone must be only one");
                }
                body.put("called", SmsUtils.addCodePrefixIfNot(phones.get(0)));
                if (StrUtil.isBlank(message)){
                    log.error("notifyFileId can not be empty");
                    throw new SmsBlendException("notifyFileId can not be empty");
                }
                body.put("notifyFileId", message);
                break;
            case "voice/voiceTemplate":
                if (CollUtil.isEmpty(phones) || phones.size() != 1){
                    log.error("called can not be empty or phone must be only one");
                    throw new SmsBlendException("called can not be empty or phone must be only one");
                }
                body.put("called", SmsUtils.addCodePrefixIfNot(phones.get(0)));
                if (StrUtil.isEmpty(templateId)){
                    log.error("templateId can not be empty");
                    throw new SmsBlendException("templateId can not be empty");
                }
                body.put("templateId", templateId);
                if (StrUtil.isEmpty(message)){
                    log.error("param can not be empty");
                    throw new SmsBlendException("param can not be empty");
                }
                body.put("param", message);
                break;
            default:
                log.error("action not found");
                throw new SmsBlendException("action not found");
        }
        long timestamp = System.currentTimeMillis();
        body.put("timestamp", timestamp);
        body.put("sig", sign(config.getAccessKeyId(), config.getAccessKeySecret(), timestamp));
        return body;
    }

    /**
     * 签名：MD5(ACCOUNT SID + AUTH TOKEN + timestamp)。共32位（小写）
     * @param accessKeyId ACCOUNT SID
     * @param accessKeySecret AUTH TOKEN
     * @param timestamp timestamp
     * @return 签名：MD5 共32位（小写）
     */
    private static String sign(String accessKeyId, String accessKeySecret, long timestamp){
        return DigestUtil.md5Hex(accessKeyId + accessKeySecret + timestamp);
    }
}
