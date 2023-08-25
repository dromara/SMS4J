package org.dromara.sms4j.starter.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.Map;

public class SupplierConfig {

    /** smsConfig参数意义为确保注入时smsConfig已经存在*/
    @Bean
    @ConfigurationProperties(prefix = "sms.blends")
    protected Map<String, Map<String, Object>> blends(){
        return new LinkedHashMap<>();
    }

}
