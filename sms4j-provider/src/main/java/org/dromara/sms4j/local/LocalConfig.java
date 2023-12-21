package org.dromara.sms4j.local;

import lombok.Getter;
import lombok.Setter;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.constant.SupplierConstant;

/**
 * 用于测试的{@link SupplierConfig}实现
 *
 * @author huangchengxing
 * @see SupplierConstant#LOCAL
 */
@Setter
@Getter
public class LocalConfig implements SupplierConfig {

    private String configId = SupplierConstant.LOCAL;
    private String supplier = SupplierConstant.LOCAL;
}
