package org.dromara.oa.comm.config;

/**
 * @author dongfeng
 * 2023-10-19 13:36
 */
public interface OaSupplierConfig {

    /**
     * 获取配置标识名(唯一)
     */
    String getConfigId();

    /**
     * 获取供应商
     */
    String getSupplier();

    /**
     * 获取是否使用
     */
    Boolean getIsEnable();
}
