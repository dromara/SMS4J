package org.dromara.sms4j.yunpian.config;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class YunpianConfig {
    /**
     * 账号唯一标识
     */
    private String apikey;

    /**
     * 短信发送后将向这个地址推送(运营商返回的)发送报告
     */
    private String callbackUrl;

    /**
     * 模板Id
     */
    private String templateId;

    /**
     * 模板变量名称
     */
    private String templateName;

}
