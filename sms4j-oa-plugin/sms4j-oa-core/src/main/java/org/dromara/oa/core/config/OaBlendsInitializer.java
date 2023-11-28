package org.dromara.oa.core.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.config.OaSupplierConfig;
import org.dromara.oa.comm.content.OaContent;
import org.dromara.oa.core.byteTalk.config.ByteTalkFactory;
import org.dromara.oa.core.dingTalk.config.DingTalkFactory;
import org.dromara.oa.core.provider.config.OaConfig;
import org.dromara.oa.core.provider.factory.OaBaseProviderFactory;
import org.dromara.oa.core.provider.factory.OaFactory;
import org.dromara.oa.core.provider.factory.ProviderFactoryHolder;
import org.dromara.oa.core.weTalk.config.WeTalkFactory;

import java.util.List;
import java.util.Map;

/**
 * 注册工厂, 读取yaml配置并根据配置生成对象
 * @author dongfeng
 * 2023-10-22 12:39
 */

@Slf4j
public class OaBlendsInitializer {
    private List<OaBaseProviderFactory<? extends OaSender, ? extends OaSupplierConfig>> factoryList;

    private final OaConfig oaConfig;
    private final Map<String, Map<String, Object>> blends;

    public OaBlendsInitializer(
            List<OaBaseProviderFactory<? extends OaSender, ? extends OaSupplierConfig>> factoryList,
            OaConfig oaConfig,
            Map<String, Map<String, Object>> oas
    ) {
        this.factoryList=factoryList;
        this.oaConfig = oaConfig;
        this.blends = oas;
        onApplicationEvent();
    }

    public void onApplicationEvent() {
        registerDefaultFactory();
        // 解析供应商配置
        for (String configId : blends.keySet()) {
            Map<String, Object> configMap = blends.get(configId);
            if (Boolean.FALSE.equals(configMap.get("isEnable"))) {
                log.warn("configId为"+configId+"的配置未启用,请注意是否需要开启");
                continue;
            }
            Object supplierObj = configMap.get(OaContent.SUPPLIER_KEY);
            String supplier = supplierObj == null ? "" : String.valueOf(supplierObj);
            supplier = StrUtil.isEmpty(supplier) ? configId : supplier;
            OaBaseProviderFactory<OaSender, OaSupplierConfig> providerFactory = (OaBaseProviderFactory<OaSender, OaSupplierConfig>) ProviderFactoryHolder.requireForSupplier(supplier);
            if (providerFactory == null) {
                log.warn("创建\"{}\"的通知webhook服务失败，未找到供应商为\"{}\"的服务", configId, supplier);
                continue;
            }
            configMap.put("configId", configId);
            JSONObject configJson = new JSONObject(configMap);
            OaSupplierConfig supplierConfig = JSONUtil.toBean(configJson, providerFactory.getConfigClass());
            OaFactory.createAndRegisterOaSender(supplierConfig);
        }
    }

    /**
     * 注册默认工厂实例
     */
    private void registerDefaultFactory() {
        ProviderFactoryHolder.registerFactory(DingTalkFactory.instance());
        ProviderFactoryHolder.registerFactory(ByteTalkFactory.instance());
        ProviderFactoryHolder.registerFactory(WeTalkFactory.instance());
    }
}
