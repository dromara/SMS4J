package org.dromara.sms4j.source;

import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.EventListener;
import sms4j.dao.MySmsDao;
import sms4j.interceptor.MyIntercepterStrategy;
import sms4j.interceptor.MyInterceptor;

@Configuration
public class Load {


    @Bean
    public MyIntercepterStrategy myIntercepterStrategy(){
        return  new MyIntercepterStrategy();
    }

    @Bean
    public MyInterceptor myInterceptor(){
        return  new MyInterceptor();
    }

    @Bean
    public SmsDao smsDao(){
        return new MySmsDao();
    }

}
