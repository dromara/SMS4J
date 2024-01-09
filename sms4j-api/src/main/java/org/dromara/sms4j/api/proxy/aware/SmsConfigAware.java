package org.dromara.sms4j.api.proxy.aware;


/**
 * 系统配置感知接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface SmsConfigAware {
    void setSmsConfig(Object config);
}
