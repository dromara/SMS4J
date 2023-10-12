package org.dromara.sms4j.lianlu.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Collection;
import java.util.TreeMap;

/**
 * <a href="https://console.shlianlu.com/#/document/api_4_2">普通短信</a>
 * <a href="https://console.shlianlu.com/#/document/api_4_4">模板短信</a>
 */
@Data
@Accessors(chain = true)
public class LianLuRequest {
    /**
     * 企业ID
     */
    private String mchId;

    /**
     * 短信类型
     */
    private String type;

    /**
     * 模板变量内容
     * 按顺序填写
     */
    private Collection<String> templateParamSet;

    /**
     * 接收短信的手机号码数组，上限为10000
     */
    private Collection<String> phoneNumberSet;

    private String appId;

    private String version;

    /**
     * 数字签名
     * @see org.dromara.sms4j.lianlu.utils.LianLuUtils#generateSignature
     */
    private String signature;

    /**
     * 加密方式
     * 仅支持HMACSHA256和MD5
     */
    private String signType;

    private Long timeStamp;

    /**
     * 模版id
     */
    private String templateId;

    /**
     * 短信内容
     */
    private String sessionContext;

    /**
     * 短信签名
     */
    private String signName;

    /**
     * 联麓签名机制要求参数按英文字母顺序排序,因此使用treeMap
     * @return
     */
    public TreeMap<String, Object> toMap() {
        TreeMap<String, Object> ret = new TreeMap<>();
        ret.put("MchId", mchId);
        ret.put("Type", type);
        ret.put("TemplateParamSet", templateParamSet);
        ret.put("PhoneNumberSet", phoneNumberSet);
        ret.put("AppId", appId);
        ret.put("Version", version);
        ret.put("Signature", signature);
        ret.put("SignType", signType);
        ret.put("TimeStamp", timeStamp);
        ret.put("TemplateId", templateId);
        ret.put("SessionContext", sessionContext);
        ret.put("SignName", signName);
        return ret;
    }
}
