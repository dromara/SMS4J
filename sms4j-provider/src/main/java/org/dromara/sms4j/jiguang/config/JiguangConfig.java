package org.dromara.sms4j.jiguang.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

@Data
@EqualsAndHashCode(callSuper = true)
public class JiguangConfig extends BaseConfig {
    /**
     * appKey
     */
    private String appKey;
    /**
     * masterSecret
     */
    private String masterSecret;
    /**
     * signid
     */
    private String signId;
    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return "jiguang";
    }
}
