package org.dromara.email.jakarta.core.factory;

import jakarta.mail.MessagingException;
import org.dromara.email.jakarta.api.Blacklist;
import org.dromara.email.jakarta.api.MailClient;
import org.dromara.email.jakarta.comm.config.MailSmtpConfig;
import org.dromara.email.jakarta.comm.errors.MailException;
import org.dromara.email.jakarta.core.service.MailBuild;

import java.util.HashMap;
import java.util.Map;

/**
 * MailFactory
 * <p> 配置工厂
 * @author :Wind
 * 2023/6/8  22:35
 **/
public class MailFactory{
    private static final Map<Object,MailSmtpConfig> CONFIGS = new HashMap<>();

    /**
     *  createMailClient
     * <p>从工厂获取一个邮件发送实例
     * @param key 配置的标识key
     * @author :Wind
    */
    public static MailClient createMailClient(Object key){
        try {
            return MailBuild.build(CONFIGS.get(key));
        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }


    /**
     *  createMailClient
     * <p>从工厂获取一个邮件发送实例,该实例发送短信将依照黑名单中的数据进行过滤
     * @param key 配置的标识key
     * @param blacklist 黑名单接口，实例将从这里获取黑名单数据
     * @author :Wind
    */
    public static MailClient createMailClient(Object key, Blacklist blacklist){
        try {
            return MailBuild.build(CONFIGS.get(key),blacklist);
        } catch (MessagingException e) {
            throw new MailException(e);
        }
    }

    /**
     *  set
     * <p>将一个配置对象交给工厂
     * @param key 标识
     * @param config 配置对象
     * @author :Wind
    */
    public static void put(Object key, MailSmtpConfig config){
        CONFIGS.put(key,config);
    }

}
