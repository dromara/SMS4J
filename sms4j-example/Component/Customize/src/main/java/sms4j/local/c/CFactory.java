package sms4j.local.c;

import org.dromara.sms4j.provider.factory.BaseProviderFactory;

/**
 * 新增厂商的工厂实现{@link CSmsImpl}
 * 这个类就是获取LocalSmsImpl的工厂
 *
 * @author huangchengxing
 */
public class CFactory implements BaseProviderFactory<CSmsImpl, CConfig> {

    private static final CFactory INSTANCE = new CFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static CFactory instance() {
        return INSTANCE;
    }

    @Override
    public CSmsImpl createSms(CConfig aConfig) {
        return new CSmsImpl(aConfig);
    }

    @Override
    public Class<CConfig> getConfigClass() {
        return CConfig.class;
    }

    @Override
    public String getSupplier() {
        return "c";
    }
}
