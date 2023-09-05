package org.dromara.sms4j.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class SupplierConfig {

    /** 注入配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.blends")
    protected Map<String, Map<String, Object>> blends(){
        return new LinkedHashMap<>();
    }


    // @Bean
    // protected SmsBlendsInitializer smsBlendsInitializer(List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>>factoryList,
    //                                                     SmsConfig smsConfig,
    //                                                     Map<String, Map<String, Object>> blends){
    //     return new SmsBlendsInitializer(factoryList,smsConfig,blends);
    // }

}
