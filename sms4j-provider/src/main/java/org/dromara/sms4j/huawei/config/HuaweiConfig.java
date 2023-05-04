package org.dromara.sms4j.huawei.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.config.BaseConfig;

@EqualsAndHashCode(callSuper = true)
@Data
public class HuaweiConfig extends BaseConfig {

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
