package org.dromara.email.core.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.dromara.email.api.MailClient;
import org.dromara.email.api.Parameter;
import org.dromara.email.comm.constants.FileConstants;
import org.dromara.email.comm.entity.MailMessage;
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
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class MailService implements MailClient {

    private static Logger logger = Logger.getLogger("mailLog");
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
    public void sendEmail(List<String> mailAddress, String title, String body, Map<String, String> files) {
        sendEmail(mailAddress, title, body, null, null, files);
    }

    @Override
    public void sendEmail(String mailAddress, String title, String body, List<String> cc, List<String> bcc, Map<String, String> files) {
        sendEmail(Collections.singletonList(mailAddress), title, body, cc, bcc, files);
    }

    @Override
    public void sendEmail(MailMessage mailMessage) {
        sendEmail(mailMessage.getMailAddress(),
                mailMessage.getTitle(),
                mailMessage.getBody(),
                mailMessage.getCc(),
                mailMessage.getBcc(),
                mailMessage.getFiles());
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
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Parameter parameter, String zipName, Map<String, String> files) {
        sendHtml(mailAddress, title, body, htmlName, ReflectUtil.getValues(parameter), zipName, files);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, String htmlName, Map<String, String> parameter, Map<String, String> files) {
        send(mailAddress, title, body, HtmlUtil.readHtml(htmlName), parameter, files, null, null);
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
        send(mailAddress, title, body, HtmlUtil.readHtml(html), parameter, files, null, null);
    }

    @Override
    public void sendHtml(List<String> mailAddress, String title, String body, InputStream html, Parameter parameter, Map<String, String> files) {
        sendHtml(mailAddress, title, body, html, ReflectUtil.getValues(parameter), files);
    }

    @Override
    public void sendHtml(List<String> mailAddress,
                         String title,
                         String body,
                         InputStream html,
                         List<String> cc,
                         List<String> bcc,
                         Map<String, String> parameter,
                         Map<String, String> files) {
        send(mailAddress, title, body, HtmlUtil.readHtml(html), parameter, files, cc, bcc);
    }

    @Override
    public void sendHtml(List<String> mailAddress,
                         String title,
                         String body,
                         InputStream html,
                         List<String> cc,
                         List<String> bcc,
                         Parameter parameter,
                         Map<String, String> files) {
        send(mailAddress, title, body, HtmlUtil.readHtml(html), ReflectUtil.getValues(parameter), files, cc, bcc);
    }

    @Override
    public void sendHtml(List<String> mailAddress,
                         String title,
                         String body,
                         String html,
                         List<String> cc,
                         List<String> bcc,
                         Map<String, String> parameter,
                         Map<String, String> files) {
        send(mailAddress, title, body, HtmlUtil.readHtml(html), parameter, files, cc, bcc);
    }

    @Override
    public void sendHtml(List<String> mailAddress,
                         String title,
                         String body,
                         String html,
                         List<String> cc,
                         List<String> bcc,
                         Parameter parameter,
                         Map<String, String> files) {
        send(mailAddress, title, body, HtmlUtil.readHtml(html), ReflectUtil.getValues(parameter), files, cc, bcc);
    }

    @Override
    public void sendHtml(MailMessage mailMessage) {
        if (mailMessage.getHtmlInputStream() == null) {
            sendHtml(mailMessage.getMailAddress(),
                    mailMessage.getTitle(),
                    mailMessage.getBody(),
                    mailMessage.getHtmlPath(),
                    mailMessage.getCc(),
                    mailMessage.getBcc(),
                    mailMessage.getHtmlValues(),
                    mailMessage.getFiles()
            );
        } else {
            sendHtml(mailMessage.getMailAddress(),
                    mailMessage.getTitle(),
                    mailMessage.getBody(),
                    mailMessage.getHtmlInputStream(),
                    mailMessage.getCc(),
                    mailMessage.getBcc(),
                    mailMessage.getHtmlValues(),
                    mailMessage.getFiles()
            );
        }
    }

    private void forFiles(Multipart multipart, Map<String, String> files) throws MessagingException {
        for (Map.Entry<String, String> entry : files.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            // 设置附件消息部分
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            DataSource source;
            if (v.startsWith("http")) {
                byte[] bytes = HttpUtil.downloadBytes(v);
                source = new ByteArrayDataSource(bytes, FileConstants.IO_FILE_TYPE);
            }else {
                source = new FileDataSource(v);
            }
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
        DataSource source = new ByteArrayDataSource(stream, FileConstants.IO_FILE_TYPE);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(StrUtil.isNotBlank(zipName) ? zipName : UUID.fastUUID() + ".zip");
        multipart.addBodyPart(messageBodyPart);
    }

    private void send(List<String> mailAddress,
                      String title,
                      String body,
                      List<String> html,
                      Map<String, String> parameter,
                      Map<String, String> files,
                      List<String> cc,
                      List<String> bcc) {
        try {
            Message message = messageBuild(mailAddress, title, body, html, parameter, files, cc, bcc);
            Transport.send(message);
            logger.info("邮件发送成功！^_^");
        } catch (MessagingException e) {
            // 防止 maxRetries 数值小于0带来的其他问题
            if (mailBuild.getMaxRetries() > 0){
                ReSendList(mailAddress,
                        title,
                        body,
                        html,
                        parameter,
                        files,
                        cc,
                        bcc);
            } else {
                logger.warning(e.getMessage());
                throw new MailException(e);
            }
        }
    }

    @Override
    public void sendEmail(List<String> mailAddress, String title, String body, List<String> cc, List<String> bcc, Map<String, String> files) {
        try {
            Message message = messageBuild(mailAddress, title, body, cc, bcc, files);
            Transport.send(message);
            logger.info("邮件发送成功！^_^");
        } catch (MessagingException e) {
            if (mailBuild.getMaxRetries() > 0) {
                ReSendList(mailAddress, title, body, cc, bcc, files);
            } else {
                logger.warning(e.getMessage());
                throw new MailException(e);
            }
        }
    }

    @Override
    public void sendEmail(String mailAddress, String title, String body, String zipName, Map<String, String> files) {
        try {
            Message message = messageBuild(mailAddress, title, body, zipName, files);
            Transport.send(message);
            logger.info("邮件发送成功！^_^");
        } catch (MessagingException | IOException e) {
            if (mailBuild.getMaxRetries() > 1) {
                ReSend(mailAddress, title, body, zipName, files);
            } else {
                logger.warning(e.getMessage());
                throw new MailException(e);
            }
        }
    }

    @Override
    public void sendHtml(String mailAddress, String title, String body, String htmlName, Map<String, String> parameter, String zipName, Map<String, String> files) {
        try {
            Message message = messageBuild(mailAddress, title, body, htmlName, parameter, zipName, files);
            Transport.send(message);
            logger.info("邮件发送成功！^_^");
        } catch (MessagingException | IOException e) {
            if (mailBuild.getMaxRetries() > 1) {
                ReSend(mailAddress, title, body, htmlName, parameter, zipName, files);
            } else {
                logger.warning(e.getMessage());
                throw new MailException(e);
            }
        }
    }

    private void ReSendList(List<String> mailAddress,
                      String title,
                      String body,
                      List<String> html,
                      Map<String, String> parameter,
                      Map<String, String> files,
                      List<String> cc,
                      List<String> bcc) {
        int maxRetries = mailBuild.getMaxRetries();
        int retryCount = 1; // 初始值为1；则while循环中少发送一次，最后一次发送在判断 retryCount >= maxRetries 这里。
        boolean retryOnFailure = true;

        while (retryOnFailure && retryCount < maxRetries) {
            try {
                logger.warning("邮件第 {" + retryCount + "} 次重新发送");
                Message message;
                if (html != null || parameter != null){
                    message = messageBuild(mailAddress, title, body, html, parameter, files, cc, bcc);
                } else {
                    message = messageBuild(mailAddress, title, body, cc, bcc, files);
                }
                Transport.send(message);
                retryOnFailure = false; // 发送成功，停止重试
            } catch (MessagingException e) {
                retryCount++;
                try {
                    // 间隔秒数
                    TimeUnit.SECONDS.sleep(mailBuild.getRetryInterval());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (retryCount >= maxRetries && retryOnFailure) {
            try {
                Message message;
                if (html != null || parameter != null){
                    message = messageBuild(mailAddress, title, body, html, parameter, files, cc, bcc);
                } else {
                    message = messageBuild(mailAddress, title, body, cc, bcc, files);
                }
                Transport.send(message);
            } catch (MessagingException e) {
                throw new MailException(e);
            }
        }
    }

    public void  ReSendList(List<String> mailAddress,
                        String title,
                        String body,
                        List<String> cc,
                        List<String> bcc,
                        Map<String, String> files) {
        ReSendList(mailAddress, title, body, null, null, files, cc, bcc);
    }

    private void ReSend(String mailAddress,
                        String title,
                        String body,
                        String htmlName,
                        Map<String, String> parameter,
                        String zipName,
                        Map<String, String> files) {
        int maxRetries = mailBuild.getMaxRetries();
        int retryCount = 1; // 初始值为1；则while循环中少发送一次，最后一次发送在判断 retryCount >= maxRetries 这里。
        boolean retryOnFailure = true;

        while (retryOnFailure && retryCount < maxRetries) {
            try {
                logger.warning("邮件第 {" + retryCount + "} 次重新发送");
                if (htmlName != null || parameter != null){
                    Message    message = messageBuild(mailAddress, title, body, htmlName, parameter, zipName, files);
                    Transport.send(message);
                } else {
                    Message message = messageBuild(mailAddress, title, body, zipName, files);
                    Transport.send(message);
                }
                retryOnFailure = false; // 发送成功，停止重试
            } catch (MessagingException e) {
                retryCount++;
                try {
                    // 间隔秒数
                    TimeUnit.SECONDS.sleep(mailBuild.getRetryInterval());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (retryCount >= maxRetries && retryOnFailure) {
            try {
                if (htmlName != null || parameter != null){
                    Message   message = messageBuild(mailAddress, title, body, htmlName, parameter, zipName, files);
                    Transport.send(message);
                } else {
                    Message message = messageBuild(mailAddress, title, body, zipName, files);
                    Transport.send(message);
                }
            } catch (MessagingException e) {
                throw new MailException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void  ReSend(String mailAddress,
                         String title,
                         String body,
                         String zipName,
                         Map<String, String> files) {
        ReSend(mailAddress, title, body, null, null, zipName, files);
    }

    // messageBuild 方法重载
    private Message messageBuild(List<String> mailAddress,
                                 String title,
                                 String body,
                                 List<String> html,
                                 Map<String, String> parameter,
                                 Map<String, String> files,
                                 List<String> cc,
                                 List<String> bcc) throws MessagingException {
        Message message = mailBuild.getMessage();
        message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(mailAddress));
        message.setSubject(title);

        Multipart multipart = new MimeMultipart("alternative");
        // 读取模板并进行变量替换
        List<String> strings = HtmlUtil.replacePlaceholder(html, parameter);
        // 拼合HTML数据
        String htmlData = HtmlUtil.pieceHtml(strings);
        if (!body.isEmpty()) {
            // 创建文本正文部分
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);
        }
        // 添加附件
        if (files != null && files.size() != 0) {
            forFiles(multipart, files);
        }

        MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlData, "text/html;charset=UTF-8");
        addCC(cc, bcc, message);
        multipart.addBodyPart(htmlPart);
        message.setContent(multipart);
        return  message;
    }

    public Message messageBuild(List<String> mailAddress,
                                String title,
                                String body,
                                List<String> cc,
                                List<String> bcc,
                                Map<String, String> files) throws MessagingException {
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
        addCC(cc, bcc, message);
        return message;
    }

    public Message messageBuild(String mailAddress,
                                String title,
                                String body,
                                String zipName,
                                Map<String, String> files) throws MessagingException, IOException {
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
        return message;
    }

    public Message messageBuild(String mailAddress,
                                String title,
                                String body,
                                String htmlName,
                                Map<String, String> parameter,
                                String zipName,
                                Map<String, String> files) throws MessagingException, IOException {
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
        return message;
    }

    private void addCC(List<String> cc, List<String> bcc, Message message) throws MessagingException {
        if (cc != null && cc.size() > 0) {
            message.addRecipients(Message.RecipientType.CC, mailBuild.eliminate(cc));
        }
        if (bcc != null && bcc.size() > 0) {
            message.addRecipients(Message.RecipientType.BCC, mailBuild.eliminate(bcc));
        }
    }
}
