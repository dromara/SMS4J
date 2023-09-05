package org.dromara.sms4j.tencent.config;

import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.dromara.sms4j.tencent.service.TencentSmsImpl;

/**
 * TencentSmsConfig
 * <p> 建造腾讯云短信
 *
 * @author :Wind
 * 2023/4/8  16:05
 **/
public class TencentFactory extends AbstractProviderFactory<TencentSmsImpl, TencentConfig> {

    private static final TencentFactory INSTANCE = new TencentFactory();

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
        return new TencentSmsImpl(tencentConfig);
    }

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.TENCENT;
    }

}
