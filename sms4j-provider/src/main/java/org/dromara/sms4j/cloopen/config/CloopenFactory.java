package org.dromara.sms4j.cloopen.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.cloopen.service.CloopenSmsImpl;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.provider.base.BaseProviderFactory;

/**
 * 容联云短信配置
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloopenFactory implements BaseProviderFactory<CloopenSmsImpl, CloopenConfig> {

    private static CloopenSmsImpl cloopenSms;

    private static final CloopenFactory INSTANCE = new CloopenFactory();

    private static final class ConfigHolder {
        private static CloopenConfig config = CloopenConfig.builder().build();
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static CloopenFactory instance() {
        return INSTANCE;
    }

    /**
     * 创建容连云短信实现对象
     * @param cloopenConfig 短信配置对象
     * @return 短信实现对象
     */
    @Override
    public CloopenSmsImpl createSms(CloopenConfig cloopenConfig) {
        if (cloopenSms == null) {
            cloopenSms = createMultitonSms(cloopenConfig);
        }
        return cloopenSms;
    }

    @Override
    public CloopenSmsImpl createMultitonSms(CloopenConfig cloopenConfig) {
        return new CloopenSmsImpl(cloopenConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
    }

    /**
     * 刷新容连云短信实现对象
     * @param cloopenConfig 短信配置对象
     * @return 刷新后的短信实现对象
     */
    @Override
    public CloopenSmsImpl refresh(CloopenConfig cloopenConfig) {
        //重新构造一个实现对象
        cloopenSms = new CloopenSmsImpl(cloopenConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
        return cloopenSms;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public CloopenConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(CloopenConfig config) {
        ConfigHolder.config = config;
    }

}
