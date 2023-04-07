package kim.wind.sms.autoimmit.config;

import kim.wind.sms.autoimmit.aop.AopAdvice;
import kim.wind.sms.comm.delayedTime.DelayedTime;
import kim.wind.sms.core.utils.RedisUtils;
import kim.wind.sms.core.config.SmsConfig;
import kim.wind.sms.core.factory.BeanFactory;
import kim.wind.sms.core.utils.SpringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import java.util.concurrent.Executor;


public class SmsAutowiredConfig {

    private final SpringUtil springUtil;

    public SmsAutowiredConfig(SpringUtil springUtil) {
        this.springUtil = springUtil;
    }

    @Bean
    @ConfigurationProperties(prefix = "sms")     //指定配置文件注入属性前缀
    public SmsConfig smsConfig(){
        return BeanFactory.getSmsConfig();
    }

    /** 注入一个定时器*/
    @Bean
    public DelayedTime delayedTime(){
      return BeanFactory.getDelayedTime();
    }

    /** 如果启用了短信限制，则注入Aop组件*/
    @Bean
    @ConditionalOnProperty(prefix = "sms", name = "restricted", havingValue = "true")
    public AopAdvice aopAdvice(){
        return new AopAdvice();
    }

    /** 如果启用了redis作为缓存则注入redis工具类*/
    @Bean
    public RedisUtils redisUtils(){
        return null;
    }

    /** 注入线程池*/
    @Bean("smsExecutor")
    protected Executor taskExecutor(SmsConfig config){
       return BeanFactory.setExecutor(config);
    }


    void init(){
        if (BeanFactory.getSmsConfig().getRedisCache()){
            springUtil.createBean(RedisUtils.class.getName(),new RedisUtils());
        }

    }
}
