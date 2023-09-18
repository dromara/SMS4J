package org.dromara.sms4j.starter.config;

import lombok.Data;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.provider.config.SmsBanner;
import org.dromara.sms4j.provider.factory.BeanFactory;
import org.dromara.sms4j.starter.utils.SmsSpringUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;


@Data
public class SmsMainConfig {

    @Bean
    public SmsSpringUtils smsSpringUtil(DefaultListableBeanFactory defaultListableBeanFactory){
        return new SmsSpringUtils(defaultListableBeanFactory);
    }

    /** 主要配置注入 确保springUtil注入后再注入*/
    @Bean
    public SmsAutowiredConfig smsAutowiredConfig(SmsSpringUtils smsSpringUtils){
        return new SmsAutowiredConfig(smsSpringUtils);
    }

    @EventListener
    void init(ContextRefreshedEvent event) {
        //打印banner
        if (BeanFactory.getSmsConfig().getIsPrint()) {
            SmsBanner.PrintBanner(Constant.VERSION);
        }
    }

}
