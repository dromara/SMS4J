package org.dromara.sms4j.yunpian.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.dromara.sms4j.yunpian.service.YunPianSmsImpl;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class YunPianFactory extends AbstractProviderFactory<YunPianSmsImpl, YunpianConfig> {

    private static final YunPianFactory INSTANCE = new YunPianFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static YunPianFactory instance() {
        return INSTANCE;
    }

    /**
     * 建造一个云片短信实现
     */
    @Override
    public YunPianSmsImpl createSms(YunpianConfig yunpianConfig){
        return new YunPianSmsImpl(yunpianConfig);
    }

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.YUNPIAN;
    }

}
