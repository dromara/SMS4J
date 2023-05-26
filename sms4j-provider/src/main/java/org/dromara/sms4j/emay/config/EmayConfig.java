package org.dromara.sms4j.emay.config;

import lombok.Builder;
import lombok.Data;
import org.dromara.sms4j.api.universal.SupplierConfig;

/**
 * @author Richard
 * @date 2023-04-11 12:00
 */
@Data
@Builder
public class EmayConfig implements SupplierConfig {
    /** appKey*/
    private String appId ;
    /** appSecret */
    private String secretKey ;
    /** APP接入地址*/
    private String requestUrl;
}
