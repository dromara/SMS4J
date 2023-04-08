package kim.wind.sms.autoimmit.config;

import kim.wind.sms.autoimmit.aop.AopAdvice;
import kim.wind.sms.autoimmit.utils.ConfigUtil;
import kim.wind.sms.autoimmit.utils.RedisUtils;
import kim.wind.sms.autoimmit.utils.SpringUtil;
import kim.wind.sms.comm.config.SmsBanner;
import kim.wind.sms.comm.config.SmsConfig;
import kim.wind.sms.comm.delayedTime.DelayedTime;
import kim.wind.sms.comm.factory.BeanFactory;
import lombok.extern.slf4j.Slf4j;
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
    protected SupplierConfig supplierConfig(){
        return new SupplierConfig();
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
