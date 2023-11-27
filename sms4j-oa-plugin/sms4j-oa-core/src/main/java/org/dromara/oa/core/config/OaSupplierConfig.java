package org.dromara.oa.core.config;

import org.dromara.oa.api.OaSender;
import org.dromara.oa.core.provider.config.OaConfig;
import org.dromara.oa.core.provider.factory.OaBaseProviderFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.LinkedHashMap;
import java.util.List;
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
    @ConfigurationProperties(prefix = "sms-oa.oas")
    @ConditionalOnProperty(prefix = "sms-oa", name = "config-type", havingValue = "yaml")
    protected Map<String, Map<String, Object>> oas() {
        return new LinkedHashMap<>();
    }


    @Bean
    protected OaBlendsInitializer smsOasInitializer(
            List<OaBaseProviderFactory<? extends OaSender, ? extends org.dromara.oa.comm.config.OaSupplierConfig>> factoryList,
            OaConfig oaConfig,
            Map<String, Map<String, Object>> oas) {
        return new OaBlendsInitializer(factoryList,oaConfig,oas);
    }
}
