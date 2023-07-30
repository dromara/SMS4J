package org.dromara.sms4j.huawei.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.aliyun.service.AlibabaSmsImpl;
import org.dromara.sms4j.huawei.service.HuaweiSmsImpl;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

/**
 * HuaweiSmsConfig
 * <p> 华为短信对象建造
 *
 * @author :Wind
 * 2023/4/8  15:27
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HuaweiFactory implements BaseProviderFactory<HuaweiSmsImpl, HuaweiConfig> {

    private static final HuaweiFactory INSTANCE = new HuaweiFactory();

    static {
        ProviderFactoryHolder.registerFactory(INSTANCE);
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static HuaweiFactory instance() {
        return INSTANCE;
    }

    /** 建造一个华为短信实现*/
    @Override
    public HuaweiSmsImpl createSms(HuaweiConfig huaweiConfig) {
        return new HuaweiSmsImpl(huaweiConfig);
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return AlibabaSmsImpl.SUPPLIER;
    }

}
