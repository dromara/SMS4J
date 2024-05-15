package org.dromara.sms4j.api.proxy.aware;


/**
 * 给InterceptorStrate使用的系统配置感知接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface InterceptorStrategySmsConfigAware {
    void setSmsConfig(Object config);
}
