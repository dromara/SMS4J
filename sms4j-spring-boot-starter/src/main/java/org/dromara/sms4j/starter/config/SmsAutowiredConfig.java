package org.dromara.sms4j.starter.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.smsProxy.SmsInvocationHandler;
import org.dromara.sms4j.api.universal.SmsRedisUtil;
import org.dromara.sms4j.comm.config.SmsBanner;
import org.dromara.sms4j.comm.config.SmsConfig;
import org.dromara.sms4j.comm.config.SmsSqlConfig;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.core.SupplierSqlConfig;
import org.dromara.sms4j.starter.aop.RestrictedProcessImpl;
import org.dromara.sms4j.starter.utils.ConfigUtil;
import org.dromara.sms4j.starter.utils.SmsRedisUtils;
import org.dromara.sms4j.starter.utils.SmsSpringUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.Executor;


@Slf4j
public class SmsAutowiredConfig {

    private final SmsSpringUtil smsSpringUtil;

    public SmsAutowiredConfig(SmsSpringUtil smsSpringUtil) {
        this.smsSpringUtil = smsSpringUtil;
    }

    @Bean
    @ConfigurationProperties(prefix = "sms.sql")
    protected SmsSqlConfig smsSqlConfig(){return BeanFactory.getSmsSqlConfig();}

    @Bean
    @Primary
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

    /** smsConfig参数意义为确保注入时smsConfig已经存在*/
    @Bean
    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "config_file")
    protected SupplierConfig supplierConfig(SmsConfig smsConfig){
        return new SupplierConfig();
    }

    @Bean
    @ConditionalOnProperty(prefix = "sms", name = "config-type", havingValue = "sql_config")
    protected SupplierSqlConfig supplierSqlConfig(SmsSqlConfig smsSqlConfig) throws SQLException {
        DataSource bean = SmsSpringUtil.getBean(DataSource.class);
        if (!Objects.isNull(bean)){
            BeanFactory.getJDBCTool().setConnection(bean.getConnection());
        }
        return new SupplierSqlConfig();
    }

    void init(){
        /* 如果配置中启用了redis，则注入redis工具*/
        if (BeanFactory.getSmsConfig().getRedisCache()){
            //如果用户没有实现RedisUtil接口则注入默认的实现
            if (!SmsSpringUtil.interfaceExist(SmsRedisUtil.class)){
                smsSpringUtil.createBean(SmsRedisUtils.class);
            }
            SmsInvocationHandler.setRestrictedProcess(new RestrictedProcessImpl());
            log.debug("The redis cache is enabled for sms4j");
        }
        //打印banner
        if (BeanFactory.getSmsConfig().getIsPrint()){
            SmsBanner.PrintBanner(Constant.VERSION);
        }
    }
}
