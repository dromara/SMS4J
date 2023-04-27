package org.dromara.sms4j.api;

import org.dromara.sms4j.comm.config.BaseConfig;

/**
 * @author <a href = "mailto:kamtohung@gmail.com">KamTo Hung</a>
 */
public interface Sms<C extends BaseConfig> {

    /**
     * init sms
     *
     * @param config config
     */
    void init(C config);

    /**
     * refresh sms
     *
     * @param config config
     * @return sms
     */
    void refresh(C config);

}
