package org.dromara.email.core.service;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;
import org.dromara.email.api.Blacklist;
import org.dromara.email.api.MailClient;
import org.dromara.email.comm.config.MailSmtpConfig;
import org.dromara.email.comm.errors.MailException;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Data
public class MailBuild {

    private Message message;

    private Session session;

    private MailSmtpConfig config;

    private Blacklist blacklist;

    private MailBuild(MailSmtpConfig config) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getSmtpServer());
        props.put("mail.smtp.auth", config.getIsAuth());
        props.put("mail.smtp.port", config.getPort());
        props.put("mail.smtp.ssl.enable", config.getIsSSL());
//        props.put("mail.smtp.ssl.socketFactory", new MailSSLSocketFactory());
        this.session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getUsername(), config.getPassword());
            }
        });
        this.message = new MimeMessage(session);
        this.message.setFrom(new InternetAddress(config.getFromAddress()));
        this.config = config;
    }

    private MailBuild(MailSmtpConfig config,Blacklist blacklist)throws MessagingException{
        Properties props = new Properties();
        props.put("mail.smtp.host", config.getSmtpServer());
        props.put("mail.smtp.auth", config.getIsAuth());
        props.put("mail.smtp.port", config.getPort());
        props.put("mail.smtp.ssl.enable", config.getIsSSL());
//        props.put("mail.smtp.ssl.socketFactory", new MailSSLSocketFactory());
        this.session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(config.getUsername(), config.getPassword());
                    }
                });
        this.message = new MimeMessage(session);
        this.message.setFrom(new InternetAddress(config.getFromAddress()));
        this.config = config;
        this.blacklist = blacklist;
    }

    public static MailClient build(MailSmtpConfig config) throws MessagingException {
        return MailService.NewMailService(new MailBuild(config));
    }
    public static MailClient build(MailSmtpConfig config,Blacklist blacklist)throws MessagingException {
       return MailService.NewMailService(new MailBuild(config,blacklist));
    }

    /**
     * eliminate
     * <p>过滤黑名单内容
     * @param source 需要过滤的源数据
     * @author :Wind
     */
    public InternetAddress[] eliminate(List<String> source) {
        List<String> list = new ArrayList<>();
        try {
            if (Objects.isNull(blacklist)) {
                return InternetAddress.parse(Objects.requireNonNull(CollUtil.join(source, ",")));
            }
            for (String s : blacklist.getBlacklist()) {
                if (!source.contains(s)) {
                    list.add(s);
                }
            }
           return InternetAddress.parse(CollUtil.join(list, ","));
        } catch (AddressException e) {
            throw new MailException(e);
        }
    }
}
