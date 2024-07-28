package org.dromara.sms4j.budingyun.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * BudingV2Config
 * <p> 布丁云V2短信配置
 *
 * @author NicholaslD
 * @date 2024/03/21 12:00
 * */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class BudingV2Config extends BaseConfig {

    /**
     * 签名密钥
     * 就是发短信的时候的签名，比如：【布丁云】
     */
    private String signKey;

    /**
     * 变量列表
     * 用于替换短信模板中的变量
     */
    private String[] args;

    /**
     * 获取供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.BUDING_V2;
    }
}
