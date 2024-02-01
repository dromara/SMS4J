package org.dromara.email.jakarta.comm.entity;

import jakarta.mail.Multipart;
import lombok.Data;

import java.util.Date;

/**
 * MonitorMessage
 * <p> 监听获取到的邮件内容
 * @author :Wind
 * 2023/7/18  15:59
 **/
@Data
public class MonitorMessage {

    /** 邮件主题*/
    private String title;

    /** 邮件文字内容*/
    private String text;

    /** 解析的html内容*/
    private String htmlText;

    /** 邮件发送时间*/
    private Date sendDate;

    /** 邮件内容（复杂内容）*/
    private Multipart body;

    /** 发送人*/
    private String fromAddress;

    /** 邮件消息编号*/
    private Integer messageIndex;

    /** 接收时间*/
    private Long acceptTime;
}
