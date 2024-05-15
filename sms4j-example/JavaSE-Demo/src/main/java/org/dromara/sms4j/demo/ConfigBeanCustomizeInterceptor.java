package org.dromara.sms4j.demo;

import java.util.ArrayList;
import java.util.List;

import org.dromara.sms4j.api.manage.InterceptorStrategySmsManager;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.javase.config.SEInitializer;
import org.dromara.sms4j.provider.config.SmsConfig;

import sms4j.dao.MySmsDao;
import sms4j.interceptor.MyIntercepterStrategy;
import sms4j.interceptor.MyInterceptor;
import sms4j.interceptor.MySecIntercepterStrategy;
import sms4j.local.a.AConfig;
import sms4j.local.a.AFactory;

public class ConfigBeanCustomizeInterceptor {
    public static void main(String[] args) {

        // 全局基础配置
        SmsConfig smsConfig = new SmsConfig();

        //关闭限制，关闭后只有参数检验拦截器生效
        smsConfig.setRestricted(false);

        // 服务商渠道配置
        List<SupplierConfig> blends = new ArrayList<SupplierConfig>();
        // 服务商
        AConfig aConfig = new AConfig();
        aConfig.setConfigId("a1");
        blends.add(aConfig);

        //为了方便成功通过的演示，这里是自定义的服务商渠道，如果使用sms4j预置的渠道无需此配置
        AFactory myFactory = new AFactory();

        //自定义的拦截器，自定义拦截器与拦截器策略绑定，所以必须要有对应的拦截器策略，具体关系类似切点和增强
        MyInterceptor myInterceptor = new MyInterceptor();

        //自定义拦截器策略
        MyIntercepterStrategy myIntercepterStrategy = new MyIntercepterStrategy();

        //初始化SMS
        SEInitializer
            //这里之后的方法顺序可以任意排列，但是必须以initializer开始
            .initializer()
            //为了方便成功通过的演示，这里是自定义的服务商渠道，如果使用sms4j预置的渠道无需此配置
            .registerFactory(myFactory)
            //注册自定义拦截器
            .registerSmsMethodInterceptor(myInterceptor)
            //注册自定义拦截器策略
            .registerIInterceptorStrategy(myIntercepterStrategy)
            //这里之前的方法顺序可以任意排列，但是必须以fromConfig结尾
            .fromConfig(smsConfig,blends);

        SmsFactory.getBySupplier("a").sendMessage("11111111111", "200001");

        //热修改拦截器策略
        MySecIntercepterStrategy mySecIntercepterStrategy = new MySecIntercepterStrategy();
        InterceptorStrategySmsManager.setInterceptorStrategyMapping(mySecIntercepterStrategy);

        SmsFactory.getBySupplier("a").sendMessage("11111111111", "200001");
    }
}
