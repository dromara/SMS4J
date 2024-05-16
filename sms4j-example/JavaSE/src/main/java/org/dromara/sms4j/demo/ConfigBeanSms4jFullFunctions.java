package org.dromara.sms4j.demo;

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

import java.util.ArrayList;
import java.util.List;

/*
* 文档地址 ： https://sms4j.com/doc3/javase.htmlhttps://sms4j.com/doc3/javase.html
* */
public class ConfigBeanSms4jFullFunctions {
    public static void main(String[] args) {


        // 全局基础配置
        SmsConfig smsConfig = new SmsConfig();
        // 账户级上限
        smsConfig.setAccountMax(4);
        smsConfig.setRestricted(true);

        // 服务商渠道配置
        List<SupplierConfig> blends = new ArrayList<SupplierConfig>();
        // 服务商
        AConfig aConfig = new AConfig();
        aConfig.setMaximum(5);
        aConfig.setConfigId("a1");
        blends.add(aConfig);

        //自定义渠道工厂
        AFactory myFactory = new AFactory();

        //自定义拦截器
        MyInterceptor myInterceptor = new MyInterceptor();

        //自定义拦截器策略
        MyIntercepterStrategy myIntercepterStrategy = new MyIntercepterStrategy();

        //自定义Dao
        MySmsDao mySmsDao = MySmsDao.getInstance();;

        //初始化SMS
        SEInitializer
            //这里之后的方法顺序可以任意排列，但是必须以initializer开始
            .initializer()
            //注册自定义拦截器策略
            .registerIInterceptorStrategy(myIntercepterStrategy)
            //注册自定义拦截器
            .registerSmsMethodInterceptor(myInterceptor)
            //注册自定义渠道工厂
            .registerFactory(myFactory)
            //注册自定义 smsDao
            .registerSmsDao(mySmsDao)
            //这里之前的方法顺序可以任意排列，但是必须以fromConfig结尾
            .fromConfig(smsConfig,blends);

        SmsFactory.getBySupplier("a").sendMessage("11111111111", "200001");

        //自定义拦截器执行策略
        MySecIntercepterStrategy mySecIntercepterStrategy = new MySecIntercepterStrategy();
        //热替换拦截器执行策略
        InterceptorStrategySmsManager.setInterceptorStrategyMapping(mySecIntercepterStrategy);

        SmsFactory.getBySupplier("a").sendMessage("11111111111", "200001");
    }
}
