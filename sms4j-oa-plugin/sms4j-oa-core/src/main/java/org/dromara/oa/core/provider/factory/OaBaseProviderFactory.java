package org.dromara.oa.core.provider.factory;

import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.config.OaSupplierConfig;

public interface OaBaseProviderFactory<S extends OaSender, C extends OaSupplierConfig> {

    /**
     * 创建通知webhook实现对象
     *
     * @param c 通知webhook配置对象
     * @return 通知webhook实现对象
     */
    S createSmsOa(C c);

    /**
     * 获取配置类
     *
     * @return 配置类
     */
    Class<C> getConfigClass();

    /**
     * 获取供应商
     *
     * @return 供应商
     */
    String getSupplier();

}