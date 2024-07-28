package org.dromara.sms4j.cloopen.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * 容联云短信配置属性
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CloopenConfig extends BaseConfig {

    /**
     * REST API Base URL
     */
    private String baseUrl = Constant.HTTPS_PREFIX + "app.cloopen.com:8883/2013-12-26";

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.CLOOPEN;
    }
}
