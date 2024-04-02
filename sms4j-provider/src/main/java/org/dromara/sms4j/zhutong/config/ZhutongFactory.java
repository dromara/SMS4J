package org.dromara.sms4j.zhutong.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.zhutong.service.ZhutongSmsImpl;

/**
 * 助通短信
 * */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZhutongFactory extends AbstractProviderFactory<ZhutongSmsImpl, ZhutongConfig> {
    private static final ZhutongFactory INSTANCE = new ZhutongFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static ZhutongFactory instance() {
        return INSTANCE;
    }

    /**
     * 建造一个助通短信实现
     */
    @Override
    public ZhutongSmsImpl createSms(ZhutongConfig zhutongConfig){
        return new ZhutongSmsImpl(zhutongConfig);
    }

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.ZHUTONG;
    }

}
