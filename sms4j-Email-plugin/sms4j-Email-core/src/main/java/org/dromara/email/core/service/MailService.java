package org.dromara.email.core.service;

import lombok.extern.slf4j.Slf4j;
import org.dromara.email.api.Blacklist;
import org.dromara.email.api.MailClient;
import org.dromara.email.api.Parameter;
import org.dromara.email.comm.errors.MailException;
import org.dromara.email.comm.utils.BaseUtil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

@Slf4j
public class MailService implements MailClient {

    private MailBuild mailBuild;

    private MailService(MailBuild mailBuild) {
        this.mailBuild = mailBuild;
    }

    public static MailClient NewMailService(MailBuild mailBuild) {
        return new MailService(mailBuild);
    }

    @Override
    public void sendMail(String mailAddress, String title, String body) {
        sendEmail(mailAddress,title,body);
    }

    @Override
    public void sendMail(List<String> mailAddress, String title, String body) {
        sendEmail(mailAddress,title,body);
    }

    @Override
    public void sendEmail(String mailAddress, String title, String body, String... files) {
        sendEmail(Collections.singletonList(mailAddress),title,body,files);
    }

    @Override
    public void sendEmail(List<String> mailAddress, String title, String body, String... files) {
        try {
            Message message = mailBuild.getMessage();
            message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(mailAddress));
            message.setSubject(title);
            message.setText(body);
            Multipart multipart = new MimeMultipart();
            for (String file : files) {
                // 设置附件消息部分
                MimeBodyPart messageBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(file);
                messageBodyPart.setDataHandler(new DataHandler(source));
                messageBodyPart.setFileName(BaseUtil.getPathName(file));
                multipart.addBodyPart(messageBodyPart);
            }
            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }

    @Override
    public void sendHtml(String mailAddress, String title, String htmlName, Map<String, Object> parameter) {

    }

    @Override
    public void sendHtml(String mailAddress, String title, String htmlName, Parameter parameter) {

    }

    @Override
    public void sendHtml(String mailAddress, String title, String htmlName, Map<String, Object> parameter, String... files) {

    }

    @Override
    public void sendHtml(String mailAddress, String title, String htmlName, Parameter parameter, String... files) {

    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Map<String, Object> parameter) {

    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Parameter parameter) {

    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Map<String, Object> parameter, String... files) {

    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Parameter parameter, String... files) {

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

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            String filename = "file.txt";
            DataSource source = new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filename);
            multipart.addBodyPart(messageBodyPart);


            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }
}
