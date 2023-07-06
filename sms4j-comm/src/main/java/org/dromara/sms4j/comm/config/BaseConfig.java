package org.dromara.sms4j.comm.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 短信配置属性基类
 *
 * @author Charles7c
 * @since 2023/4/20 23:03
 */
@Data
@SuperBuilder
@NoArgsConstructor
public class BaseConfig {

    /**
     * Access Key
     */
    private String accessKeyId;

    /**
     * Access Key Secret
     */
    private String accessKeySecret;

    /**
     * 短信签名
     */
    private String signature;

    /**
     * 模板 ID
     */
    private String templateId;

    /**
     * 权重
     * */
    @Builder.Default
    private Integer weight = 1;
}
