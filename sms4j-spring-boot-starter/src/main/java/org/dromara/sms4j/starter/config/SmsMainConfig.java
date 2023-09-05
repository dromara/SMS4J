package org.dromara.sms4j.starter.config;

import lombok.Data;
import org.dromara.sms4j.starter.utils.SmsSpringUtil;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;


@Data
public class SmsMainConfig {

    @Bean
    public SmsSpringUtil smsSpringUtil(DefaultListableBeanFactory defaultListableBeanFactory){
        return new SmsSpringUtil(defaultListableBeanFactory);
    }

    /** 主要配置注入 确保springUtil注入后再注入*/
    @Bean
    public SmsAutowiredConfig smsAutowiredConfig(SmsSpringUtil smsSpringUtil){
        return new SmsAutowiredConfig(smsSpringUtil);
    }

}
