package org.dromara.oa.core.provider.factory;

import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.config.OaSupplierConfig;
import org.dromara.oa.comm.errors.OaException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OaFactory {
    private final static Map<String, OaSender> configs = new ConcurrentHashMap<>();

    /**
     * <p>创建各个厂商的实现类
     *
     * @param config 通知webhook配置
     */
    public static void createAndRegisterOaSender(OaSupplierConfig config) {
        OaSender oaSender = createAndGetOa(config);
        register(oaSender);
    }

    /**
     * 注册通知webhook服务对象
     *
     * @param smsBlend 通知webhook服务对象
     */
    public static void register(OaSender smsBlend) {
        if (smsBlend == null) {
            throw new OaException("通知webhook服务对象不能为空");
        }
        configs.put(smsBlend.getConfigId(), smsBlend);
    }

    public static OaSender createAndGetOa(OaSupplierConfig config) {
        BaseProviderFactory factory = ProviderFactoryHolder.requireForSupplier(config.getSupplier());
        if (factory == null) {
            throw new OaException("不支持当前供应商配置");
        }
        return factory.createSmsOa(config);
    }

    /**
     * 通过configId获取通知webhook服务对象
     *
     * @param configId 唯一标识
     * @return 返回通知webhook服务对象。如果未找到则返回null
     */
    public static OaSender getSmsOaBlend(String configId) {
        return configs.get(configId);
    }

}
