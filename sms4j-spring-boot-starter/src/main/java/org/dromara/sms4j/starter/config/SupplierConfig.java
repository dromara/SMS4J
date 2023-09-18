package org.dromara.sms4j.starter.config;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SupplierConfig {

    /** 注入配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.blends")
    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "yaml")
    protected Map<String, Map<String, Object>> blends(){
        return new LinkedHashMap<>();
    }


     @Bean
     @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "yaml")
     protected SmsBlendsInitializer smsBlendsInitializer(List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> factoryList,
                                                         SmsConfig smsConfig,
                                                         Map<String, Map<String, Object>> blends){
         return new SmsBlendsInitializer(factoryList,smsConfig,blends);
     }

}
