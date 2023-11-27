package org.dromara.oa.core.provider.config;

import lombok.Data;
import org.dromara.oa.comm.config.OaSupplierConfig;

@Data
public abstract class OaBaseConfig implements OaSupplierConfig {
    /**
     * 供应商
     */
    private String supplier;


    /**
     * 获取配置标识名(唯一)
     */
    private String configId;

    private String tokenId;

    private String sign;
    /**
     * 默认开启
     */

    private Boolean isEnable = true;

}

