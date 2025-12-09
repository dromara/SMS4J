package org.dromara.sms4j.huyi.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.huyi.service.HuYiSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;

/**
 * HuYiSmsConfig
 * <p> 互亿无限对象建造者
 *
 * @author 初心
 * 2023/05/05
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HuYiFactory extends AbstractProviderFactory<HuYiSmsImpl, HuYiConfig> {

    private static final HuYiFactory INSTANCE = new HuYiFactory();

    /**
     * 获取建造者实例
     *
     * @return 建造者实例
     */
    public static HuYiFactory instance() {
        return INSTANCE;
    }

    /**
     * 创建短信服务
     *
     * @param huYiConfig 短信配置对象
     * @return 短信实现对象
     */
    @Override
    public HuYiSmsImpl createSms(HuYiConfig huYiConfig) {
        return new HuYiSmsImpl(huYiConfig);
    }

    /**
     * 获取供应商名称
     *
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.HUYI;
    }
}
