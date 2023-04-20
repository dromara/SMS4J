package org.dromara.sms4j.unisms.config;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
public class UniConfig {
    /** 访问键标识*/
    private String accessKeyId;
    /** 访问键秘钥 简易模式不需要配置*/
    private String accessKeySecret;
    /** 是否为简易模式*/
    private Boolean isSimple = true;
    /** 短信签名*/
    private String signature;
    /** 模板Id*/
    private String templateId;
    /** 模板变量名称*/
    private String templateName;
}
