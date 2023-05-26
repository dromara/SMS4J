package org.dromara.sms4j.huawei.config;

import lombok.Builder;
import lombok.Data;
import org.dromara.sms4j.api.universal.SupplierConfig;

@Data
@Builder
public class HuaweiConfig implements SupplierConfig {

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
