package org.dromara.sms4j.huawei.entity;

import lombok.Data;

/**
 * SmsId
 * <p> 短信ID列表
 *
 * @author :Wind
 * 2023/3/31  21:55
 **/
@Data
public class SmsId {

    /** 短信的唯一标识*/
    private String smsMsgId;

    /** 短信发送方的号码*/
    private String from;

    /** 短信接收方的号码*/
    private String originTo;

    /** 短信状态码*/
    private String status;

    /** 短信资源的创建时间，即短信平台接收到用户发送短信请求的时间，为UTC时间*/
    private String createTime;

}
