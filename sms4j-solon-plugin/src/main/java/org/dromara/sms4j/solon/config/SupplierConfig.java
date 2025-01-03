package org.dromara.sms4j.solon.config;

import cn.hutool.core.util.ObjectUtil;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.enums.ConfigType;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Condition;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.annotation.Inject;
import org.noear.solon.core.AppContext;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * smsConfig参数意义为确保注入时smsConfig已经存在
 */
@Condition(onProperty = "${sms.configType}=yaml")
@Configuration
public class SupplierConfig {
    @Inject
    AppContext context;

    private <T> T injectObj(String prefix, T obj) {
        //@Inject 只支持在字段、参数、类型上注入
        context.cfg().getProp(prefix).bindTo(obj);
        return obj;
    }

    @Bean("blends")
    public Map<String, Map<String, Object>> blends() {
        return injectObj("sms.blends", new LinkedHashMap<>());
    }

    @Bean
    @Condition(onBean = SmsConfig.class)
    public List<BaseProviderFactory> factoryList(@Inject("blends") Map<String, Map<String, Object>> blends, SmsConfig smsConfig) throws Exception {
        //注入自定义实现工厂
        List<BaseProviderFactory> factoryList = new ArrayList<>();
        if (ConfigType.YAML.equals(smsConfig.getConfigType())) {
            for (String configId : blends.keySet()) {
                Map<String, Object> configMap = blends.get(configId);
                Object factoryPath = configMap.get(Constant.FACTORY_PATH);
                if (ObjectUtil.isNotEmpty(factoryPath)) {
                    //反射创建实例
                    Class<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> newClass = (Class<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>>) Class.forName(factoryPath.toString());
                    BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig> factory = newClass.newInstance();
                    factoryList.add(factory);
                }
            }
        }

        return factoryList;
    }


    @Bean
    public SmsBlendsInitializer smsBlendsInitializer(List<BaseProviderFactory> factoryList,
                                                     SmsConfig smsConfig,
                                                     @Inject("blends") Map<String, Map<String, Object>> blends) {

        //todo: solon 不支持泛型的 List[Bean] 注入
        List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> factoryList2 = new ArrayList<>(factoryList.size());
        for (BaseProviderFactory factory : factoryList) {
            factoryList2.add((BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>) factory);
        }


        return new SmsBlendsInitializer(factoryList2, smsConfig, blends, context);
    }
}
