package org.dromara.sms4j.yunpian.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.dromara.sms4j.yunpian.service.YunPianSmsImpl;

@Data
@EqualsAndHashCode(callSuper = true)
public class YunpianConfig extends BaseConfig {

    /**
     * 短信发送后将向这个地址推送(运营商返回的)发送报告
     */
    private String callbackUrl;

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.YUNPIAN;
    }

}
