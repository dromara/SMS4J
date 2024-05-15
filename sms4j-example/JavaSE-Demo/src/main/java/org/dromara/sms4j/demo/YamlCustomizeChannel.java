package org.dromara.sms4j.demo;

import java.util.ArrayList;
import java.util.List;

import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.javase.config.SEInitializer;
import org.dromara.sms4j.provider.config.SmsConfig;

import sms4j.local.a.AConfig;
import sms4j.local.a.AFactory;

public class YamlCustomizeChannel {
    public static void main(String[] args) {



        //自定义渠道工厂
        AFactory myFactory = new AFactory();

        //初始化SMS
        SEInitializer
            //这里之后的方法顺序可以任意排列，但是必须以initializer开始
            .initializer()
            //注册自定义渠道工厂
            .registerFactory(myFactory)
            //这里之前的方法顺序可以任意排列，但是必须以fromConfig结尾
            .fromYaml();

        SmsFactory.getBySupplier("a").sendMessage("11111111111", "200001");

    }
}
