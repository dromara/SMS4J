package org.dromara.sms4j.yunpian.config;

import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.provider.base.BaseProviderFactory;
import org.dromara.sms4j.yunpian.service.YunPianSmsImpl;

public class YunPianFactory implements BaseProviderFactory<YunPianSmsImpl, YunpianConfig> {

    private static YunPianSmsImpl yunpianSmsImpl;

    private static final YunPianFactory INSTANCE = new YunPianFactory();

    private static final class ConfigHolder {
        private static YunpianConfig config = YunpianConfig.builder().build();
    }

    private YunPianFactory() {
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static YunPianFactory instance() {
        return INSTANCE;
    }

    /**
     * 建造一个云片短信实现
     */
    @Override
    public YunPianSmsImpl createSms(YunpianConfig yunpianConfig){
        if (yunpianSmsImpl == null){
            yunpianSmsImpl = createMultitonSms(yunpianConfig);
        }
        return yunpianSmsImpl;
    }

    @Override
    public YunPianSmsImpl createMultitonSms(YunpianConfig yunpianConfig) {
        return new YunPianSmsImpl(yunpianConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
    }

    /** 刷新对象*/
    @Override
    public YunPianSmsImpl refresh(YunpianConfig yunpianConfig){
        yunpianSmsImpl = new YunPianSmsImpl(yunpianConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
        return yunpianSmsImpl;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public YunpianConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(YunpianConfig config) {
        ConfigHolder.config = config;
    }

}
