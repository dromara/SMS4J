package org.dromara.sms.huawei.config;

import lombok.Data;

@Data
public class HuaweiConfig {

    /** appKey*/
    private String appKey ;
    /** appSecret */
    private String appSecret ;
    /** 短信签名*/
    private String signature;
    /** 国内短信签名通道号*/
    private String sender;
    /** 模板Id*/
    private String templateId;
    /** 短信状态报告接收地*/
    private String statusCallBack;
    /** APP接入地址*/
    private String url;
}
