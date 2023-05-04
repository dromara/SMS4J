package org.dromara.sms4j.autoimmit.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.autoimmit.aop.AopAdvice;
import org.dromara.sms4j.autoimmit.utils.ConfigUtil;
import org.dromara.sms4j.autoimmit.utils.RedisUtils;
import org.dromara.sms4j.autoimmit.utils.SpringUtil;
import org.dromara.sms4j.comm.config.SmsBanner;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Slf4j
public class SmsAutowiredConfig {

    private final SpringUtil springUtil;

    public SmsAutowiredConfig(SpringUtil springUtil) {
        this.springUtil = springUtil;
    }

//    @Bean
//    @ConfigurationProperties(prefix = "sms.sql")
//    protected SmsSqlConfig smsSqlConfig(){return BeanFactory.getSmsSqlConfig();}

//    @Bean
//    @ConfigurationProperties(prefix = "sms")     //指定配置文件注入属性前缀
//    protected SmsConfig smsConfig(){
//        return BeanFactory.getSmsConfig();
//    }

    /** 注入一个定时器*/
    @Bean
    protected DelayedTime delayedTime(){
      return BeanFactory.getDelayedTime();
    }

    /** 注入线程池*/
    @Bean("smsExecutor")
    protected Executor taskExecutor(SmsProperties smsProperties){
        // TODO
        // 创建一个线程池对象
        ThreadPoolExecutor ex = new ThreadPoolExecutor(
                smsProperties.getCorePoolSize(),
                smsProperties.getMaxPoolSize(),
                smsProperties.getQueueCapacity(),
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(smsProperties.getMaxPoolSize())
        );
        // 线程池对拒绝任务的处理策略,当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        ex.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
       return ex;
    }

    /** 注入一个配置文件读取工具*/
    @Bean
    protected ConfigUtil configUtil(Environment environment){
        return new ConfigUtil(environment);
    }

    /** smsConfig参数意义为确保注入时smsConfig已经存在*/
//    @Bean
//    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "config_file")
//    protected SupplierConfig supplierConfig(SmsConfig smsConfig){
//        return new SupplierConfig();
//    }

//    @Bean
//    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "sql_config")
//    protected void supplierSqlConfig(SmsSqlConfig smsSqlConfig){
//        SupplierSqlConfig.refreshSqlConfig();
//    }


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
        //打印banner
        if (BeanFactory.getSmsConfig().getIsPrint()){
            SmsBanner.PrintBanner("V 2.0.1");
        }
    }
}
