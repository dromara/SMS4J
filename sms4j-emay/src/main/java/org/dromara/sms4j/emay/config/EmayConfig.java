package org.dromara.sms4j.emay.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Richard
 * @date 2023-04-11 12:00
 */
@Data
@Accessors(chain = true)
public class EmayConfig {
    /** appKey*/
    private String appId ;
    /** appSecret */
    private String secretKey ;
    /** APP接入地址*/
    private String requestUrl;
}
