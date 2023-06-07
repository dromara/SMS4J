package org.dromara.email.comm.config;

import lombok.Data;

/**
 * MailSmtpConfig
 * <p> smtp协议配置文件
 * @author :Wind
 * 2023/6/7  21:19
 **/
@Data
public class MailSmtpConfig {
    /**
     * 端口号
     * */
    private String host;

    /**
     * 服务器地址
     * */
    private String smtpServer;

    /**
     * 账号
     * */
    private String username;

    /**
     * 密码
     * */
    private String password;
}
