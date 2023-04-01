package kim.wind.sms.autoimmit.config;

import kim.wind.sms.autoimmit.aop.AopAdvice;
import kim.wind.sms.comm.config.SmsBanner;
import kim.wind.sms.comm.delayedTime.DelayedTime;
import kim.wind.sms.comm.utils.RedisUtils;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Data
public class SmsAutowiredConfig {
    public void init(){
        SmsBanner.PrintBanner("V1.0.3");
    }

    @Bean
    @ConfigurationProperties(prefix = "sms")     //指定配置文件注入属性前缀
    public SmsConfig smsConfig(){
        return new SmsConfig();
    }

    /** 注入一个定时器*/
    @Bean
    public DelayedTime delayedTime(){
        return new DelayedTime();
    }

    /** 如果启用了短信限制，则注入Aop组件*/
    @Bean
    @ConditionalOnProperty(prefix = "sms", name = "restricted", havingValue = "true")
    public AopAdvice aopAdvice(){
        return new AopAdvice();
    }

    /** 如果启用了redis作为缓存则注入redis工具类*/
    @Bean
    @ConditionalOnProperty(prefix = "sms", name = "redis-cache", havingValue = "true")
    public RedisUtils redisUtils(){
        return new RedisUtils();
    }

    /** 注入线程池*/
    @Bean("smsExecutor")
    protected Executor taskExecutor(SmsConfig config){
        // 创建一个线程池对象
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(config.getCorePoolSize());
        executor.setMaxPoolSize(config.getMaxPoolSize());
        executor.setQueueCapacity(config.getQueueCapacity());
        executor.setKeepAliveSeconds(config.getKeepAliveSeconds());
        executor.setThreadNamePrefix(config.getThreadNamePrefix());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        // 线程池对拒绝任务的处理策略,当线程池没有处理能力的时候，该策略会直接在 execute 方法的调用线程中运行被拒绝的任务；如果执行程序已关闭，则会丢弃该任务
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //初始化线程池
        executor.initialize();
        return executor;
    }



}
