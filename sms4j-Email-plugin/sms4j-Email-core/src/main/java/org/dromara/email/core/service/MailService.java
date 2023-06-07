package org.dromara.email.core.service;

import org.dromara.email.api.MailClient;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Properties;

public class MailService implements MailClient {

    private MailService() {
    }

    public static MailClient NewMailService() {
        return new MailService();
    }

    @Override
    public void sendMail(String mailAddress, String Title, String body) {
        String smtpServer = "smtp.qq.com";
        String username = "wzsf1810@qq.com";
        String password = "xophzbzswkzkiacb";
        String fromAddress = "wzsf1810@qq.com";
        String toAddress = "291203727@qq.com";
        int port = 465; // SMTP服务器的端口号

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.enable", "true");
//        props.put("mail.smtp.ssl.socketFactory", new MailSSLSocketFactory());

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(Title);
            message.setText(body);

            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }

    @Override
    public void sendHtml(String mailAddress, String Title, String body) {
        String smtpServer = "smtp.qq.com";
        String username = "wzsf1810@qq.com";
        String password = "xophzbzswkzkiacb";
        String fromAddress = "wzsf1810@qq.com";
        String toAddress = "291203727@qq.com";
        int port = 465; // SMTP服务器的端口号

        Properties props = new Properties();
        props.put("mail.smtp.host", smtpServer);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.ssl.enable", "true");
//        props.put("mail.smtp.ssl.socketFactory", new MailSSLSocketFactory());

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromAddress));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toAddress));
            message.setSubject(Title);
            // 创建 MimeMultipart 对象
            MimeMultipart multipart = new MimeMultipart("related");
            // 创建 MimeBodyPart 对象，并将 HTML 内容添加到 MimeMultipart 中
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(body, "text/html;charset=UTF-8");
            message.setContent(multipart);
            multipart.addBodyPart(htmlPart);
            Transport.send(message);

            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            System.out.println("Failed to send email: " + e.getMessage());
        }
    }
}
