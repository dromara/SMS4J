package org.dromara.sms4j.aliyun.config;

import lombok.Data;

@Data
public class AlibabaConfig {

    /** accessKey*/
    private String accessKeyId;
    /** 访问键秘钥 */
    private String accessKeySecret;
    /** 短信签名*/
    private String signature;
    /** 模板Id*/
    private String templateId;
    /** 模板变量名称*/
    private String templateName;
    /** 请求地址*/
    private String requestUrl = "dysmsapi.aliyuncs.com";

}
