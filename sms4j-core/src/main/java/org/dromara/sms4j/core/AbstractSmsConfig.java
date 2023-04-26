package org.dromara.sms4j.core;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.SmsConfig;
import org.dromara.sms4j.comm.config.BaseConfig;
import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.core.factory.SmsFactory;

/**
 * @author <a href = "mailto:kamtohung@gmail.com">KamTo Hung</a>
 */
public abstract class AbstractSmsConfig<S extends SmsBlend, C extends BaseConfig> implements SmsConfig<S, C> {

    protected abstract SupplierType type();

    @Override
    public S create(C config) {
        S smsBlend = init(config);
        SmsFactory.register(type(), smsBlend);
        return smsBlend;
    }

    protected abstract S init(C config);

}
