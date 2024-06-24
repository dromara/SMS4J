package org.dromara.sms4j.starter.adepter;

import cn.hutool.core.bean.BeanUtil;
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.provider.config.BaseConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigCombineMapAdaptor<S, M> extends HashMap {
    @Override
    public M get(Object key) {
        Object o = super.get(key);
        if (null == o){
            Set configKeySet = this.keySet();
            for (Object insideMapKey : configKeySet) {
                if (((String)insideMapKey).startsWith(SmsReadConfig.class.getSimpleName())){
                    Map smsBlendsConfigInsideMap  = (Map) this.get(insideMapKey);
                    SmsReadConfig config = (SmsReadConfig) smsBlendsConfigInsideMap.get(insideMapKey);
                    BaseConfig supplierConfig = config.getSupplierConfig((String)key);
                    List<BaseConfig> supplierConfigList = config.getSupplierConfigList();
                    if (null == supplierConfigList){
                        supplierConfigList = new ArrayList<>();
                    }
                    if (null != supplierConfig){
                        supplierConfigList.add(supplierConfig);
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
