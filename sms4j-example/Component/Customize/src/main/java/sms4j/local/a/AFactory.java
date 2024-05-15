package sms4j.local.a;

import org.dromara.sms4j.provider.factory.BaseProviderFactory;

/**
 * 新增厂商的工厂实现{@link ASmsImpl}
 * 这个类就是获取LocalSmsImpl的工厂
 *
 * @author huangchengxing
 */
public class AFactory implements BaseProviderFactory<ASmsImpl, AConfig> {

    private static final AFactory INSTANCE = new AFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static AFactory instance() {
        return INSTANCE;
    }

    @Override
    public ASmsImpl createSms(AConfig aConfig) {
        return new ASmsImpl(aConfig);
    }

    @Override
    public Class<AConfig> getConfigClass() {
        return AConfig.class;
    }

    @Override
    public String getSupplier() {
        return "a";
    }
}
