package org.dromara.sms4j.luosimao.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.luosimao.service.LuoSiMaoSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;

/**
 * <p>类名: LuoSiMaoFactory
 *
 * @author :bleachtred
 * 2024/6/21  23:59
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LuoSiMaoFactory extends AbstractProviderFactory<LuoSiMaoSmsImpl, LuoSiMaoConfig> {

    private static final LuoSiMaoFactory INSTANCE = new LuoSiMaoFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static LuoSiMaoFactory instance() {
        return INSTANCE;
    }

    /**
     * <p> 建造一个短信实现对像
     *
     * @author :bleachtred
     */
    @Override
    public LuoSiMaoSmsImpl createSms(LuoSiMaoConfig config) {
        return new LuoSiMaoSmsImpl(config);
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.LUO_SI_MAO;
    }

}
