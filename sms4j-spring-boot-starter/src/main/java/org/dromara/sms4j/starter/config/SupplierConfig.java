package org.dromara.sms4j.starter.config;

import cn.hutool.core.util.ObjectUtil;
import lombok.SneakyThrows;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.enums.ConfigType;
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SupplierConfig {

    /**
     * 注入配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.blends")
    // SmsConfig 默认使用 YAML；未显式配置 sms.config-type 时也应创建 blends，避免默认启动缺少该 Bean。
    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "yaml", matchIfMissing = true)
    protected Map<String, Map<String, Object>> blends() {
        return new LinkedHashMap<>();
    }

    @Bean
    @ConditionalOnBean({SmsConfig.class})
    @SneakyThrows
    protected List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> factoryList(@Qualifier("blends") Map<String, Map<String, Object>> blends, SmsConfig smsConfig) {
        //注入自定义实现工厂
        List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> factoryList = new ArrayList<>();
        if (ConfigType.YAML.equals(smsConfig.getConfigType())) {
            for (String configId : blends.keySet()) {
                Map<String, Object> configMap = blends.get(configId);
                Object factoryPath = configMap.get(Constant.FACTORY_PATH);
                if (ObjectUtil.isNotEmpty(factoryPath)) {
                    // 使用构造器 API 替代已废弃的 Class#newInstance，保留更明确的反射异常语义。
                    Class<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> newClass = (Class<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>>) Class.forName(factoryPath.toString());
                    BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig> factory = newClass.getDeclaredConstructor().newInstance();
                    factoryList.add(factory);
                }
            }
        }
        return factoryList;
    }

    @Bean
    protected SmsBlendsInitializer smsBlendsInitializer(List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> factoryList,
                                                        SmsConfig smsConfig,
                                                        @Qualifier("blends") Map<String, Map<String, Object>> blends,
                                                        ObjectProvider<SmsReadConfig> extendsSmsConfigs) {
        return new SmsBlendsInitializer(factoryList, smsConfig, blends, extendsSmsConfigs);
    }

}
