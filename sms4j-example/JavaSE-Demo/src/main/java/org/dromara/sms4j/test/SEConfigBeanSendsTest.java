package org.dromara.sms4j.test;

import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.javase.config.SEInitializer;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.test.send.SimpleSendMsgById;
import org.dromara.sms4j.test.send.SimpleSendMsgByLoadBalance;
import org.dromara.sms4j.test.send.SimpleSendMsgBySupplier;

import sms4j.dao.MySmsDao;
import sms4j.interceptor.MyIntercepterStrategy;
import sms4j.interceptor.MyInterceptor;
import sms4j.local.a.AConfig;
import sms4j.local.a.AFactory;

import java.util.ArrayList;
import java.util.List;

/*
* 测试情况
* */
public class SEConfigBeanSendsTest {

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
        SimpleSendMsgById simpleSendMsgById = new SimpleSendMsgById("a1", "11111111111");
        simpleSendMsgById.test();
        SimpleSendMsgByLoadBalance simpleSendMsgByLoadBalance = new SimpleSendMsgByLoadBalance("a","11111111111");
        simpleSendMsgByLoadBalance.test();
        SimpleSendMsgBySupplier simpleSendMsgBySupplier = new SimpleSendMsgBySupplier("a","11111111111");
        simpleSendMsgBySupplier.test();
    }
}
