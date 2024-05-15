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

public class ConfigBeanCustomizeChannel {
    public static void main(String[] args) {

        // 全局基础配置
        SmsConfig smsConfig = new SmsConfig();

        //关闭限制，关闭后只有参数检验拦截器生效
        smsConfig.setRestricted(false);

        // 服务商渠道配置
        List<SupplierConfig> blends = new ArrayList<SupplierConfig>();
        // 自定义服务商
        AConfig aConfig = new AConfig();
        aConfig.setMaximum(5);
        aConfig.setConfigId("a1");
        blends.add(aConfig);

        //自定义渠道工厂
        AFactory myFactory = new AFactory();

        //初始化SMS
        SEInitializer
            //这里之后的方法顺序可以任意排列，但是必须以initializer开始
            .initializer()
            //注册自定义渠道工厂
            .registerFactory(myFactory)
            //这里之前的方法顺序可以任意排列，但是必须以fromConfig结尾
            .fromConfig(smsConfig,blends);

        SmsFactory.getBySupplier("a").sendMessage("11111111111", "200001");

    }
}
