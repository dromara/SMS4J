package org.dromara.email.core.entity;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class MailMessage {

    /** 收件人地址*/
    private List<String> mailAddress;

    /** 邮件主题*/
    private String title;

    /** 文字正文*/
    private String body;

    /** 抄送人*/
    private List<String> cc;

    /** 密送人*/
    private List<String> bcc;

    /** 附件*/
    private Map<String,String> files;

    public static MailsBuilder Builder(){
        return new MailsBuilder();
    }

    static class MailsBuilder{
        private final MailMessage mailMessage = new MailMessage();
        public MailsBuilder() {
        }
        public MailMessage build(){
            return mailMessage;
        }

        public MailsBuilder setMailAddress(List<String> mailAddress) {
            mailMessage.mailAddress = mailAddress;
            return this;
        }

        public MailsBuilder setMailAddress(String mailAddress){
            if ( mailMessage.mailAddress == null){
                mailMessage.mailAddress = new ArrayList<>();
            }
            mailMessage.mailAddress.add(mailAddress);
            return this;
        }

        public MailsBuilder setTitle(String title){
            mailMessage.title = title;
            return this;
        }

        public MailsBuilder setBody(String body){
            mailMessage.body = body;
            return this;
        }

        public MailsBuilder setCc(List<String> cc){
            mailMessage.cc = cc;
            return this;
        }

        public MailsBuilder setCc(String cc){
            if (mailMessage.cc == null){
                mailMessage.cc = new ArrayList<>();
            }
            mailMessage.cc.add(cc);
            return this;
        }

        public MailsBuilder setBcc(List<String> bcc){
            mailMessage.bcc = bcc;
            return this;
        }

        public MailsBuilder setBcc(String bcc){
            if (mailMessage.bcc == null){
                mailMessage.bcc = new ArrayList<>();
            }
            mailMessage.bcc.add(bcc);
            return this;
        }

        public MailsBuilder setFiles(Map<String, String> files){
            mailMessage.files = files;
            return this;
        }

        public MailsBuilder setFiles(String fileName,String filePath){
            if (mailMessage.files == null){
                mailMessage.files = new HashMap<>();
            }
            mailMessage.files.put(fileName,filePath);
            return this;
        }
    }
}
