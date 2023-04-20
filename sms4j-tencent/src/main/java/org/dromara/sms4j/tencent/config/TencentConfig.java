package org.dromara.sms4j.tencent.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TencentConfig {
    /** 应用accessKey*/
    private String accessKeyId;
    /**
     * 访问键秘钥
     */
    private String accessKeySecret;
    /**
     * 短信签名
     */
    private String signature;
    /**
     * 模板Id
     */
    private String templateId;
    /** 短信sdkAppId*/
    private String sdkAppId;
    /** 地域信息默认为 ap-guangzhou*/
    private String territory ="ap-guangzhou";
    /**请求超时时间 */
    private Integer connTimeout = 60;
}
