package org.dromara.sms4j.dingzhong.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * @author Wind
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class DingZhongConfig extends BaseConfig {

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * 请求地址
     */
    private String requestUrl = "http://demoapi.321sms.com:8201";

    /**
     * 基础接口名称
     */
    private String baseAction = "Sms/SendSms";

    /**
     * 模板接口名称
     */
    private String templateAction = "Sms/SendTemplateSms";

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.DINGZHONG;
    }

}
