package org.dromara.sms4j.demo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.javase.config.SEInitializer;
import org.dromara.sms4j.provider.config.SmsConfig;

import sms4j.local.a.AConfig;
import sms4j.local.a.AFactory;

/*
* 文档地址 ： https://sms4j.com/doc3/javase.htmlhttps://sms4j.com/doc3/javase.html
* */
public class YamlBasicSend {
    public static void main(String[] args) {

        //为了方便成功通过的演示，这里是自定义的服务商渠道，如果使用sms4j预置的渠道无需此配置
        AFactory myFactory = new AFactory();

        //初始化SMS
        SEInitializer
            //这里之后的方法顺序可以任意排列，但是必须以initializer开始
            .initializer()
            //为了方便成功通过的演示，这里是自定义的服务商渠道注册，如果使用sms4j预置的渠道无需此配置
            .registerFactory(myFactory)
            //这里之前的方法顺序可以任意排列，但是必须以fromConfig结尾
            .fromYaml();
        //发送固定消息模板短信
        //这里的Supplier 是 aConfig.getSupplier(),即从a渠道中若有多个配置则获取任一配置
        SmsFactory.getBySupplier("a").sendMessage("11111111111", "200001");

        //发送固定模板多模板参数短信
        //这个map存放需要用到的模板参数
        LinkedHashMap<String,String> map4Normal = new LinkedHashMap<>();
        map4Normal.put("param1","value1");
        map4Normal.put("param2","value2");
        //这里的Supplier 是 aConfig.getSupplier(),即从a渠道中若有多个配置则获取任一配置
        SmsFactory.getBySupplier("a").sendMessage("22222222222", map4Normal);

        //使用指定模板发送短信
        LinkedHashMap<String,String> map4Point = new LinkedHashMap<>();
        map4Point.put("param1","value1");
        map4Point.put("param2","value2");
        map4Point.put("param3","value3");
        //这里的Supplier 是 aConfig.getSupplier(),即从a渠道中若有多个配置则获取任一配置
        SmsFactory.getBySupplier("a").sendMessage("33333333333","templetaId", map4Point);

        //群发短信，固定模板短信
        //此方法使用配置文件中预设的短信模板进行短信发送,该方法指定的模板变量只能存在一个（配置文件中）
        //这个数组是批量发送消息的手机号，会一次发送多个，注意，发送多个账号也只会触发一次拦截器，比如黑名单拦截器，会在一次把所有手机号全部校验，而不是校验多次
        List<String> phones = new ArrayList<>();
        phones.add("44444444444");
        phones.add("55555555555");
        //这里的Supplier 是 aConfig.getSupplier(),即从a渠道中若有多个配置则获取任一配置
        SmsFactory.getBySupplier("a").massTexting(phones, "200001");

        //异步发送固定模板短信
        //这里的Supplier 是 aConfig.getSupplier(),即从a渠道中若有多个配置则获取任一配置
        //不关注执行结果
        SmsFactory.getBySupplier("a").sendMessageAsync("66666666666", "200001");
        //关注执行结果
        SmsFactory.getBySupplier("a").sendMessageAsync("77777777777", "200001", e-> System.out.println(e));

        //异步发送自定义模板短信
        LinkedHashMap<String,String> map4Async = new LinkedHashMap<>();
        map4Async.put("param1","value1");
        map4Async.put("param2","value2");
        map4Async.put("param3","value3");
        map4Async.put("param4","value4");
        //这里的Supplier 是 aConfig.getSupplier(),即从a渠道中若有多个配置则获取任一配置
        //不关注执行结果
        SmsFactory.getBySupplier("a").sendMessageAsync("88888888888","templetaId", map4Async);
        //关注执行结果
        SmsFactory.getBySupplier("a").sendMessageAsync("99999999999","templetaId", map4Async,e-> System.out.println(e));


        //使用自定义模板发送延时短信
        //使用指定模板发送短信
        LinkedHashMap<String,String> map4Delayed = new LinkedHashMap<>();
        map4Delayed.put("param1","value1");
        map4Delayed.put("param2","value2");
        map4Delayed.put("param3","value3");
        map4Delayed.put("param4","value4");
        map4Delayed.put("param5","value5");
        //这里的Supplier 是 aConfig.getSupplier(),即从a渠道中若有多个配置则获取任一配置
        SmsFactory.getBySupplier("a").delayedMessage("01111111111","templetaId", map4Delayed,1000L);

        //群发固定模板延迟短信
        List<String> phones4Delay = new ArrayList<>();
        phones4Delay.add("02222222222");
        phones4Delay.add("03333333333");
        //这里的Supplier 是 aConfig.getSupplier(),即从a渠道中若有多个配置则获取任一配置
        SmsFactory.getBySupplier("a").delayMassTexting(phones4Delay, "200001",1000L);

        //群发自定义模板延迟短信
        List<String> phones4Delayed = new ArrayList<>();
        phones4Delayed.add("04444444444");
        phones4Delayed.add("05555555555");
        LinkedHashMap<String,String> map4Delay = new LinkedHashMap<>();
        map4Delay.put("param1","value1");
        map4Delay.put("param2","value2");
        map4Delay.put("param3","value3");
        map4Delay.put("param4","value4");
        map4Delay.put("param5","value5");
        map4Delay.put("param6","value6");
        //这里的Supplier 是 aConfig.getSupplier(),即从a渠道中若有多个配置则获取任一配置
        SmsFactory.getBySupplier("a").delayMassTexting(phones4Delayed,"templetaId", map4Delay,1000L);

    }
}
