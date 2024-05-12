package org.dromara.sms4j.jg.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.jg.service.JgSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;

/**
 * <p>类名: JgFactory
 * <p>说明：极光 sms
 *
 * @author :SmartFire
 * 2024/3/15
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JgFactory extends AbstractProviderFactory<JgSmsImpl, JgConfig> {

    private static final JgFactory INSTANCE = new JgFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static JgFactory instance() {
        return INSTANCE;
    }

    /**
     * 创建短信实现对象
     * @param config 短信配置对象
     * @return 短信实现对象
     */
    @Override
    public JgSmsImpl createSms(JgConfig config) {
        return new JgSmsImpl(config);
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
