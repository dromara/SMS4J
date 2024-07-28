package org.dromara.sms4j.starter.configration;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import lombok.SneakyThrows;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.enums.ConfigType;
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.starter.config.*;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;
import java.util.*;

@EnableConfigurationProperties({AlibabaMultiConfig.class, CloopenMultiConfig.class, CtyunMultiConfig.class, DingzhongMultiConfig.class
        , EmayMultiConfig.class, HuaweiMultiConfig.class, JdcloudMultiConfig.class, LianluMultiConfig.class, NeteaseMultiConfig.class
        , QiniuMultiConfig.class, TencentMultiConfig.class, UnismsMultiConfig.class, YunpianMultiConfig.class, ZhutongMultiConfig.class})
public class SupplierConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "yaml")
    @SneakyThrows
    private Map<String, Map<String, Object>> blends(List<AbstractMultiConfig<?>> blends) {
        Map<String, Map<String, Object>> blendsMap = new HashMap<>();
        for (AbstractMultiConfig<?> blend : blends) {
            Field[] declaredFields = blend.getClass().getDeclaredFields();
            Optional<Field> first = Arrays.stream(declaredFields).filter(field -> field.getType().isAssignableFrom(Map.class)).findFirst();
            if (first.isPresent()) {
                Field field = first.get();
                field.setAccessible(Boolean.TRUE);
                Map<String, ? extends BaseConfig> configMap = (Map) field.get(blend);
                if (Objects.isNull(configMap)) {
                    continue;
                }
                for (String configId : configMap.keySet()) {
                    BaseConfig config = configMap.get(configId);
                    //将实体转换为单独属性,兼容原逻辑,更改最小
                    Map<String, Object> fieldConfigMap = JSONUtil.parseObj(config).toBean(new TypeReference<Map<String, Object>>() {
                    });
                    blendsMap.put(configId, fieldConfigMap);
                }

            }
        }
        return blendsMap;
    }

    @Bean
    @ConditionalOnBean({SmsConfig.class})
    @SneakyThrows
    protected List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> factoryList(Map<String, Map<String, Object>> blends, SmsConfig smsConfig) {
        //注入自定义实现工厂
        List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> factoryList = new ArrayList<>();
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
    protected SmsBlendsInitializer smsBlendsInitializer(List<BaseProviderFactory<? extends SmsBlend, ? extends org.dromara.sms4j.api.universal.SupplierConfig>> factoryList,
                                                        SmsConfig smsConfig,
                                                        Map<String, Map<String, Object>> blends,
                                                        ObjectProvider<SmsReadConfig> extendsSmsConfigs) {
        return new SmsBlendsInitializer(factoryList, smsConfig, blends, extendsSmsConfigs);
    }

}
