package org.dromara.sms4j.aliyun.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.aliyun.service.AlibabaSmsImpl;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

/**
 * AlibabaSmsConfig
 * <p> 阿里巴巴对象建造者
 *
 * @author :Wind
 * 2023/4/8  14:54
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AlibabaFactory extends AbstractProviderFactory<AlibabaSmsImpl, AlibabaConfig> {

    private static final AlibabaFactory INSTANCE = new AlibabaFactory();

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
        return new AlibabaSmsImpl(alibabaConfig);
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.ALIBABA;
    }

}
