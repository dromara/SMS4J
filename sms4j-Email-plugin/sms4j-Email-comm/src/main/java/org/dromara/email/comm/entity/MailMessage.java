package org.dromara.email.comm.entity;

import lombok.Getter;

import java.io.InputStream;
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

    /** html模板文件路径（resources目录下的路径）*/
    private String htmlPath;

    /** html模板文件的输入流，可来自任意可读取位置*/
    private InputStream htmlInputStream;

    /** html 模板参数*/
    private Map<String,String> htmlValues;

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

        public MailsBuilder setHtmlPath(String htmlPath){
            mailMessage.htmlPath = htmlPath;
            return this;
        }

        public MailsBuilder setHtmlInputStream(InputStream htmlInputStream){
            mailMessage.htmlInputStream = htmlInputStream;
            return this;
        }

        public MailsBuilder setHtmlValues(String key, String value){
            if (mailMessage.files == null){
                mailMessage.files = new HashMap<>();
            }
            mailMessage.htmlValues.put(key,value);
            return this;
        }

        public MailsBuilder setHtmlValues(Map<String,String> htmlValues){
            if (mailMessage.files == null){
                mailMessage.files = new HashMap<>();
            }
            mailMessage.htmlValues.putAll(htmlValues);
            return this;
        }
    }
}
