package org.dromara.sms4j.api.proxy.aware;

import java.util.Map;

/**
 * 给InterceptorStrate使用的厂商配置感知接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface InterceptorStrategySmsBlendConfigAware {
    void setSmsBlendsConfig(Map<String, Map<String, Object>> blends);
}
