package org.dromara.sms4j.api.proxy;

import java.util.Set;

/**
 * 限制拦截器仅针对哪些厂商生效，如果拦截器需要根据支持厂商加载，那可以实现此接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface SupplierSupportedMethodInterceptor extends SmsMethodInterceptor {

    /**
     * 获取支持的供应商名称
     *
     * @return 供应商名称
     */
    Set<String> getSupportedSuppliers();
}
