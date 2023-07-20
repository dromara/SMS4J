package org.dromara.sms4j.emay.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.api.universal.SupplierConfig;

/**
 * @author Richard
 * @date 2023-04-11 12:00
 */
@Data
@SuperBuilder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class EmayConfig implements SupplierConfig {
    /** appKey*/
    private String appId ;
    /** appSecret */
    private String secretKey ;
    /** APP接入地址*/
    private String requestUrl;

    /**
     * 配置标识名 如未配置取对应渠道名例如 Alibaba
     *
     * @since 3.0.0
     */
    private String configId;
}
