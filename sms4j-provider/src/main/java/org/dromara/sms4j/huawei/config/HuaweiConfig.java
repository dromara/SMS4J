package org.dromara.sms4j.huawei.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

@Data
@EqualsAndHashCode(callSuper = true)
public class HuaweiConfig extends BaseConfig {
    /** 国内短信签名通道号*/
    private String sender;
    /** 短信状态报告接收地*/
    private String statusCallBack;
    /** APP接入地址*/
    private String url;

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.HUAWEI;
    }

}
