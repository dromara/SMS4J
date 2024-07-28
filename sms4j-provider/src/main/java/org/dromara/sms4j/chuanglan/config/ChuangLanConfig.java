package org.dromara.sms4j.chuanglan.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * @author YYM
 * @Date: 2024/1/31 17:56 44
 * @描述: ChuangLanConfig
 **/
@EqualsAndHashCode(callSuper = true)
@Data
public class ChuangLanConfig extends BaseConfig {

    /**
     * 基础路径
     */
    private String baseUrl = Constant.HTTPS_PREFIX + "smssh1.253.com/msg";

    /**
     * 短信发送路径
     * 普通短信发送 /v1/send/json 此接口支持单发、群发短信
     * 变量短信发送 /variable/json 单号码对应单内容批量下发
     */
    private String msgUrl = "/variable/json";

    @Override
    public String getSupplier() {
        return SupplierConstant.CHUANGLAN;
    }
}