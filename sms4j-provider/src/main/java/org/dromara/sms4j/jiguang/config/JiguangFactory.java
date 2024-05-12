package org.dromara.sms4j.jiguang.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.jiguang.service.JiguangSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;

/**
 * <p>类名: JiguangFactory
 * <p>说明：极光 sms
 *
 * @author :SmartFire
 * 2024/3/15
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JiguangFactory extends AbstractProviderFactory<JiguangSmsImpl, JiguangConfig> {

    private static final JiguangFactory INSTANCE = new JiguangFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static JiguangFactory instance() {
        return INSTANCE;
    }

    /**
     * 创建短信实现对象
     * @param config 短信配置对象
     * @return 短信实现对象
     */
    @Override
    public JiguangSmsImpl createSms(JiguangConfig config) {
        return new JiguangSmsImpl(config);
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.JIGUANG;
    }

}
