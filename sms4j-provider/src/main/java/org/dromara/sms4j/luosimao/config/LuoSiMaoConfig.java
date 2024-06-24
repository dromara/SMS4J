package org.dromara.sms4j.luosimao.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * <p>类名: LuoSiMaoConfig
 * <p>说明： 螺丝帽短信差异配置
 *
 * @author :bleachtred
 * 2024/6/21  23:59
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class LuoSiMaoConfig extends BaseConfig {

    /**
     * 请求地址
     */
    private String host = Constant.HTTPS_PREFIX + "sms-api.luosimao.com/v1/";

    /**
     * 接口名称
     * 发送短信接口详细 send.json
     * 批量发送接口详细 send_batch.json
     * 查询账户余额 status.json
     */
    private String action = "send.json";

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.LUO_SI_MAO;
    }
}
