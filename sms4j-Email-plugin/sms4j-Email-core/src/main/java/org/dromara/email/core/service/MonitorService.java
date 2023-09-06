package org.dromara.email.core.service;

import org.dromara.email.api.Monitor;
import org.dromara.email.comm.config.MailImapConfig;
import org.dromara.email.comm.entity.MonitorMessage;
import org.dromara.email.comm.errors.MailException;

import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
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
            inbox.open(Folder.READ_ONLY);
            Message[] messages = inbox.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            for (Message message : messages) {
                MonitorMessage monitorMessage = new MonitorMessage();
                // 获取邮件的发送者
                monitorMessage.setFromAddress(message.getFrom()[0].toString());
                // 获取邮件主题
                monitorMessage.setTitle(message.getSubject());
                // 获取邮件的内容
                if (message.isMimeType("text/plain")) {
                    monitorMessage.setText(message.getContent().toString());
                } else if (message.isMimeType("multipart/*")) {
                    Multipart mp = (Multipart) message.getContent();
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
