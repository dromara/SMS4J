package org.dromara.sms4j.api;

import org.dromara.sms4j.comm.config.BaseConfig;

/**
 * @author <a href = "mailto:kamtohung@gmail.com">KamTo Hung</a>
 */
public interface SmsConfig<S extends SmsBlend, C extends BaseConfig> {

    S create(C config);

    S refresh(C config);

}
