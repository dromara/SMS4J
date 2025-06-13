package org.dromara.sms4j.montnets.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.montnets.service.MontnetsSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;


/**
 * MontnetsSmsConfig
 * <p> 梦网对象建造者
 *
 * @author :SU
 * 2025/4/23  10:32
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MontnetsFactory extends AbstractProviderFactory<MontnetsSmsImpl, MontnetsConfig> {

    private static final MontnetsFactory INSTANCE = new MontnetsFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static MontnetsFactory instance() {
        return INSTANCE;
    }

    /**
     * 创建短信实现对象
     * @param montnetsConfig 短信配置对象
     * @return 短信实现对象
     */
    @Override
    public MontnetsSmsImpl createSms(MontnetsConfig montnetsConfig) {
        return new MontnetsSmsImpl(montnetsConfig);
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.MONTNETS;
    }
}
