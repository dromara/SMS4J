package org.dromara.sms4j.yixintong.config;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.provider.config.BaseConfig;

/**
 * <p>类名: YiXintongConfig
 * <p>说明：联通一信通平台配置类
 * <p>所用到配置项：spCode、f、accessKeyId(用户名)、accessKeySecret（接口密钥）、signCode、templateId、retryInterval、maxRetries
 *
 * @author moat
 * @create 2024-07-30 16:50
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class YiXintongConfig extends BaseConfig {

    /**
     *  短信发送请求地址
     */
    private String requestUrl = "https://api.ums86.com:9600/sms/Api/Send.do";

    /**
     *  企业编号
     */
    private String spCode;

    /**
     * 签名编号
     */
    private String signCode;

    /**
     * 提交时检测方式
     * 1 --- 提交号码中有效的号码仍正常发出短信，无效的号码在返回参数faillist中列出
     *
     * 不为1 或该参数不存在 --- 提交号码中只要有无效的号码，那么所有的号码都不发出短信，无效号码在返回参数faillist中列出
     */
    private String f = "1";


    @Override
    public String getSupplier() {
        return SupplierConstant.YIXINTONG;
    }
}
