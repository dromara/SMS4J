package org.dromara.sms4j.tencent.config;

import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.provider.base.BaseProviderFactory;
import org.dromara.sms4j.tencent.service.TencentSmsImpl;

/**
 * TencentSmsConfig
 * <p> 建造腾讯云短信
 *
 * @author :Wind
 * 2023/4/8  16:05
 **/
public class TencentFactory implements BaseProviderFactory<TencentSmsImpl, TencentConfig> {

    private static TencentSmsImpl tencentSms;

    private static final TencentFactory INSTANCE = new TencentFactory();

    private static final class ConfigHolder {
        private static TencentConfig config = TencentConfig.builder().build();
    }

    private TencentFactory() {
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static TencentFactory instance() {
        return INSTANCE;
    }

    /**
     * 建造一个腾讯云的短信实现
     */
    @Override
    public TencentSmsImpl createSms(TencentConfig tencentConfig) {
        if (tencentSms == null) {
            tencentSms = createMultitonSms(tencentConfig);
        }
        return tencentSms;
    }

    @Override
    public TencentSmsImpl createMultitonSms(TencentConfig tencentConfig) {
        return new TencentSmsImpl(tencentConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime()
        );
    }

    /**
     * 刷新对象
     */
    @Override
    public TencentSmsImpl refresh(TencentConfig tencentConfig) {
        tencentSms = new TencentSmsImpl(tencentConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime()
        );
        return tencentSms;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public TencentConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(TencentConfig config) {
        ConfigHolder.config = config;
    }

}
