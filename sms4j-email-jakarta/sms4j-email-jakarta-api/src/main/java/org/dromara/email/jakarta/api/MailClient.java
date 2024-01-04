package org.dromara.email.jakarta.api;

import org.dromara.email.jakarta.comm.entity.MailMessage;

public interface MailClient {

    /**
     * 发送邮件，可以通过对象构造群体发送或者单体发送，取决于添加进去的收件人，同时可以添加
     * 密送人，抄送人，附件等参数
     * @param mailMessage 发送邮件参数对象
     * @author :Wind
     */
    void send(MailMessage mailMessage);
}
