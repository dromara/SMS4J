package org.dromara.sms4j.api.proxy.aware;

import java.util.Map;

/**
 * 厂商配置感知接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface SmsBlendConfigAware {
    void setSmsBlendsConfig(Map<String, Map<String, Object>> blends);
}
