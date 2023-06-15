package org.dromara.sms4j.provider.base;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;

/**
 * AlibabaSmsConfig
 * <p>短信对象建造者</p>
 * @param <S> 短信对象
 * @param <C> 短信配置对象
 */
public interface BaseProviderFactory<S extends SmsBlend, C extends SupplierConfig> {

    /**
     * 创建短信实现对象
     * @param c 短信配置对象
     * @return 短信实现对象
     */
    S createSms(C c);

    /**
     *  createMultitonSms
     * <p> 创建多例的短信实现对象
     * 在此方法中创建的短信实现对象框架将不再持有单例，故而JVM中可以同时存在多个，如果更改配置后需要重新调用该方法
     * @param c 短信配置对象
     * @return 短信实现对象
     * @author :Wind
    */
    S createMultitonSms(C c);

    /**
     * 刷新短信实现对象
     * @param c 短信配置对象
     * @return 刷新后的短信实现对象
     */
    S refresh(C c);

    /**
     * 获取配置
     * @return 配置对象
     */
    C getConfig();

    /**
     * 设置配置
     * @param config 配置对象
     */
    void setConfig(C config);

}
