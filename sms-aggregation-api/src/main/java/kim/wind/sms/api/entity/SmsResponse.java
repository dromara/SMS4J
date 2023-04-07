package kim.wind.sms.api.entity;

import lombok.Data;

/**
 * <p>类名: SmsResponse
 * <p>说明：  发送短信返回信息
 * @author :Wind
 * 2023/3/25  0:25
 **/
@Data
public class SmsResponse {
    /** 状态码*/
    private String code;
    /** 返回消息*/
    private String message;
    /** 错误码，没有错误信息则为空*/
    private String errorCode;
    /** 错误信息，如果没有错误则为空*/
    private String errMessage;
    /** 回执ID*/
    private String bizId;
    /** 返回体*/
    private Object data;
}
