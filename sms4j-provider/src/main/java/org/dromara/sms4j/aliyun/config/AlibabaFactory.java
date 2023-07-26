package org.dromara.sms4j.aliyun.config;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.service.AlibabaSmsImpl;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

/**
 * AlibabaSmsConfig
 * <p> 阿里巴巴对象建造者
 *
 * @author :Wind
 * 2023/4/8  14:54
 **/
@Slf4j
public class AlibabaFactory implements BaseProviderFactory<AlibabaSmsImpl, AlibabaConfig> {

    private static final AlibabaFactory INSTANCE = new AlibabaFactory();

    static {
        ProviderFactoryHolder.registerFactory(INSTANCE);
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
        String configId = StrUtil.isEmpty(alibabaConfig.getConfigId()) ? SupplierConstant.ALIBABA : alibabaConfig.getConfigId();
        return new AlibabaSmsImpl(configId, alibabaConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
    }

    /**
     * 校验是否支持指定的config类
     * @param config config类
     * @return 是否支持
     */
    @Override
    public boolean supports(Class<?> config) {
        return AlibabaConfig.class.isAssignableFrom(config);
    }


}
