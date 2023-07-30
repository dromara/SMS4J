package org.dromara.sms4j.aliyun.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.aliyun.service.AlibabaSmsImpl;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * @author Wind
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class AlibabaConfig extends BaseConfig {

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * 请求地址
     */
    @Builder.Default
    private String requestUrl = "dysmsapi.aliyuncs.com";

    /**
     * 接口名称
     */
    @Builder.Default
    private String action = "SendSms";

    /**
     * 接口版本号
     */
    @Builder.Default
    private String version = "2017-05-25";

    /**
     * 地域信息默认为 cn-hangzhou
     */
    @Builder.Default
    private String regionId = "cn-hangzhou";

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return AlibabaSmsImpl.SUPPLIER;
    }

}
