package org.dromara.sms4j.ctyun.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.ctyun.service.CtyunSmsImpl;
import org.dromara.sms4j.provider.base.BaseProviderFactory;

/**
 * <p>类名: CtyunSmsConfig
 * <p>说明： 天翼云 云通信短信配置器
 *
 * @author :bleachhtred
 * 2023/5/12  15:06
 **/
@Slf4j
public class CtyunFactory implements BaseProviderFactory<CtyunSmsImpl, CtyunConfig> {

    private static CtyunSmsImpl ctyunSms;

    private static final CtyunFactory INSTANCE = new CtyunFactory();

    private static final class ConfigHolder {
        private static CtyunConfig config = CtyunConfig.builder().build();
    }

    private CtyunFactory() {
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static CtyunFactory instance() {
        return INSTANCE;
    }

    /**
     * getCtyunSms
     * <p> 建造一个短信实现对像
     *
     * @author :bleachhtred
     */
    @Override
    public CtyunSmsImpl createSms(CtyunConfig ctyunConfig) {
        if (ctyunSms == null) {
            ctyunSms = createMultitonSms(ctyunConfig);
        }
        return ctyunSms;
    }

    @Override
    public CtyunSmsImpl createMultitonSms(CtyunConfig ctyunConfig) {
        return new CtyunSmsImpl(
                ctyunConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime());
    }

    /**
     * refresh
     * <p> 刷新对象
     *
     * @author :bleachhtred
     */
    @Override
    public CtyunSmsImpl refresh(CtyunConfig ctyunConfig) {
        //重新构造一个实现对象
        ctyunSms = new CtyunSmsImpl(
                ctyunConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime());
        return ctyunSms;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public CtyunConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(CtyunConfig config) {
        ConfigHolder.config = config;
    }
}
