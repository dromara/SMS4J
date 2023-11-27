package org.dromara.oa.core.provider.factory;

import cn.hutool.core.collection.CollUtil;
import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.config.OaSupplierConfig;
import org.dromara.oa.comm.errors.OaException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author dongfeng
 * 2023-10-22 21:12
 */
public class ProviderFactoryHolder {


    private static final Map<String, OaBaseProviderFactory<? extends OaSender, ? extends OaSupplierConfig>> factories = new ConcurrentHashMap<>();

    public static void registerFactory(OaBaseProviderFactory<? extends OaSender, ? extends OaSupplierConfig> factory) {
        if (factory == null) {
            throw new OaException("注册供应商工厂失败，工厂实例不能为空");
        }
        factories.put(factory.getSupplier(), factory);
    }

    public static void registerFactory(List<OaBaseProviderFactory<? extends OaSender, ? extends OaSupplierConfig>> factoryList) {
        if (CollUtil.isEmpty(factoryList)) {
            return;
        }
        for (OaBaseProviderFactory<? extends OaSender, ? extends OaSupplierConfig> factory : factoryList) {
            if (factory == null) {
                continue;
            }
            registerFactory(factory);
        }
    }

    public static OaBaseProviderFactory<? extends OaSender, ? extends OaSupplierConfig> requireForSupplier(String supplier) {
        return factories.getOrDefault(supplier, null);
    }
}
