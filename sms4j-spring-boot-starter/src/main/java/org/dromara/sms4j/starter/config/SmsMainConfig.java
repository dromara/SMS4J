package org.dromara.sms4j.starter.config;

import lombok.Data;
import org.dromara.sms4j.starter.utils.SmsSpringUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;


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

}
