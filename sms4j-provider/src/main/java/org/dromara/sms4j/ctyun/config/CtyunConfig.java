package org.dromara.sms4j.ctyun.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * <p>类名: CtyunConfig
 * <p>说明： 天翼云短信差异配置
 *
 * @author :bleachhtred
 * 2023/5/12  15:06
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class CtyunConfig extends BaseConfig {

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * 请求地址
     */
    private String requestUrl = "https://sms-global.ctapi.ctyun.cn/sms/api/v1";

    /**
     * 接口名称
     */
    private String action = "SendSms";

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.CTYUN;
    }
}
