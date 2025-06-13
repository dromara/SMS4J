package org.dromara.sms4j.montnets.config;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * @author SU
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MontnetsConfig extends BaseConfig {

    /**
     * 请求地址
     */
    private String url;
    /**
     * 接口名称
     */
    private String api;
    /**
     * 模板code
     */
    private String templateId;
    /**
     * 模板变量名称
     */
    private String templateParam;

    /**
     * 获取供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.MONTNETS;
    }

}
