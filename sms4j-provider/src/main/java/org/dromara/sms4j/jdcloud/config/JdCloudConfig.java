package org.dromara.sms4j.jdcloud.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * 京东云短信配置属性
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class JdCloudConfig extends BaseConfig {

    /**
     * 地域信息
     */
    private String region = "cn-north-1";

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.JDCLOUD;
    }

}
