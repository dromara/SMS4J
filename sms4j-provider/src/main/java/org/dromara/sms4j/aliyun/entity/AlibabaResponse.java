package org.dromara.sms4j.aliyun.entity;

import lombok.Data;

/**
 * AlibabaResponse
 * <p> 阿里响应参数
 *
 * @author :handy
 * 2023/5/19  16:23
 **/
@Data
public class AlibabaResponse {

    /**
     * 请求返回的结果码
     */
    private String Code;

    /**
     * 请求返回的结果码描述
     */
    private String Message;

    private String RequestId;

    private String BizId;

}
