package org.dromara.sms4j.starter.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.provider.config.SmsBanner;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BeanFactory;
import org.dromara.sms4j.starter.utils.ConfigUtils;
import org.dromara.sms4j.starter.utils.SmsSpringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executor;


@Slf4j
public class SmsAutowiredConfig {

    private final SmsSpringUtils smsSpringUtils;

    public SmsAutowiredConfig(SmsSpringUtils smsSpringUtils) {
        this.smsSpringUtils = smsSpringUtils;
    }

    @Bean
    @ConfigurationProperties(prefix = "sms")     //指定配置文件注入属性前缀
    protected SmsConfig smsConfig(){
        return BeanFactory.getSmsConfig();
    }

    /** 注入一个定时器*/
    @Bean
    @Lazy
    protected DelayedTime delayedTime(){
        return BeanFactory.getDelayedTime();
    }

    /** 注入线程池*/
    @Bean("smsExecutor")
    @Lazy
    protected Executor taskExecutor(SmsConfig config){
        return BeanFactory.setExecutor(config);
    }

    /** 注入一个配置文件读取工具*/
    @Bean("smsConfigUtil")
    @Lazy
    protected ConfigUtils configUtil(Environment environment){
        return new ConfigUtils(environment);
    }

    /** smsConfig参数意义为确保注入时smsConfig已经存在*/
    @Bean
    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "yaml")
    protected SupplierConfig supplierConfig(SmsConfig smsConfig){
        return new SupplierConfig();
    }

    @PostConstruct
    void init() {
        //打印banner
        if (BeanFactory.getSmsConfig().getIsPrint()){
            SmsBanner.PrintBanner(Constant.VERSION);
        }
    }
}
