package org.dromara.sms4j.cloopen.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.cloopen.service.CloopenSmsImpl;
import org.dromara.sms4j.comm.factory.BeanFactory;

/**
 * 容联云短信配置
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CloopenSmsConfig {

    private static CloopenSmsImpl cloopenSms;

    private static CloopenSmsConfig cloopenSmsConfig;

    /**
     * 创建容联云短信实现
     */
    public static CloopenSmsImpl createCloopenSms(CloopenConfig cloopenConfig) {
        if (cloopenSmsConfig == null) {
            cloopenSmsConfig = new CloopenSmsConfig();
        }
        if (cloopenSms == null) {
            cloopenSms = new CloopenSmsImpl(cloopenConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
        }
        return cloopenSms;
    }

    /**
     * 刷新对象
     */
    public static CloopenSmsImpl refresh(CloopenConfig cloopenConfig) {
        if (cloopenSmsConfig == null) {
            cloopenSmsConfig = new CloopenSmsConfig();
        }
        cloopenSms = new CloopenSmsImpl(cloopenConfig, BeanFactory.getExecutor(), BeanFactory.getDelayedTime());
        return cloopenSms;
    }
}
