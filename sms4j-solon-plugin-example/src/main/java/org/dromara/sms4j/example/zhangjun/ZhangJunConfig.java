package org.dromara.sms4j.example.zhangjun;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * @author 4n
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ZhangJunConfig extends BaseConfig {
    private String appId;
    private String sid;
    private String url;

    @Override
    public String getSupplier() {
        return "zhangjun";
    }

}
