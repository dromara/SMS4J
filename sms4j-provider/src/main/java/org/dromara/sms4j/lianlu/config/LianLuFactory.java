package org.dromara.sms4j.lianlu.config;

import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.lianlu.service.LianLuSmsImpl;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;

/**
 * 联鹿短信
 * */
public class LianLuFactory implements BaseProviderFactory<LianLuSmsImpl, LianLuConfig> {
    private static final LianLuFactory INSTANCE = new LianLuFactory();

    private LianLuFactory() {
    }

    public static LianLuFactory instance() {
        return INSTANCE;
    }

    @Override
    public LianLuSmsImpl createSms(LianLuConfig lianLuConfig) {
        return new LianLuSmsImpl(lianLuConfig);
    }

    @Override
    public Class<LianLuConfig> getConfigClass() {
        return LianLuConfig.class;
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.LIANLU;
    }
}
