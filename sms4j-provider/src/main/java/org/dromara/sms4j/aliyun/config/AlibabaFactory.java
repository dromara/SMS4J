package org.dromara.sms4j.aliyun.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.service.AlibabaSmsImpl;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.provider.base.BaseProviderFactory;


/**
 * AlibabaSmsConfig
 * <p> 阿里巴巴对象建造者
 *
 * @author :Wind
 * 2023/4/8  14:54
 **/
@Slf4j
public class AlibabaFactory implements BaseProviderFactory<AlibabaSmsImpl, AlibabaConfig> {

    private static AlibabaSmsImpl alibabaSms;

    private static final AlibabaFactory INSTANCE = new AlibabaFactory();

    private static final class ConfigHolder {
        private static AlibabaConfig config = AlibabaConfig.builder().build();
    }

    private AlibabaFactory() {
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static AlibabaFactory instance() {
        return INSTANCE;
    }

    /**
     * 创建短信实现对象
     * @param alibabaConfig 短信配置对象
     * @return 短信实现对象
     */
    @Override
    public AlibabaSmsImpl createSms(AlibabaConfig alibabaConfig) {
        if (alibabaSms == null) {
            alibabaSms = createMultitonSms(alibabaConfig);
        }
        return alibabaSms;
    }

    @Override
    public AlibabaSmsImpl createMultitonSms(AlibabaConfig alibabaConfig) {
        return new AlibabaSmsImpl(alibabaConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
    }

    /**
     * 刷新短信实现对象
     * @param alibabaConfig 短信配置对象
     * @return 刷新后的短信实现对象
     */
    @Override
    public AlibabaSmsImpl refresh(AlibabaConfig alibabaConfig) {
        //重新构造一个实现对象
        alibabaSms = new AlibabaSmsImpl(alibabaConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
        return alibabaSms;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public AlibabaConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(AlibabaConfig config) {
        ConfigHolder.config = config;
    }

}
