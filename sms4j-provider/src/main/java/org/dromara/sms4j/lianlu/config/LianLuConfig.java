package org.dromara.sms4j.lianlu.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.lianlu.req.LianLuRequest;
import org.dromara.sms4j.lianlu.utils.LianLuUtils;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * 联麓短信：
 * <a href="https://console.shlianlu.com/#/document/smsDoc">官方文档</a>
 *
 * @author lym
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class LianLuConfig extends BaseConfig {
    /**
     * 企业ID
     */
    private String mchId;

    private String version = "1.1.0";

    private String appKey;

    private String appId;

    /**
     * 加密方式
     * 仅支持HMACSHA256和MD5
     */
    private String signType = LianLuUtils.SIGN_TYPE_MD5;

    private String requestUrl = "https://apis.shlianlu.com/sms/trade";

    /**
     * 短信签名
     * 发送普通短信时需使用
     * 不填会使用平台上设置的默认签名
     */
    private String signName;

    @Override
    public String getSupplier() {
        return SupplierConstant.LIANLU;
    }

    public LianLuRequest toLianLuRequest() {
        return new LianLuRequest()
                .setTemplateId(getTemplateId())
                .setAppId(getAppId())
                .setMchId(getMchId())
                .setVersion(getVersion())
                .setSignType(getSignType())
                .setSignName(getSignName());
    }
}
