package org.dromara.sms4j.cloopen.config;

import lombok.Data;

/**
 * 容联云短信配置属性
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@Data
public class CloopenConfig {

    /** Access Key */
    private String accessKeyId;

    /** Access Key Secret */
    private String accessKeySecret;

    /** 模板 ID */
    private String templateId;

    /** 应用 ID */
    private String appId;

    /** Rest URL 域名 */
    private String serverIp;

    /** Rest URL 端口 */
    private String serverPort;
}
