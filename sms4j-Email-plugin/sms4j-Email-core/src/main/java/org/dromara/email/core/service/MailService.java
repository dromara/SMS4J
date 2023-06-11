package org.dromara.email.core.service;

import org.dromara.email.api.MailClient;
import org.dromara.email.api.Parameter;
import org.dromara.email.comm.errors.MailException;
import org.dromara.email.comm.utils.BaseUtil;
import org.dromara.email.comm.utils.HtmlUtil;
import org.dromara.email.core.ReflectUtil;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Transport;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
        sendEmail(mailAddress, title, body);
    }

    @Override
    public void sendMail(List<String> mailAddress, String title, String body) {
        sendEmail(mailAddress, title, body);
    }

    @Override
    public void sendEmail(String mailAddress, String title, String body, String... files) {
        sendEmail(Collections.singletonList(mailAddress), title, body, files);
    }

    @Override
    public void sendEmail(List<String> mailAddress, String title, String body, String... files) {
        try {
            Message message = mailBuild.getMessage();
            message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(mailAddress));
            message.setSubject(title);
            if (Objects.isNull(body) ||body.isEmpty()){
                message.setText(body);
            }
            if (files.length != 0){
                Multipart multipart = new MimeMultipart();
                forFiles(multipart, files);
                message.setContent(multipart);
            }
            Transport.send(message);
        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }

    @Override
    public void sendHtml(String mailAddress, String title, String htmlName, Map<String, String> parameter) {
        sendHtml(Collections.singletonList(mailAddress), title, htmlName, parameter);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String htmlName, Map<String, String> parameter) {
        sendHtml(mailAddress, title, htmlName, parameter, new String[0]);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String htmlName, Parameter parameter) {
        sendHtml(Collections.singletonList(mailAddress), title, htmlName, parameter);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String htmlName, Parameter parameter) {
        sendHtml(mailAddress, title, htmlName, ReflectUtil.getValues(parameter));
    }

    @Override
    public void sendHtml(String mailAddress, String title, String htmlName, Map<String, String> parameter, String... files) {
        sendHtml(mailAddress, title, "", htmlName, parameter, files);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String htmlName, Map<String, String> parameter, String... files) {
        sendHtml(mailAddress, title, "", htmlName, parameter, files);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String htmlName, Parameter parameter, String... files) {
        sendHtml(mailAddress, title, htmlName, ReflectUtil.getValues(parameter), files);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String htmlName, Parameter parameter, String... files) {
        sendHtml(mailAddress, title, htmlName, ReflectUtil.getValues(parameter), files);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Map<String, String> parameter) {
        sendHtml(mailAddress, title, body, htmlName, parameter, new String[0]);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, String htmlName, Map<String, String> parameter) {
        sendHtml(mailAddress, title, body, htmlName, parameter, new String[0]);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Parameter parameter) {
        sendHtml(Collections.singletonList(mailAddress), title, body, htmlName, parameter);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, String htmlName, Parameter parameter) {
        sendHtml(mailAddress, title, body, htmlName, parameter, new String[0]);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Map<String, String> parameter, String... files) {
        sendHtml(Collections.singletonList(mailAddress), title, body, htmlName, parameter, files);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, String htmlName, Map<String, String> parameter, String... files) {
        try {
            Message message = mailBuild.getMessage();
            message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(mailAddress));
            message.setSubject(title);

            Multipart multipart = new MimeMultipart("alternative");
            //读取模板并进行变量替换
            List<String> strings = HtmlUtil.replacePlaceholder(HtmlUtil.readHtml(htmlName), parameter);
            //拼合HTML数据
            String htmlData = HtmlUtil.pieceHtml(strings);
            if (!body.isEmpty()){
                // 创建文本正文部分
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body);
                multipart.addBodyPart(textPart);
            }
            //添加附件
            if (files.length != 0){
                forFiles(multipart, files);
            }
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlData, "text/html;charset=UTF-8");
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Parameter parameter, String... files) {
        sendHtml(Collections.singletonList(mailAddress), title, body, htmlName, parameter, files);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, String htmlName, Parameter parameter, String... files) {
        sendHtml(mailAddress, title, body, htmlName, ReflectUtil.getValues(parameter), files);
    }

    private void forFiles(Multipart multipart, String[] files) throws MessagingException {
        for (String file : files) {
            // 设置附件消息部分
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(file);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(BaseUtil.getPathName(file));
            multipart.addBodyPart(messageBodyPart);
        }
    }
}
