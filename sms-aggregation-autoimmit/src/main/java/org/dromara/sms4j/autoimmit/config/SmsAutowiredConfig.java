package org.dromara.sms4j.autoimmit.config;

import org.dromara.sms4j.autoimmit.aop.AopAdvice;
import org.dromara.sms4j.autoimmit.utils.ConfigUtil;
import org.dromara.sms4j.autoimmit.utils.RedisUtils;
import org.dromara.sms4j.autoimmit.utils.SpringUtil;
import org.dromara.sms4j.comm.config.SmsBanner;
import org.dromara.sms4j.comm.config.SmsConfig;
import org.dromara.sms4j.comm.config.SmsSqlConfig;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.factory.BeanFactory;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.core.SupplierSqlConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.concurrent.Executor;


@Slf4j
public class SmsAutowiredConfig {

    private final SpringUtil springUtil;

    public SmsAutowiredConfig(SpringUtil springUtil) {
        this.springUtil = springUtil;
    }

    @Bean
    @ConfigurationProperties(prefix = "sms.sql")
    protected SmsSqlConfig smsSqlConfig(){return BeanFactory.getSmsSqlConfig();}

    @Bean
    @ConfigurationProperties(prefix = "sms")     //指定配置文件注入属性前缀
    protected SmsConfig smsConfig(){
        return BeanFactory.getSmsConfig();
    }

    /** 注入一个定时器*/
    @Bean
    protected DelayedTime delayedTime(){
      return BeanFactory.getDelayedTime();
    }

    /** 注入线程池*/
    @Bean("smsExecutor")
    protected Executor taskExecutor(SmsConfig config){
       return BeanFactory.setExecutor(config);
    }

    /** 注入一个配置文件读取工具*/
    @Bean
    protected ConfigUtil configUtil(Environment environment){
        return new ConfigUtil(environment);
    }

    @Bean
    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "config_file")
    protected SupplierConfig supplierConfig(SmsConfig smsConfig){
        return new SupplierConfig();
    }

    @Bean
    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "sql_config")
    protected void supplierSqlConfig(){
         new SupplierSqlConfig();
    }


    void init(){
        /* 如果配置中启用了redis，则注入redis工具*/
        if (BeanFactory.getSmsConfig().getRedisCache()){
            springUtil.createBean(RedisUtils.class);
            log.debug("The redis cache is enabled for sms-aggregation");
        }
        /* 如果启用了短信限制，则注入AOP组件*/
        if (BeanFactory.getSmsConfig().getRestricted()){
            springUtil.createBean(AopAdvice.class);
            log.debug("SMS restriction is enabled");
        }
        if (BeanFactory.getSmsConfig().getIsPrint()){
            SmsBanner.PrintBanner("V 1.0.5");
        }
    }
}
