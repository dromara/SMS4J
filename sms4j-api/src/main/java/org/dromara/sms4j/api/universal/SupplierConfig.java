package org.dromara.sms4j.api.universal;

/**
 * SupplierConfig
 * <p> 标定配置类的额外类型
 * @author :Wind
 * 2023/5/16  15:14
 **/
public interface SupplierConfig {

    /**
     * 获取配置标识名
     *
     * @since 3.0.0
     */
    String getConfigId();

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    String getSupplier();

    /**
     * 获取代理配置
     *
     */
    ProxyConfig getProxy();
}
