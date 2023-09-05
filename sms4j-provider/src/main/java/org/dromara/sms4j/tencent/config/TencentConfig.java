package org.dromara.sms4j.tencent.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.dromara.sms4j.tencent.service.TencentSmsImpl;

@Data
@EqualsAndHashCode(callSuper = true)
public class TencentConfig extends BaseConfig {

    /**
     * 短信sdkAppId
     */
    private String sdkAppId;

    /**
     * 地域信息默认为 ap-guangzhou
     */
    private String territory = "ap-guangzhou";

    /**
     * 请求超时时间
     */
    private Integer connTimeout = 60;
    /** 请求地址*/
    private String requestUrl = "sms.tencentcloudapi.com";
    /**
     * 接口名称
     */
    private String action = "SendSms";

    /**
     * 接口版本
     */
    private String version = "2021-01-11";

    /**
     * 服务名
     */
    private String service = "sms";

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.TENCENT;
    }

}
