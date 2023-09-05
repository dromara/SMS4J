package org.dromara.sms4j.netease.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.netease.service.NeteaseSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

/**
 * NeteaseSmsConfig
 * <p> 网易云信短信
 *
 * @author :adam
 * 2023-05-30
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NeteaseFactory extends AbstractProviderFactory<NeteaseSmsImpl, NeteaseConfig> {

    private static final NeteaseFactory INSTANCE = new NeteaseFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static NeteaseFactory instance() {
        return INSTANCE;
    }

    /**
     * 建造一个网易云的短信实现
     */
    @Override
    public NeteaseSmsImpl createSms(NeteaseConfig neteaseConfig) {
        return new NeteaseSmsImpl(neteaseConfig);
    }

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.NETEASE;
    }

}
