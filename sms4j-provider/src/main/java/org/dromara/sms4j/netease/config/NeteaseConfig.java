package org.dromara.sms4j.netease.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * 网易云信
 * @author adam
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NeteaseConfig extends BaseConfig {

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * 模板短信请求地址
     */
    private String templateUrl = Constant.HTTPS_PREFIX + "api.netease.im/sms/sendtemplate.action";

    /**
     * 验证码短信请求地址
     */
    private String codeUrl = Constant.HTTPS_PREFIX + "api.netease.im/sms/sendcode.action";

    /**
     * 验证码验证请求地址
     */
    private String verifyUrl = Constant.HTTPS_PREFIX + "api.netease.im/sms/verifycode.action";

    /**
     * 是否需要支持短信上行。true:需要，false:不需要
     * 说明：如果开通了短信上行抄送功能，该参数需要设置为true，其它情况设置无效
     */
    private Boolean needUp = false;

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.NETEASE;
    }

}
