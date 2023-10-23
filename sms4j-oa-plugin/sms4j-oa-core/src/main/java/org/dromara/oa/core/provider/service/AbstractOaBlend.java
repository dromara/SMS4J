package org.dromara.oa.core.provider.service;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.config.OaSupplierConfig;

/**
 * @author dongfeng
 * @date 2023-10-22 21:03
 */
public abstract class AbstractOaBlend<C extends OaSupplierConfig> implements OaSender {

    @Getter
    private final String configId;

    private final C config;

    protected AbstractOaBlend(C config) {
        this.configId = StrUtil.isEmpty(config.getConfigId()) ? getSupplier() : config.getConfigId();
        this.config = config;
    }

    protected C getConfig() {
        return config;
    }
}