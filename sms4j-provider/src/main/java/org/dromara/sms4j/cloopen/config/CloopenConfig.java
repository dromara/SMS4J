package org.dromara.sms4j.cloopen.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.comm.config.BaseConfig;

/**
 * 容联云短信配置属性
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class CloopenConfig extends BaseConfig {

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * REST API Base URL
     */
    
    private String baseUrl = "https://app.cloopen.com:8883/2013-12-26";

    /**
     * Rest URL 域名
     *
     * @deprecated v2.0.1
     * @see baseUrl
     */
    @Deprecated
    private String serverIp;

    /**
     * Rest URL 端口
     *
     * @deprecated v2.0.1
     * @see baseUrl
     */
    @Deprecated
    private String serverPort;
}
