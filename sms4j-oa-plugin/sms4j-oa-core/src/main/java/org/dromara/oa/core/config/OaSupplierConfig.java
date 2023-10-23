package org.dromara.oa.core.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author dongfeng
 * @date 2023-10-22 12:50
 */
public class OaSupplierConfig {
    /**
     * 注入配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.oas")
    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "yaml")
    protected Map<String, Map<String, Object>> oas() {
        return new LinkedHashMap<>();
    }


    @Bean
    protected OaBlendsInitializer smsOasInitializer(
            Map<String, Map<String, Object>> oas) {
        return new OaBlendsInitializer(oas);
    }
}
