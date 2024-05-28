package org.dromara.sms4j.test.paramvalidate;

import org.dromara.sms4j.core.datainterface.SmsBlendsSelectedConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.provider.config.BaseConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import sms4j.dao.MySmsDao;
import sms4j.interceptor.MyIntercepterStrategy;
import sms4j.interceptor.MyInterceptor;
import sms4j.local.a.AConfig;
import sms4j.local.a.AFactory;

// SmsReadConfig 是一个存在但是未来可能被删除的一个接口，理想状态是使用  【SmsBlendsBeanConfig、SmsBlendsSelectedConfig】 SmsReadConfig正是继承了前面提到的两个接口
// 这里为了只用一个类配置以此测试演示，使用中可以分开配置不用都写在一起
@Configuration
public class BootConfigParamValidateConfig implements SmsBlendsSelectedConfig {

    //自定义渠道工厂
    @Bean
    public AFactory myFactory() {
        return new AFactory();
    }

    //自定义拦截器
    @Bean
    public MyInterceptor myInterceptor() {
        return new MyInterceptor();
    }

    //自定义拦截器策略
    @Bean
    public MyIntercepterStrategy myIntercepterStrategy() {
        return new MyIntercepterStrategy();
    }

    //自定义Dao
    @Bean
    public MySmsDao mySmsDao() {
        return MySmsDao.getInstance();
    }

    @Override
    public BaseConfig getSupplierConfig(String configId) {
        // 服务商渠道配置
        // 服务商
        AConfig aConfig = new AConfig();
        if ("a1".equals(configId)){
            aConfig.setMaximum(5);
            aConfig.setConfigId("a1");
        }
        if ("a2".equals(configId)){
            aConfig.setMaximum(5);
            aConfig.setConfigId("a2");
        }
        return aConfig;
    }

    @EventListener
    //不是非要用监听器，有无数种方法，这里就是介绍这个SmsBlendsSelectedConfig  需要你在本类加载之后手动createSmsBlend选择使用那一个配置
    public void whenYouAppStarted(ContextRefreshedEvent event){
        SmsFactory.createSmsBlend(this,"a1");
    }
}
