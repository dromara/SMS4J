package org.dromara.sms4j.netease.config;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.config.BaseConfig;

/**
 * @author adam
 */
@Data
@SuperBuilder
public class NeteaseConfig extends BaseConfig implements SupplierConfig {

    /**
     * 模板变量名称
     */
    private String templateId;

    /**
     * appKey
     */
    private String appKey;

    /**
     * secret
     */
    private String secret;

    /**
     * 模板短信请求地址
     */
    @Builder.Default
    private String templateUrl = "https://api.netease.im/sms/sendtemplate.action";


    /**
     * 验证码短信请求地址
     */
    @Builder.Default
    private String codeUrl = "https://api.netease.im/sms/sendcode.action";

    /**
     * 验证码验证请求地址
     */
    @Builder.Default
    private String verifyUrl = "https://api.netease.im/sms/verifycode.action";

    /**
     * 是否需要支持短信上行。true:需要，false:不需要
     * 说明：如果开通了短信上行抄送功能，该参数需要设置为true，其它情况设置无效
     */
    private final Boolean needUp;

}
