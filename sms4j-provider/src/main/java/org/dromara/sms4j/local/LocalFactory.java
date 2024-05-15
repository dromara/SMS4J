package org.dromara.sms4j.local;

import org.dromara.sms4j.aliyun.config.AlibabaFactory;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;

/**
 * 用于创建{@link LocalSmsImpl}的工厂实现
 *
 * @author huangchengxing
 * @see SupplierConstant#LOCAL
 */
public class LocalFactory implements BaseProviderFactory<LocalSmsImpl, LocalConfig> {

    private static final LocalFactory INSTANCE = new LocalFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static LocalFactory instance() {
        return INSTANCE;
    }

    @Override
    public LocalSmsImpl createSms(LocalConfig localConfig) {
        return new LocalSmsImpl();
    }

    @Override
    public Class<LocalConfig> getConfigClass() {
        return LocalConfig.class;
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.LOCAL;
    }
}
