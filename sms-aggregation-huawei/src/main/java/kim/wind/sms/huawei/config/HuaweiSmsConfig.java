package kim.wind.sms.huawei.config;

import kim.wind.sms.comm.factory.BeanFactory;
import kim.wind.sms.huawei.service.HuaweiSmsImpl;

/**
 * HuaweiSmsConfig
 * <p> 华为短信对象建造
 *
 * @author :Wind
 * 2023/4/8  15:27
 **/
public class HuaweiSmsConfig {
    private static HuaweiSmsImpl huaweiSms;
    private static HuaweiSmsConfig huaweiSmsConfig;

    private HuaweiSmsConfig() {
    }

    /** 建造一个华为短信实现*/
    public static HuaweiSmsImpl createHuaweiSms(HuaweiConfig huaweiConfig) {
        if (huaweiSmsConfig == null){
            huaweiSmsConfig = new HuaweiSmsConfig();
        }
        if (huaweiSms == null){
            huaweiSms = new HuaweiSmsImpl(huaweiConfig, BeanFactory.getExecutor(),BeanFactory.getDelayedTime());
        }
        return huaweiSms;
    }

    /** 刷新对象*/
    public static HuaweiSmsImpl refresh(HuaweiConfig huaweiConfig){
        if (huaweiSmsConfig == null){
            huaweiSmsConfig = new HuaweiSmsConfig();
        }
        huaweiSms = new HuaweiSmsImpl(huaweiConfig, BeanFactory.getExecutor(),BeanFactory.getDelayedTime());
        return huaweiSms;
    }

}
