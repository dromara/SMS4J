package org.dromara.sms4j.starter.configration;

import lombok.Data;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.provider.config.SmsBanner;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BeanFactory;
import org.dromara.sms4j.starter.utils.ConfigUtils;
import org.dromara.sms4j.starter.utils.SmsSpringUtils;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;


@Data
public class SmsMainConfiguration {

    @Bean
    public SmsSpringUtils smsSpringUtil(DefaultListableBeanFactory defaultListableBeanFactory){
        return new SmsSpringUtils(defaultListableBeanFactory);
    }

    @Bean
    @ConfigurationProperties(prefix = "sms")     //指定配置文件注入属性前缀
    protected SmsConfig smsConfig() {
        return BeanFactory.getSmsConfig();
    }

    /**
     * 注入一个配置文件读取工具
     */
    @Bean("smsConfigUtil")
    @Lazy
    protected ConfigUtils configUtil(Environment environment) {
        return new ConfigUtils(environment);
    }

    @EventListener
    void init(ContextRefreshedEvent event) {
        //打印banner
        if (BeanFactory.getSmsConfig().getIsPrint()) {
            SmsBanner.PrintBanner(Constant.VERSION);
        }
    }

}
