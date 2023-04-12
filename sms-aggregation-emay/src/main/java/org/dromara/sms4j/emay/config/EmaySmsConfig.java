package org.dromara.sms4j.emay.config;

import org.dromara.sms4j.emay.service.EmaySmsImpl;
import org.dromara.sms4j.comm.factory.BeanFactory;

/**
 * EmaySmsConfig
 * <p> Emay短信对象建造
 *
 * @author Richard
 * @date 2023/04/11 12:00
 * */
public class EmaySmsConfig {
    private static EmaySmsImpl emaySms;
    private static EmaySmsConfig emaySmsConfig;

    private EmaySmsConfig() {
    }

    /** 建造一个亿美软通短信实现*/
    public static EmaySmsImpl createEmaySms(EmayConfig emayConfig) {
        if (emaySmsConfig == null){
            emaySmsConfig = new EmaySmsConfig();
        }
        if (emaySms == null){
            emaySms = new EmaySmsImpl(emayConfig, BeanFactory.getExecutor(),BeanFactory.getDelayedTime());
        }
        return emaySms;
    }

    /** 刷新对象*/
    public static EmaySmsImpl refresh(EmayConfig emayConfig){
        if (emaySmsConfig == null){
            emaySmsConfig = new EmaySmsConfig();
        }
        emaySms = new EmaySmsImpl(emayConfig, BeanFactory.getExecutor(),BeanFactory.getDelayedTime());
        return emaySms;
    }

}
