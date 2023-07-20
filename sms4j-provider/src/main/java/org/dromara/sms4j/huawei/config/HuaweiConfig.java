package org.dromara.sms4j.huawei.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.api.universal.SupplierConfig;

@Data
@SuperBuilder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
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

    /**
     * 配置标识名 如未配置取对应渠道名例如 Alibaba
     *
     * @since 3.0.0
     */
    private String configId;
}
