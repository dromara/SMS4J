package org.dromara.sms4j.starter.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.dromara.sms4j.starter.utils.SmsSpringUtil;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class SmsBlendsInitializer {

    private final SmsConfig smsConfig;
    private final Map<String, Map<String, String>> blends;

    @PostConstruct
    public void initBlends() {
        // 解析供应商配置
        for(String configId : blends.keySet()) {
            Map<String, String> configMap = blends.get(configId);
            String supplier = configMap.get(Constant.SUPPLIER_KEY);
            supplier = StrUtil.isEmpty(supplier) ? configId : supplier;
            BaseProviderFactory<SmsBlend, SupplierConfig> providerFactory = (BaseProviderFactory<SmsBlend, org.dromara.sms4j.api.universal.SupplierConfig>) ProviderFactoryHolder.requireForSupplier(supplier);
            if(providerFactory == null) {
                log.warn("创建\"{}\"的短信服务失败，未找到供应商为\"{}\"的服务", configId, supplier);
                continue;
            }
            SmsUtil.replaceKeysSeperator(configMap, "-", "_");
            JSONObject configJson = new JSONObject(configMap);
            org.dromara.sms4j.api.universal.SupplierConfig supplierConfig = JSONUtil.toBean(configJson, providerFactory.getConfigClass());
            if(Boolean.TRUE.equals(smsConfig.getRestricted())) {
                SmsFactory.createRestrictedSmsBlend(supplierConfig);
            } else {
                SmsFactory.createSmsBlend(supplierConfig);
            }
        }
    }

}
