package org.dromara.sms4j.core.proxy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.provider.config.SmsConfig;

import java.util.Map;
import java.util.Objects;

/**
 * 环境信息持有
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EnvironmentHolder {

    private static SmsConfig smsConfig = null;
    private static Map<String, Map<String, Object>> blends = null;

    public static void frozen(SmsConfig smsConfig, Map<String, Map<String, Object>> blends) {
        if (Objects.nonNull(EnvironmentHolder.smsConfig) || Objects.nonNull(EnvironmentHolder.blends)) {
            log.warn("The environmental information has been loaded and cannot be overwritten!");
            return;
        }
        EnvironmentHolder.smsConfig = smsConfig;
        EnvironmentHolder.blends = blends;
    }

    /**
     * 配置信息
     *
     * @return 配置
     */
    static SmsConfig getSmsConfig() {
        return smsConfig;
    }

    /**
     * 获取短信配置信息
     *
     * @return 短信配置信息
     */
    static Map<String, Map<String, Object>> getBlends() {
        return blends;
    }
}
