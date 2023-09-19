package org.dromara.email.core.service;

import org.dromara.email.api.Monitor;
import org.dromara.email.comm.config.MailImapConfig;
import org.dromara.email.comm.entity.MonitorMessage;
import org.dromara.email.comm.errors.MailException;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;

/**
 * MonitorService
 * <p> 监听器服务
 *
 * @author :Wind
 * 2023/7/18  16:10
 **/
public class MonitorService{
    private final Store store;
    private Monitor monitor;
    private MailImapConfig mailImapConfig;
    private Timer timer;

    public MonitorService(MailImapConfig config, Monitor monitor) {
        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        try {
            Session session = Session.getDefaultInstance(props, null);
            Store store = session.getStore("imaps");
            store.connect(config.getImapServer(), config.getUsername(), config.getAccessToken());
            this.store = store;
            this.monitor = monitor;
            this.mailImapConfig = config;
        } catch (Exception e) {
            throw new MailException(e);
        }
    }

    private void startListening() {
        try {
            Folder inbox = store.getFolder("Inbox");
            inbox.open(Folder.READ_WRITE);
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (Message message : messages) {
                MonitorMessage monitorMessage = new MonitorMessage();
                // 获取邮件的发送者
                monitorMessage.setFromAddress(message.getFrom()[0].toString());
                // 获取邮件主题
                monitorMessage.setTitle(message.getSubject());
                // 获取邮件的内容
                if (message.isMimeType("text/plain")) {
                    Object content = message.getContent();
                    if (content == null){
                        StringBuilder stringBuilder = getStringBuilder(message);
                        content = stringBuilder.toString();
                    }
                    monitorMessage.setText(content.toString());
                } else if (message.isMimeType("multipart/*")) {
                    Multipart mp = (Multipart) message.getContent();
                    for (int i = 0; i < mp.getCount();i++){
                        BodyPart bodyPart = mp.getBodyPart(i);
                        String contentType = bodyPart.getContentType().toLowerCase();
                        if (contentType.startsWith("text/plain")) {
                            // 纯文本内容
                            monitorMessage.setText(bodyPart.getContent().toString());
                        } else if (contentType.startsWith("text/html")) {
                            // HTML内容
                            monitorMessage.setText(bodyPart.getContent().toString());
                        }
                    }
                    monitorMessage.setBody(mp);
                }
                monitorMessage.setMessageIndex(message.getMessageNumber());
                monitorMessage.setSendDate(message.getSentDate());
                monitorMessage.setAcceptTime(System.currentTimeMillis());
                if (this.monitor.monitor(monitorMessage)) {
                    message.setFlag(Flags.Flag.SEEN, true);
                }
            }
            inbox.close();
        } catch (Exception e) {
            throw new MailException(e);
        }
    }

    private static StringBuilder getStringBuilder(Message message) throws IOException, MessagingException {
        InputStream inputStream = message.getInputStream();

        // 解析输入流以获取内容
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        return stringBuilder;
    }

    public MonitorService start(){
        Timer timer = new Timer();
        this.timer = timer;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                startListening();
            }
        },0,mailImapConfig.getCycle()*1000);
        return this;
    }

    public void stop(){
        timer.cancel();
    }

    public MailImapConfig getMailImapConfig() {
        return mailImapConfig;
    }
}
