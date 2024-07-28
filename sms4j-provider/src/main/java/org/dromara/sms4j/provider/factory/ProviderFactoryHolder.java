package org.dromara.sms4j.provider.factory;

import cn.hutool.core.collection.CollUtil;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 供应商工厂持有者
 *
 * @author xiaoyan
 * @since 3.0.0
 */
public class ProviderFactoryHolder {

    private static final Map<String, BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig>> FACTORIES = new ConcurrentHashMap<>();

    public static void registerFactory(BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> factory) {
        if(factory == null) {
            throw new SmsBlendException("注册供应商工厂失败，工厂实例不能为空");
        }
        FACTORIES.put(factory.getSupplier(), factory);
    }

    public static void registerFactory(List<BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig>> factoryList) {
        if(CollUtil.isEmpty(factoryList)) {
            return;
        }
        for(BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> factory : factoryList) {
            if(factory == null) {
                continue;
            }
            registerFactory(factory);
        }
    }

    public static BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> requireForSupplier(String supplier) {
        return FACTORIES.getOrDefault(supplier, null);
    }

}
