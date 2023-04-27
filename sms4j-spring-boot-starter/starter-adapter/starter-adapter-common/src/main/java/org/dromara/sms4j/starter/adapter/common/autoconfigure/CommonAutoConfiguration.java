package org.dromara.sms4j.starter.adapter.common.autoconfigure;

import lombok.Data;
import org.dromara.sms4j.autoimmit.config.SmsAutowiredConfig;
import org.dromara.sms4j.autoimmit.utils.SpringUtil;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Data
@EnableAsync
@Configuration
public class CommonAutoConfiguration {

    @Bean
    public SpringUtil springUtil(DefaultListableBeanFactory defaultListableBeanFactory){
        return new SpringUtil(defaultListableBeanFactory);
    }

    /** 主要配置注入 确保springUtil注入后再注入*/
    @Bean(initMethod = "init")
    public SmsAutowiredConfig smsAutowiredConfig(SpringUtil springUtil){
        return new SmsAutowiredConfig(springUtil);
    }

}
