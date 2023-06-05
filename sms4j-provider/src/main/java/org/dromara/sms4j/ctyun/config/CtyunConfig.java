package org.dromara.sms4j.ctyun.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.config.BaseConfig;

/**
 * <p>类名: CtyunConfig
 * <p>说明： 天翼云短信差异配置
 *
 * @author :bleachhtred
 * 2023/5/12  15:06
 **/
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CtyunConfig extends BaseConfig implements SupplierConfig {

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * 请求地址
     */
    @Builder.Default
    private String requestUrl = "https://sms-global.ctapi.ctyun.cn/sms/api/v1";

    /**
     * 接口名称
     */
    @Builder.Default
    private String action = "SendSms";
}
