package org.dromara.sms4j.provider.factory;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * 供应商工厂持有者
 *
 * @author xiaoyan
 * @since 3.0.0
 */
public class ProviderFactoryHolder {

    private static final Set<BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig>> factories = new HashSet<>();

    public static void registerFactory(BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> factory) {
        factories.add(factory);
    }

    public static BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> requireForSupplier(String supplier) {
        return factories.stream()
                .filter(f -> f.getSupplier().equals(supplier))
                .findFirst()
                .orElse(null);
    }

}
