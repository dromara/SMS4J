package org.dromara.email.api;

public interface MailClient {

    void sendMail(String mailAddress, String Title ,String body);

    void sendHtml(String mailAddress, String Title ,String body);
}
