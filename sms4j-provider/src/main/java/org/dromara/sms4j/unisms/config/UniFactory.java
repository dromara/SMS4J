package org.dromara.sms4j.unisms.config;

import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.provider.base.BaseProviderFactory;
import org.dromara.sms4j.unisms.core.Uni;
import org.dromara.sms4j.unisms.service.UniSmsImpl;

/**
 * UniSmsConfig
 * <p>合一短信建造对象
 * @author :Wind
 * 2023/4/8  15:46
 **/
public class UniFactory implements BaseProviderFactory<UniSmsImpl, UniConfig> {

    private static UniSmsImpl uniSmsImpl;

    private static final UniFactory INSTANCE = new UniFactory();

    private static final class ConfigHolder {
        private static UniConfig config = UniConfig.builder().build();
    }

    private UniFactory() {
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static UniFactory instance() {
        return INSTANCE;
    }


    /** 短信配置*/
    private void buildSms(UniConfig uniConfig){
        if (uniConfig.getIsSimple()){
            Uni.init(uniConfig.getAccessKeyId());
        }else {
            Uni.init(uniConfig.getAccessKeyId(),uniConfig.getAccessKeySecret());
        }
    }

    /**
     *  createUniSms
     * <p>建造一个短信实现对像
     * @author :Wind
    */
    @Override
    public UniSmsImpl createSms(UniConfig uniConfig){
        if (uniSmsImpl == null){
            this.buildSms(uniConfig);
            uniSmsImpl = createMultitonSms(uniConfig);
        }
        return uniSmsImpl;
    }

    @Override
    public UniSmsImpl createMultitonSms(UniConfig config) {
        return new UniSmsImpl(
                config,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime()
        );
    }

    /**
     *  refresh
     * <p>刷新对象
     * @author :Wind
    */
    @Override
    public UniSmsImpl refresh(UniConfig uniConfig){
        this.buildSms(uniConfig);
        uniSmsImpl = new UniSmsImpl(
                uniConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime()
        );
        return uniSmsImpl;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public UniConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(UniConfig config) {
        ConfigHolder.config = config;
    }

}
