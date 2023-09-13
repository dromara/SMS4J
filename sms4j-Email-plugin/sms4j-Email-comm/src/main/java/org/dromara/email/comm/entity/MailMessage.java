package org.dromara.email.comm.entity;

import lombok.Getter;
import org.dromara.email.comm.utils.ReflectUtil;

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

    /** 压缩文件名称*/
    private String zipName;

    public static MailsBuilder Builder(){
        return new MailsBuilder();
    }

    public static class MailsBuilder{
        private final MailMessage mailMessage = new MailMessage();
        public MailsBuilder() {
        }
        public MailMessage build(){
            return mailMessage;
        }

        /** 收件人地址*/
        public MailsBuilder mailAddress(List<String> mailAddress) {
            mailMessage.mailAddress = mailAddress;
            return this;
        }

        /** 收件人地址*/
        public MailsBuilder mailAddress(String mailAddress){
            if ( mailMessage.mailAddress == null){
                mailMessage.mailAddress = new ArrayList<>();
            }
            mailMessage.mailAddress.add(mailAddress);
            return this;
        }

        /** 邮件主题*/
        public MailsBuilder title(String title){
            mailMessage.title = title;
            return this;
        }

        /** 文字正文*/
        public MailsBuilder body(String body){
            mailMessage.body = body;
            return this;
        }

        /** 抄送人*/
        public MailsBuilder cc(List<String> cc){
            mailMessage.cc = cc;
            return this;
        }

        /** 抄送人*/
        public MailsBuilder cc(String cc){
            if (mailMessage.cc == null){
                mailMessage.cc = new ArrayList<>();
            }
            mailMessage.cc.add(cc);
            return this;
        }

        /** 密送人*/
        public MailsBuilder bcc(List<String> bcc){
            mailMessage.bcc = bcc;
            return this;
        }

        /** 密送人*/
        public MailsBuilder bcc(String bcc){
            if (mailMessage.bcc == null){
                mailMessage.bcc = new ArrayList<>();
            }
            mailMessage.bcc.add(bcc);
            return this;
        }

        /** 附件*/
        public MailsBuilder files(Map<String, String> files){
            if (mailMessage.files == null){
                mailMessage.files = new HashMap<>();
            }
            mailMessage.files.putAll(files);
            return this;
        }

        /** 附件*/
        public MailsBuilder files(String fileName,String filePath){
            if (mailMessage.files == null){
                mailMessage.files = new HashMap<>();
            }
            mailMessage.files.put(fileName,filePath);
            return this;
        }

        /** html模板文件路径（resources目录下的路径）*/
        public MailsBuilder html(String htmlPath){
            mailMessage.htmlPath = htmlPath;
            return this;
        }

        /** html模板文件的输入流，可来自任意可读取位置*/
        public MailsBuilder html(InputStream htmlInputStream){
            mailMessage.htmlInputStream = htmlInputStream;
            return this;
        }

        /** html 模板参数*/
        public MailsBuilder htmlValues(String key, String value){
            if (mailMessage.htmlValues == null){
                mailMessage.htmlValues = new HashMap<>();
            }
            mailMessage.htmlValues.put(key,value);
            return this;
        }

        /** html 模板参数*/
        public MailsBuilder htmlValues(Map<String,String> htmlValues){
            if (mailMessage.htmlValues == null){
                mailMessage.htmlValues = new HashMap<>();
            }
            mailMessage.htmlValues.putAll(htmlValues);
            return this;
        }

        /** html 模板参数*/
        public MailsBuilder htmlValues(Parameter parameter){
            Map<String, String> values = ReflectUtil.getValues(parameter);
            if (mailMessage.htmlValues == null){
                mailMessage.htmlValues = new HashMap<>();
            }
            mailMessage.htmlValues.putAll(values);
            return this;
        }

        /** 压缩文件名称*/
        public MailsBuilder zipName(String zipName){
            mailMessage.zipName = zipName;
            return this;
        }
    }
}
