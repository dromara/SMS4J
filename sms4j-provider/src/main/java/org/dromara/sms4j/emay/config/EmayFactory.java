package org.dromara.sms4j.emay.config;

import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.emay.service.EmaySmsImpl;
import org.dromara.sms4j.provider.base.BaseProviderFactory;

/**
 * EmaySmsConfig
 * <p> Emay短信对象建造
 *
 * @author Richard
 * @date 2023/04/11 12:00
 * */
public class EmayFactory implements BaseProviderFactory<EmaySmsImpl, EmayConfig> {
    private static EmaySmsImpl emaySms;
    private static final EmayFactory INSTANCE = new EmayFactory();

    private static final class ConfigHolder {
        private static EmayConfig config = EmayConfig.builder().build();
    }

    private EmayFactory() {
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static EmayFactory instance() {
        return INSTANCE;
    }

    /**
     * 创建亿美软通短信实现对象
     * @param emayConfig 短信配置对象
     * @return 短信实现对象
     */
    @Override
    public EmaySmsImpl createSms(EmayConfig emayConfig) {
        if (emaySms == null){
            emaySms = new EmaySmsImpl(emayConfig, BeanFactory.getExecutor(),BeanFactory.getDelayedTime());
        }
        return emaySms;
    }

    /**
     * 刷新短信实现对象
     * @param emayConfig 短信配置对象
     * @return 刷新后的短信实现对象
     */
    @Override
    public EmaySmsImpl refresh(EmayConfig emayConfig){
        emaySms = new EmaySmsImpl(emayConfig, BeanFactory.getExecutor(),BeanFactory.getDelayedTime());
        return emaySms;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public EmayConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(EmayConfig config) {
        ConfigHolder.config = config;
    }

}
