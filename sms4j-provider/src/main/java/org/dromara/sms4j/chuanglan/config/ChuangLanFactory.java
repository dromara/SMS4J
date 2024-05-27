package org.dromara.sms4j.chuanglan.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.chuanglan.service.ChuangLanSmsImpl;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;

/**
 * @author YYM
 * @Date: 2024/2/1 9:03 44
 * @描述: ChuangLanFactory
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChuangLanFactory extends AbstractProviderFactory<ChuangLanSmsImpl, ChuangLanConfig> {

    private static final ChuangLanFactory INSTANCE = new ChuangLanFactory();

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static ChuangLanFactory instance() {
        return INSTANCE;
    }

    @Override
    public ChuangLanSmsImpl createSms(ChuangLanConfig chuangLanConfig) {
        return new ChuangLanSmsImpl(chuangLanConfig);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.CHUANGLAN;
    }
}