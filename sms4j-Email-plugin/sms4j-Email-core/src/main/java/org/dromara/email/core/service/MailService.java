package org.dromara.email.core.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.dromara.email.api.MailClient;
import org.dromara.email.api.Parameter;
import org.dromara.email.comm.errors.MailException;
import org.dromara.email.comm.utils.HtmlUtil;
import org.dromara.email.comm.utils.ZipUtils;
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
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
        sendEmail(mailAddress, title, body, null);
    }

    @Override
    public void sendMail(List<String> mailAddress, String title, String body) {
        sendEmail(mailAddress, title, body, null);
    }

    @Override
    public void sendEmail(String mailAddress, String title, String body, Map<String, String> files) {
        sendEmail(Collections.singletonList(mailAddress), title, body, files);
    }

    @Override
    public void sendEmail(String mailAddress, String title, String body, String zipName, Map<String, String> files) {
        try {
            Message message = mailBuild.getMessage();
            message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(Convert.toList(String.class, mailAddress)));
            message.setSubject(title);
            if (StrUtil.isNotBlank(body)) {
                message.setText(body);
            }
            if (files != null && files.size() != 0) {
                Multipart multipart = new MimeMultipart();
                zipFiles(multipart, zipName, files);
                message.setContent(multipart);
            }
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new MailException(e);
        }
    }

    @Override
    public void sendEmail(List<String> mailAddress, String title, String body, Map<String, String> files) {
        try {
            Message message = mailBuild.getMessage();
            message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(mailAddress));
            message.setSubject(title);
            if (StrUtil.isNotBlank(body)) {
                message.setText(body);
            }
            if (files != null && files.size() != 0) {
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
        sendHtml(mailAddress, title, htmlName, parameter, null);
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
    public void sendHtml(String mailAddress, String title, String htmlName, Map<String, String> parameter, Map<String, String> files) {
        sendHtml(mailAddress, title, "", htmlName, parameter, files);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String htmlName, Map<String, String> parameter, Map<String, String> files) {
        sendHtml(mailAddress, title, "", htmlName, parameter, files);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String htmlName, Parameter parameter, Map<String, String> files) {
        sendHtml(mailAddress, title, htmlName, ReflectUtil.getValues(parameter), files);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String htmlName, Parameter parameter, Map<String, String> files) {
        sendHtml(mailAddress, title, htmlName, ReflectUtil.getValues(parameter), files);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Map<String, String> parameter) {
        sendHtml(mailAddress, title, body, htmlName, parameter, null);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, String htmlName, Map<String, String> parameter) {
        sendHtml(mailAddress, title, body, htmlName, parameter, null);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Parameter parameter) {
        sendHtml(Collections.singletonList(mailAddress), title, body, htmlName, parameter);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, String htmlName, Parameter parameter) {
        sendHtml(mailAddress, title, body, htmlName, parameter, null);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Map<String, String> parameter, Map<String, String> files) {
        sendHtml(Collections.singletonList(mailAddress), title, body, htmlName, parameter, files);
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Map<String, String> parameter, String zipName, Map<String, String> files) {
        try {
            Message message = mailBuild.getMessage();
            message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(Convert.toList(String.class, mailAddress)));
            message.setSubject(title);

            Multipart multipart = new MimeMultipart("alternative");
            //读取模板并进行变量替换
            List<String> strings = HtmlUtil.replacePlaceholder(HtmlUtil.readHtml(htmlName), parameter);
            //拼合HTML数据
            String htmlData = HtmlUtil.pieceHtml(strings);
            if (StrUtil.isNotBlank(body)) {
                // 创建文本正文部分
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body);
                multipart.addBodyPart(textPart);
            }
            //添加附件
            if (MapUtil.isNotEmpty(files)) {
                zipFiles(multipart, zipName, files);
            }
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlData, "text/html;charset=UTF-8");
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException | IOException e) {
            throw new MailException(e);
        }
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Parameter parameter, String zipName, Map<String, String> files) {
        sendHtml(mailAddress, title, body,htmlName, ReflectUtil.getValues(parameter),zipName,files );
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, String htmlName, Map<String, String> parameter, Map<String, String> files) {
        try {
            Message message = mailBuild.getMessage();
            message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(mailAddress));
            message.setSubject(title);

            Multipart multipart = new MimeMultipart("alternative");
            //读取模板并进行变量替换
            List<String> strings = HtmlUtil.replacePlaceholder(HtmlUtil.readHtml(htmlName), parameter);
            //拼合HTML数据
            String htmlData = HtmlUtil.pieceHtml(strings);
            if (!body.isEmpty()) {
                // 创建文本正文部分
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body);
                multipart.addBodyPart(textPart);
            }
            //添加附件
            if (files != null && files.size() != 0) {
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
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Parameter parameter, Map<String, String> files) {
        sendHtml(Collections.singletonList(mailAddress), title, body, htmlName, parameter, files);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, String htmlName, Parameter parameter, Map<String, String> files) {
        sendHtml(mailAddress, title, body, htmlName, ReflectUtil.getValues(parameter), files);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, InputStream html, Map<String, String> parameter, Map<String, String> files) {
        try {
            Message message = mailBuild.getMessage();
            message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(mailAddress));
            message.setSubject(title);

            Multipart multipart = new MimeMultipart("alternative");
            //读取模板并进行变量替换
            List<String> strings = HtmlUtil.replacePlaceholder(HtmlUtil.readHtml(html), parameter);
            //拼合HTML数据
            String htmlData = HtmlUtil.pieceHtml(strings);
            if (!body.isEmpty()) {
                // 创建文本正文部分
                MimeBodyPart textPart = new MimeBodyPart();
                textPart.setText(body);
                multipart.addBodyPart(textPart);
            }
            //添加附件
            if (files != null && files.size() != 0) {
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
    public void sendHtml(List<String> mailAddress, String title, String body, InputStream html, Parameter parameter, Map<String, String> files) {
        sendHtml(mailAddress, title, body, html, ReflectUtil.getValues(parameter), files);
    }

    private void forFiles(Multipart multipart, Map<String, String> files) throws MessagingException {
        for (Map.Entry<String, String> entry : files.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            // 设置附件消息部分
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(v);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(k);
            multipart.addBodyPart(messageBodyPart);
        }
    }

    private void zipFiles(Multipart multipart, String zipName, Map<String, String> files) throws MessagingException, IOException {
        // 设置附件消息部分
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ZipUtils.zipFilePip(files, os);
        ByteArrayInputStream stream = IoUtil.toStream(os);
        DataSource source = new ByteArrayDataSource(stream, "application/octet-stream");
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(StrUtil.isNotBlank(zipName) ? zipName : UUID.fastUUID() + ".zip");
        multipart.addBodyPart(messageBodyPart);
    }
}
