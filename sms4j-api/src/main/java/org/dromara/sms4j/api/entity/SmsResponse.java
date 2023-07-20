package org.dromara.sms4j.api.entity;

import lombok.Data;

/**
 * <p>类名: SmsResponse
 * <p>说明：  发送短信返回信息
 *
 * @author :Wind
 * 2023/3/25  0:25
 **/
@Data
public class SmsResponse {

    /**
     * 是否成功
     *
     * @since 2.2.0
     */
    private boolean success;

    /**
     * 厂商原返回体
     *
     * @since 2.3.0
     */
    private Object data;

    /**
     * 配置标识名 如未配置取对应渠道名例如 Alibaba
     *
     * @since 3.0.0
     */
    private String configId;
}
