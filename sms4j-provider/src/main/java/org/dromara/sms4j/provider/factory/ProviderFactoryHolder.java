package org.dromara.sms4j.provider.factory;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 供应商工厂持有者
 *
 * @author xiaoyan
 * @since 3.0.0
 */
public class ProviderFactoryHolder {

    private static final Map<String, BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig>> factories = new HashMap<>();

    public static void registerFactory(BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> factory) {
        factories.put(factory.getSupplier(), factory);
    }

    public static BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> requireForSupplier(String supplier) {
        return factories.getOrDefault(supplier, null);
    }

}
