package org.dromara.sms4j.huyi.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * 互亿无线-自定义短信发送-配置
 *
 * @author 初心
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class HuYiConfig extends BaseConfig {

    /**
     * 请求地址
     */
    private String baseUrl = Constant.HTTPS_PREFIX + "106.ihuyi.com/webservice/sms.php";

    /**
     * 单条链接
     */
    private String singleMsgUrl = "?method=Submit";

    /**
     * 批量链接
     */
    private String massMsgUrl = "?method=SubmitBatch";

    /**
     * 默认关闭 MD5 加密，默认使用 API KEY 加密
     */
    private Boolean enableMd5 = false;

    @Override
    public String getSupplier() {
        return SupplierConstant.HUYI;
    }
}
