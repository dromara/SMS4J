package org.dromara.sms4j.jdcloud.config;

import lombok.Data;

/**
 * 京东云短信配置属性
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
@Data
public class JdCloudConfig {

    /** Access Key */
    private String accessKeyId;

    /** Access Key Secret */
    private String accessKeySecret;

    /** 短信签名 */
    private String signature;

    /** 模板 ID */
    private String templateId;

    /** 地域信息 */
    private String region = "cn-north-1";
}
