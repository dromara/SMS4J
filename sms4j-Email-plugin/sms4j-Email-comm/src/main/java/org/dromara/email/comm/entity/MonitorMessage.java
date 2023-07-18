package org.dromara.email.comm.entity;

import lombok.Data;

import javax.mail.Multipart;
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
