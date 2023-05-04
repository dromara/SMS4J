package org.dromara.sms4j.jdcloud.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.comm.config.BaseConfig;

/**
 * 京东云短信配置属性
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class JdCloudConfig extends BaseConfig {

    /**
     * 地域信息
     */
    
    private String region = "cn-north-1";
}
