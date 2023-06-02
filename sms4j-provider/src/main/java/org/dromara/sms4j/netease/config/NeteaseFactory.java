package org.dromara.sms4j.netease.config;

import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.netease.service.NeteaseSmsImpl;
import org.dromara.sms4j.provider.base.BaseProviderFactory;

/**
 * NeteaseSmsConfig
 * <p> 网易云信短信
 *
 * @author :adam
 * 2023-05-30
 **/
public class NeteaseFactory implements BaseProviderFactory<NeteaseSmsImpl, NeteaseConfig> {

    private static NeteaseSmsImpl neteaseSms;

    private static final NeteaseFactory INSTANCE = new NeteaseFactory();

    private static final class ConfigHolder {
        private static NeteaseConfig config = NeteaseConfig.builder().build();
    }

    private NeteaseFactory() {
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static NeteaseFactory instance() {
        return INSTANCE;
    }

    /**
     * 建造一个网易云的短信实现
     */
    @Override
    public NeteaseSmsImpl createSms(NeteaseConfig neteaseConfig) {
        if (neteaseSms == null) {
            neteaseSms = new NeteaseSmsImpl(
                    neteaseConfig,
                    BeanFactory.getExecutor(),
                    BeanFactory.getDelayedTime()
            );
        }
        return neteaseSms;
    }

    @Override
    public NeteaseSmsImpl createMultitonSms(NeteaseConfig neteaseConfig) {
        return new NeteaseSmsImpl(neteaseConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
    }

    /**
     * 刷新对象
     */
    @Override
    public NeteaseSmsImpl refresh(NeteaseConfig neteaseConfig) {
        neteaseSms = new NeteaseSmsImpl(
                neteaseConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime()
        );
        return neteaseSms;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public NeteaseConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(NeteaseConfig config) {
        ConfigHolder.config = config;
    }

}
