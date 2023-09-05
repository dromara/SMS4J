package org.dromara.sms4j.emay.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.emay.service.EmaySmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

/**
 * EmaySmsConfig
 * <p> Emay短信对象建造
 *
 * @author Richard
 * @date 2023/04/11 12:00
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmayFactory extends AbstractProviderFactory<EmaySmsImpl, EmayConfig> {

    private static final EmayFactory INSTANCE = new EmayFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static EmayFactory instance() {
        return INSTANCE;
    }

    /**
     * 创建亿美软通短信实现对象
     * @param emayConfig 短信配置对象
     * @return 短信实现对象
     */
    @Override
    public EmaySmsImpl createSms(EmayConfig emayConfig) {
        return new EmaySmsImpl(emayConfig);
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.EMAY;
    }

}
