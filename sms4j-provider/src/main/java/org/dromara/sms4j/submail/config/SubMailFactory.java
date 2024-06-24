package org.dromara.sms4j.submail.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.submail.service.SubMailSmsImpl;

/**
 * <p>类名: SubMailFactory
 *
 * @author :bleachtred
 * 2024/6/22  13:59
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SubMailFactory extends AbstractProviderFactory<SubMailSmsImpl, SubMailConfig> {

    private static final SubMailFactory INSTANCE = new SubMailFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static SubMailFactory instance() {
        return INSTANCE;
    }

    /**
     * <p> 建造一个短信实现对像
     *
     * @author :bleachtred
     */
    @Override
    public SubMailSmsImpl createSms(SubMailConfig config) {
        return new SubMailSmsImpl(config);
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.MY_SUBMAIL;
    }

}
