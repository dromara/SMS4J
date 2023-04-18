package org.dromara.sms4j.cloopen.config;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 容联云短信配置属性
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@Data
@Accessors(chain = true)
public class CloopenConfig {

    /**
     * Access Key
     */
    private String accessKeyId;

    /**
     * Access Key Secret
     */
    private String accessKeySecret;

    /**
     * 模板 ID
     */
    private String templateId;

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
