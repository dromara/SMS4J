package org.dromara.sms4j.baidu.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.baidu.service.BaiduSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;

/**
 * <p>类名: BaiduFactory
 * <p>说明：百度智能云 sms
 *
 * @author :bleachtred
 * 2024/4/25  13:40
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BaiduFactory extends AbstractProviderFactory<BaiduSmsImpl, BaiduConfig> {

    private static final BaiduFactory INSTANCE = new BaiduFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static BaiduFactory instance() {
        return INSTANCE;
    }

    /**
     * createSms
     * <p> 建造一个短信实现对像
     *
     * @author :bleachtred
     */
    @Override
    public BaiduSmsImpl createSms(BaiduConfig baiduConfig) {
        return new BaiduSmsImpl(baiduConfig);
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.BAIDU;
    }

}
