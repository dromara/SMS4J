package org.dromara.sms4j.jdcloud.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * 京东云短信配置属性
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class JdCloudConfig extends BaseConfig implements SupplierConfig {

    /**
     * 地域信息
     */
    @Builder.Default
    private String region = "cn-north-1";
}
