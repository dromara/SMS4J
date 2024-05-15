package sms4j.local.c;


import lombok.Data;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * 新增厂商的配置实现{@link SupplierConfig}
 * 这个类中还可以添加厂商携带的其他属性
 *
 * @author huangchengxing
 */

@Data
public class CConfig extends BaseConfig {

    private String supplier = "c";
    //其他属性
    private String qi2Ta1Shu3Xing4;

    @Override
    public String getSupplier() {
        return supplier;
    }
}
