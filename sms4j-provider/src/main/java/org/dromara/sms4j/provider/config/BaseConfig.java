package org.dromara.sms4j.provider.config;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.api.universal.SupplierConfig;

/**
 * 短信配置属性基类
 *
 * @author Charles7c
 * @since 2023/4/20 23:03
 */
@Data
@SuperBuilder
@NoArgsConstructor
public abstract class BaseConfig implements SupplierConfig {

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
     */
    @Builder.Default
    private Integer weight = 1;

    /**
     * 配置标识名 如未配置取对应渠道名例如 Alibaba
     *
     * @since 3.0.0
     */
    private String configId;

}
