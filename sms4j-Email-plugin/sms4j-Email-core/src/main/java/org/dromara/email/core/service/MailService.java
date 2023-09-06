package org.dromara.email.core.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import org.dromara.email.api.MailClient;
import org.dromara.email.comm.constants.FileConstants;
import org.dromara.email.comm.entity.MailMessage;
import org.dromara.email.comm.errors.MailException;
import org.dromara.email.comm.utils.HtmlUtil;
import org.dromara.email.comm.utils.ZipUtils;

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

    public static MailClient instance(MailBuild mailBuild) {
        return new MailService(mailBuild);
    }

    @Override
    public void send(MailMessage mailMessage) {
        List<String> html;
        if (mailMessage.getHtmlInputStream() == null) {
            html = HtmlUtil.readHtml(mailMessage.getHtmlPath());
        } else {
            html = HtmlUtil.readHtml(mailMessage.getHtmlInputStream());
        }
        List<String> address;
        if (CollUtil.isEmpty(mailMessage.getMailAddressList())) {
            if (StrUtil.isBlank(mailMessage.getMailAddress())) {
                throw new MailException("收件人地址不能为空!");
            }
            address = CollUtil.newArrayList(mailMessage.getMailAddress());
        } else {
            address = mailMessage.getMailAddressList();
        }
        send(address,
                mailMessage.getTitle(),
                mailMessage.getBody(),
                html,
                mailMessage.getHtmlValues(),
                mailMessage.getZipName(),
                mailMessage.getFiles(),
                mailMessage.getCc(),
                mailMessage.getBcc()
        );
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
            } else {
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
                      String zipName,
                      Map<String, String> files,
                      List<String> cc,
                      List<String> bcc) {
        try {
            Message message = messageBuild(mailAddress, title, body, html, parameter, zipName, cc, bcc, files);
            Transport.send(message);
            logger.info("邮件发送成功！^_^");
        } catch (MessagingException | IOException e) {
            // 防止 maxRetries 数值小于0带来的其他问题
            if (mailBuild.getMaxRetries() > 0) {
                ReSendList(mailAddress, title, body, html, parameter, zipName, files, cc, bcc);
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
                            String zipName,
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
                if (html != null || parameter != null) {
                    message = messageBuild(mailAddress, title, body, html, parameter, zipName, cc, bcc, files);
                } else {
                    message = messageBuild(mailAddress, title, body, null, null, zipName, cc, bcc, files);
                }
                Transport.send(message);
                retryOnFailure = false; // 发送成功，停止重试
            } catch (MessagingException | IOException e) {
                retryCount++;
                try {
                    // 间隔秒数
                    TimeUnit.SECONDS.sleep(mailBuild.getRetryInterval());
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if (retryCount >= maxRetries) {
            try {
                Message message;
                if (html != null || parameter != null) {
                    message = messageBuild(mailAddress, title, body, html, parameter, null, cc, bcc, files);
                } else {
                    message = messageBuild(mailAddress, title, body, null, null, null, cc, bcc, files);
                }
                Transport.send(message);
            } catch (MessagingException | IOException e) {
                throw new MailException(e);
            }
        }
    }

    public Message messageBuild(List<String> mailAddress,
                                String title,
                                String body,
                                List<String> html,
                                Map<String, String> parameter,
                                String zipName,
                                List<String> cc,
                                List<String> bcc,
                                Map<String, String> files) throws MessagingException, IOException {
        Message message = mailBuild.getMessage();
        message.setRecipients(Message.RecipientType.TO, mailBuild.eliminate(mailAddress));
        message.setSubject(title);

        Multipart multipart = new MimeMultipart("alternative");
        if (CollUtil.isNotEmpty(html) && MapUtil.isNotEmpty(parameter)) {
            //读取模板并进行变量替换
            List<String> strings = HtmlUtil.replacePlaceholder(html, parameter);
            //拼合HTML数据
            String htmlData = HtmlUtil.pieceHtml(strings);
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlData, "text/html;charset=UTF-8");
            multipart.addBodyPart(htmlPart);
        }

        if (StrUtil.isNotBlank(body)) {
            // 创建文本正文部分
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText(body);
            multipart.addBodyPart(textPart);
        }
        //添加附件
        if (MapUtil.isNotEmpty(files) && StrUtil.isNotBlank(zipName)) {
            zipFiles(multipart, zipName, files);
        } else {
            if (MapUtil.isNotEmpty(files)) {
                forFiles(multipart, files);
                message.setContent(multipart);
            }
        }
        if (CollUtil.isNotEmpty(cc) || CollUtil.isNotEmpty(bcc)) {
            addCC(cc, bcc, message);
        }
        message.setContent(multipart);
        return message;
    }

    private void addCC(List<String> cc, List<String> bcc, Message message) throws MessagingException {
        if (CollUtil.isNotEmpty(cc)) {
            message.addRecipients(Message.RecipientType.CC, mailBuild.eliminate(cc));
        }
        if (CollUtil.isNotEmpty(bcc)) {
            message.addRecipients(Message.RecipientType.BCC, mailBuild.eliminate(bcc));
        }
    }
}
