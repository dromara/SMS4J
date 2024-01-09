package org.dromara.email.jakarta.comm.config;

import lombok.Data;

@Data
public class MailImapConfig {

    /** imap服务地址*/
    private String imapServer;

    /** 要监听的邮箱账号*/
    private String username;

    /** 要监听的邮箱授权码或密码*/
    private String accessToken;

    /** 监听周期（秒）*/
    private Integer cycle = 5;
}
