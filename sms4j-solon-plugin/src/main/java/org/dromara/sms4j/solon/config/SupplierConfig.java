package org.dromara.sms4j.solon.config;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.AppContext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * smsConfig参数意义为确保注入时smsConfig已经存在
 */
@Condition(onProperty = "${sms.configType}=config_file")
@Configuration
public class SupplierConfig {
    @Inject
    AppContext context;

    private <T> T injectObj(String prefix, T obj) {
        //@Inject 只支持在字段、参数、类型上注入
        context.cfg().getProp(prefix).bindTo(obj);
        return obj;
    }
    @Bean
    protected Map<String, Map<String, Object>> blends(){
        return injectObj("sms.blends",new LinkedHashMap<>());
    }


    @Bean
    protected SmsBlendsInitializer smsBlendsInitializer(List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> factoryList,
                                                        SmsConfig smsConfig,
                                                        Map<String, Map<String, Object>> blends){
        return new SmsBlendsInitializer(factoryList,smsConfig,blends,context);
    }
}
