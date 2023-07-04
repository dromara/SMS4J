package org.dromara.sms4j.zhutong.config;

import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.provider.base.BaseProviderFactory;
import org.dromara.sms4j.zhutong.service.ZhutongSmsImpl;

public class ZhutongFactory implements BaseProviderFactory<ZhutongSmsImpl, ZhutongConfig> {
    private static ZhutongSmsImpl ZhutongSmsImpl;

    private static final ZhutongFactory INSTANCE = new ZhutongFactory();

    private static final class ConfigHolder {
        private static ZhutongConfig config = ZhutongConfig.builder().build();
    }

    private ZhutongFactory() {
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static ZhutongFactory instance() {
        return INSTANCE;
    }

    /**
     * 建造一个助通短信实现
     */
    @Override
    public ZhutongSmsImpl createSms(ZhutongConfig ZhutongConfig){
        if (ZhutongSmsImpl == null){
            ZhutongSmsImpl = createMultitonSms(ZhutongConfig);
        }
        return ZhutongSmsImpl;
    }

    @Override
    public ZhutongSmsImpl createMultitonSms(ZhutongConfig zhutongConfig) {
        return new ZhutongSmsImpl(zhutongConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
    }

    /** 刷新对象*/
    @Override
    public ZhutongSmsImpl refresh(ZhutongConfig zhutongConfig){
        ZhutongSmsImpl = new ZhutongSmsImpl(zhutongConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
        return ZhutongSmsImpl;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public ZhutongConfig getConfig() {
        return ZhutongFactory.ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(ZhutongConfig config) {
        ZhutongFactory.ConfigHolder.config = config;
    }
}
