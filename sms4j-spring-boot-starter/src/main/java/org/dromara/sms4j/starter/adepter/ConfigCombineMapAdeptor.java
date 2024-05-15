package org.dromara.sms4j.starter.adepter;

import cn.hutool.core.bean.BeanUtil;
import org.dromara.sms4j.core.datainterface.SmsBlendsBeanConfig;
import org.dromara.sms4j.core.datainterface.SmsBlendsSelectedConfig;
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.provider.config.BaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ConfigCombineMapAdeptor
 * <p> 继承HashMap，相当于代理了get方法，再get时如果没有取到指定key的value，那么遍历map的特殊value对象再比对对象的属性和指定key是否一致，一致则把对象转换为原返回类型的信息
 * @author :Sh1yu
 * 2023/8/1  12:06
 **/
public class ConfigCombineMapAdeptor<S, M> extends HashMap {
    @Override
    public M get(Object key) {
        Object o = super.get(key);
        if (null == o){
            Set configKeySet = this.keySet();
            for (Object insideMapKey : configKeySet) {
                if (((String)insideMapKey).startsWith(SmsReadConfig.class.getSimpleName())){
                    Map smsBlendsConfigInsideMap  = (Map) this.get(insideMapKey);
                    Object config = smsBlendsConfigInsideMap.get(insideMapKey);
                    List<BaseConfig> supplierConfigList = new ArrayList<>();
                    if (config instanceof SmsBlendsBeanConfig){
                        SmsBlendsBeanConfig beanConfig = (SmsBlendsBeanConfig)config;
                        supplierConfigList = beanConfig.getSupplierConfigList();
                    }
                    if (config instanceof SmsBlendsSelectedConfig){
                        SmsBlendsSelectedConfig selectedConfig = (SmsBlendsSelectedConfig)config;
                        BaseConfig supplierConfig = selectedConfig.getSupplierConfig((String)key);
                        if (null != supplierConfig){
                            supplierConfigList.add(supplierConfig);
                        }
                    }
                    for (BaseConfig baseConfig : supplierConfigList) {
                        if (key.equals(baseConfig.getConfigId())){
                            Map<String, Object> configMap = BeanUtil.beanToMap(baseConfig);
                            this.put(baseConfig.getConfigId(),configMap);
                            return (M)configMap;
                        }
                    }
                }
            }
            return null;
        }
        return (M)o;
    }
}
