package org.dromara.sms4j.cloopen.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.cloopen.service.CloopenSmsImpl;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;

/**
 * 容联云短信配置
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloopenFactory extends AbstractProviderFactory<CloopenSmsImpl, CloopenConfig> {

    private static final CloopenFactory INSTANCE = new CloopenFactory();

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static CloopenFactory instance() {
        return INSTANCE;
    }

    /**
     * 创建容连云短信实现对象
     *
     * @param cloopenConfig 短信配置对象
     * @return 短信实现对象
     */
    @Override
    public CloopenSmsImpl createSms(CloopenConfig cloopenConfig) {
        return new CloopenSmsImpl(cloopenConfig);
    }

    /**
     * 获取供应商
     *
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.CLOOPEN;
    }

}
