package org.dromara.sms4j.huawei.config;

import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.huawei.service.HuaweiSmsImpl;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;

/**
 * HuaweiSmsConfig
 * <p> 华为短信对象建造
 *
 * @author :Wind
 * 2023/4/8  15:27
 **/
public class HuaweiFactory implements BaseProviderFactory<HuaweiSmsImpl, HuaweiConfig> {
    private static HuaweiSmsImpl huaweiSms;
    private static final HuaweiFactory INSTANCE = new HuaweiFactory();

    private static final class ConfigHolder {
        private static HuaweiConfig config = HuaweiConfig.builder().build();
    }

    private HuaweiFactory() {
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
        if (huaweiSms == null){
            huaweiSms = createMultitonSms(huaweiConfig);
        }
        return huaweiSms;
    }

    @Override
    public HuaweiSmsImpl createMultitonSms(HuaweiConfig huaweiConfig) {
        return new HuaweiSmsImpl(huaweiConfig, BeanFactory.getExecutor(),BeanFactory.getDelayedTime());
    }

    /** 刷新对象*/
    @Override
    public HuaweiSmsImpl refresh(HuaweiConfig huaweiConfig){
        huaweiSms = new HuaweiSmsImpl(huaweiConfig, BeanFactory.getExecutor(),BeanFactory.getDelayedTime());
        return huaweiSms;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public HuaweiConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(HuaweiConfig config) {
        ConfigHolder.config = config;
    }

}
