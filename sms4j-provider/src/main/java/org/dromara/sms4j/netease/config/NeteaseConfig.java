package org.dromara.sms4j.netease.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.netease.service.NeteaseSmsImpl;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * @author adam
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
public class NeteaseConfig extends BaseConfig {

    /**
     * 模板变量名称
     */
    private String templateName;

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
    private Boolean needUp;

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return NeteaseSmsImpl.SUPPLIER;
    }

}
