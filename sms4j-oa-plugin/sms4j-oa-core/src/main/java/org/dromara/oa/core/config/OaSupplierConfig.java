package org.dromara.oa.core.config;

import org.dromara.oa.api.OaSender;
import org.dromara.oa.core.provider.config.OaConfig;
import org.dromara.oa.core.provider.factory.OaBaseProviderFactory;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author dongfeng
 * 2023-10-22 12:50
 */
public class OaSupplierConfig {

    @Bean
    protected OaBlendsInitializer smsOasInitializer(
            List<OaBaseProviderFactory<? extends OaSender, ? extends org.dromara.oa.comm.config.OaSupplierConfig>> factoryList,
            OaConfig oaConfig,
            OaConfigProperties properties) {
        return new OaBlendsInitializer(factoryList, oaConfig, properties.getOas());
    }
}
