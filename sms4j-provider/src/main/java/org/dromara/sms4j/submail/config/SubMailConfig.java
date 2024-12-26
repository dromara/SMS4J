package org.dromara.sms4j.submail.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * <p>类名: SubMailConfig
 * <p>说明： SUBMAIL短信差异配置
 *
 * @author :bleachtred
 * 2024/6/22  13:59
 **/
@Data
@EqualsAndHashCode(callSuper = true)
public class SubMailConfig extends BaseConfig {

    /**
     * 请求地址
     */
    private String host = Constant.HTTPS_PREFIX + "api-v4.mysubmail.com/sms/";

    /**
     * 接口名称
     * 短信发送 send.json
     * 短信模板发送 xsend.json
     * 短信一对多发送 multisend.json
     * 短信模板一对多发送 multixsend.json
     * 短信批量群发 batchsend.json
     * 短信批量模板群发 batchxsend.json
     */
    private String action = "send.json";

    /**
     * MD5 或 SHA-1 默认MD5 填写任意值，不为即为 密匙明文验证模式
     */
    private String signType = "md5";

    /**
     * signature加密计算方式
     * (当sign_version传2时，会忽略某些字段)
     */
    private String signVersion = "2";

    /**
     * 获取供应商
     *
     * @since 3.0.0
     */
    @Override
    public String getSupplier() {
        return SupplierConstant.MY_SUBMAIL;
    }
}
