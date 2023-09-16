package org.dromara.sms4j.unisms.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

@Data
@EqualsAndHashCode(callSuper = true)
public class UniConfig extends BaseConfig {

    /**
     * 是否为简易模式
     */
    private Boolean isSimple = true;

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.UNISMS;
    }

}
