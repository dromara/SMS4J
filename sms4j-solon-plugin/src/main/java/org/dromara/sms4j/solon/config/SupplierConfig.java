package org.dromara.sms4j.solon.config;

import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.BeanFactory;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
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
