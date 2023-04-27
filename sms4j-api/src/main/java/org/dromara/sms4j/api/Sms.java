package org.dromara.sms4j.api;

import org.dromara.sms4j.comm.config.BaseConfig;

/**
 * @author <a href = "mailto:kamtohung@gmail.com">KamTo Hung</a>
 */
public interface Sms<C extends BaseConfig> {

    void create(C config);

    SmsBlend refresh(C config);

}
