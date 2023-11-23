package org.dromara.sms4j.example.zhangjun;

import lombok.NoArgsConstructor;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;

/**
 *
 * <p> 掌骏短信
 *
 * @author :4n
 * 2023/10/31  14:54
 **/
@NoArgsConstructor
public class ZhangJunFactory extends AbstractProviderFactory<ZhangJunSmsImpl, ZhangJunConfig> {

    @Override
    public ZhangJunSmsImpl createSms(ZhangJunConfig ZhangJunConfig) {
        return new ZhangJunSmsImpl(ZhangJunConfig);
    }

    @Override
    public String getSupplier() {
        return "zhangjun";
    }

}
