package org.dromara.sms4j.source;

import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sms4j.dao.MySmsDao;
import sms4j.interceptor.MyIntercepterStrategy;
import sms4j.interceptor.MyInterceptor;
import sms4j.local.a.AFactory;
import sms4j.local.b.BFactory;
import sms4j.local.c.CFactory;

@Configuration
public class Load {

    @Bean
    public SmsDao smsDao(){
        return  MySmsDao.getInstance();
    }

    @Bean
    public AFactory aFactory() {
        return AFactory.instance();
    }

    @Bean
    public BFactory bFactory() {
        return BFactory.instance();
    }

    @Bean
    public CFactory cFactory() {
        return CFactory.instance();
    }

    @Bean
    public MyIntercepterStrategy myIntercepterStrategy() {
        return new MyIntercepterStrategy();
    }

    @Bean
    public MyInterceptor myInterceptor() {
        return new MyInterceptor();
    }
}
