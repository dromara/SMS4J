package org.dromara.sms4j.chuanglan.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * @author YYM
 * @Date: 2024/1/31 17:56 44
 * @描述: ChuangLanConfig
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ChuangLanConfig extends BaseConfig {
    /**
     * 基础路径
     */
    private String baseUrl = "https://smssh1.253.com/msg";
    /**
     * 短信发送路径
     */
    private String msgUrl = "/variable/json";

    @Override
    public String getSupplier() {
        return SupplierConstant.CHUANGLAN;
    }
}