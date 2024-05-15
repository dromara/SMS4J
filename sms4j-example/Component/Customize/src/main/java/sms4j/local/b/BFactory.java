package sms4j.local.b;

import org.dromara.sms4j.provider.factory.BaseProviderFactory;

/**
 * 新增厂商的工厂实现{@link BSmsImpl}
 * 这个类就是获取LocalSmsImpl的工厂
 *
 * @author huangchengxing
 */
public class BFactory implements BaseProviderFactory<BSmsImpl, BConfig> {

    private static final BFactory INSTANCE = new BFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static BFactory instance() {
        return INSTANCE;
    }

    @Override
    public BSmsImpl createSms(BConfig aConfig) {
        return new BSmsImpl(aConfig);
    }

    @Override
    public Class<BConfig> getConfigClass() {
        return BConfig.class;
    }

    @Override
    public String getSupplier() {
        return "b";
    }
}
