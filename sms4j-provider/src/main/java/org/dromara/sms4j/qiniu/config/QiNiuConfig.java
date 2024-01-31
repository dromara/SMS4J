package org.dromara.sms4j.qiniu.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * @author Administrator
 * @Date: 2024/1/30 15:56 30
 * @描述: QiNiuConfig
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class QiNiuConfig extends BaseConfig {


    private String baseUrl = "https://sms.qiniuapi.com";

    private String templateName;

    private String singleMsgUrl = "/v1/message/single";

    private String massMsgUrl = "/v1/message";

    private String signatureId;

    @Override
    public String getSupplier() {
        return SupplierConstant.QINIU;
    }
}
