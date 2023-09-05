package org.dromara.sms4j.emay.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.emay.service.EmaySmsImpl;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * @author Richard
 * @date 2023-04-11 12:00
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmayConfig extends BaseConfig {

    /** APP接入地址*/
    private String requestUrl;

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.EMAY;
    }

}
