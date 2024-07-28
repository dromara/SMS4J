package org.dromara.sms4j.mas.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * <p>类名: MasConfig
 * <p>说明：中国移动 云MAS
 *
 * @author :bleachtred
 * 2024/4/22  13:40
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class MasConfig extends BaseConfig {

    /**
     * 企业名称
     */
    private String ecName;

    /**
     * 请求地址
     */
    private String requestUrl = "http://112.35.1.155:1992/sms/";

    /**
     * 接口名称
     */
    private String action = "tmpsubmit";

    /**
     * 扩展码
     */
    private String addSerial;

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.MAS;
    }
}
