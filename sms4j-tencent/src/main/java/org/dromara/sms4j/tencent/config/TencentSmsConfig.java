package org.dromara.sms4j.tencent.config;

import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.tencent.service.TencentSmsImpl;

/**
 * TencentSmsConfig
 * <p> 建造腾讯云短信
 *
 * @author :Wind
 * 2023/4/8  16:05
 **/
public class TencentSmsConfig {
    private TencentSmsConfig() {
    }

    private static TencentSmsImpl tencentSms;

    private static TencentSmsConfig tencentSmsConfig;

    /**
     * 建造一个腾讯云的短信实现
     */
    public static TencentSmsImpl createTencentSms(TencentConfig tencentConfig) {
        if (tencentSmsConfig == null) {
            tencentSmsConfig = new TencentSmsConfig();
        }
        if (tencentSms == null) {
            tencentSms = new TencentSmsImpl(
                    tencentConfig,
                    BeanFactory.getExecutor(),
                    BeanFactory.getDelayedTime()
            );
        }
        return tencentSms;
    }

    /**
     * 刷新对象
     */
    public static TencentSmsImpl refresh(TencentConfig tencentConfig) {
        if (tencentSmsConfig == null) {
            tencentSmsConfig = new TencentSmsConfig();
        }
        tencentSms = new TencentSmsImpl(
                tencentConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime()
        );
        return tencentSms;
    }
}
