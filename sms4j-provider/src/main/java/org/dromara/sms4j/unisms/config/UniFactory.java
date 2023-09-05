package org.dromara.sms4j.unisms.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.dromara.sms4j.unisms.core.Uni;
import org.dromara.sms4j.unisms.service.UniSmsImpl;

/**
 * UniSmsConfig
 * <p>合一短信建造对象
 * @author :Wind
 * 2023/4/8  15:46
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UniFactory extends AbstractProviderFactory<UniSmsImpl, UniConfig> {

    private static final UniFactory INSTANCE = new UniFactory();

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static UniFactory instance() {
        return INSTANCE;
    }


    /** 短信配置*/
    private void buildSms(UniConfig uniConfig){
        if (uniConfig.getIsSimple()){
            Uni.init(uniConfig.getAccessKeyId());
        }else {
            Uni.init(uniConfig.getAccessKeyId(),uniConfig.getAccessKeySecret());
        }
    }

    /**
     *  createUniSms
     * <p>建造一个短信实现对像
     * @author :Wind
    */
    @Override
    public UniSmsImpl createSms(UniConfig uniConfig){
        this.buildSms(uniConfig);
        return new UniSmsImpl(uniConfig);
    }

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return UniSmsImpl.SUPPLIER;
    }

}
