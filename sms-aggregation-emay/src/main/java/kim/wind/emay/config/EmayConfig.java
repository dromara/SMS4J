package kim.wind.emay.config;

import lombok.Data;

/**
 * @author Richard
 * @date 2023-04-11 12:00
 */
@Data
public class EmayConfig {
    /** appKey*/
    private String appId ;
    /** appSecret */
    private String secretKey ;
    /** APP接入地址*/
    private String requestUrl;
}
