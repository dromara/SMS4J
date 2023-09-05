package org.dromara.sms4j.ctyun.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.ctyun.service.CtyunSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

/**
 * <p>类名: CtyunSmsConfig
 * <p>说明： 天翼云 云通信短信配置器
 *
 * @author :bleachhtred
 * 2023/5/12  15:06
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CtyunFactory extends AbstractProviderFactory<CtyunSmsImpl, CtyunConfig> {

    private static final CtyunFactory INSTANCE = new CtyunFactory();

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
        return new CtyunSmsImpl(ctyunConfig);
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.CTYUN;
    }

}
