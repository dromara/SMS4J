package org.dromara.sms4j.provider.factory;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 短信对象建造者
 * @param <S>
 * @param <C>
 */
public abstract class AbstractProviderFactory<S extends SmsBlend, C extends SupplierConfig> implements BaseProviderFactory<S, C> {

    private Class<C> configClass;

    public AbstractProviderFactory() {
        Type genericSuperclass = getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) genericSuperclass;
            Type[] typeArguments = paramType.getActualTypeArguments();
            if(typeArguments.length > 1 && typeArguments[1] instanceof Class) {
                configClass = (Class<C>) typeArguments[1];
            }
        }
    }

    /**
     * 获取配置类
     * @return 配置类
     */
    @Override
    public Class<C> getConfigClass() {
        return configClass;
    }

}
