package org.dromara.sms4j.demo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.javase.config.SEInitializer;
import org.dromara.sms4j.provider.config.SmsConfig;

import sms4j.dao.MySmsDao;
import sms4j.local.a.AConfig;
import sms4j.local.a.AFactory;

public class ConfigBeanBlackList {
    public static void main(String[] args) {

        // 全局基础配置
        SmsConfig smsConfig = new SmsConfig();

        // 开启限制，关闭后只有参数检验拦截器生效
        smsConfig.setRestricted(true);

        // 服务商渠道配置
        List<SupplierConfig> blends = new ArrayList<SupplierConfig>();
        // 服务商
        AConfig aConfig = new AConfig();
        aConfig.setConfigId("a1");
        blends.add(aConfig);

        //为了方便成功通过的演示，这里是自定义的服务商渠道，如果使用sms4j预置的渠道无需此配置
        AFactory myFactory = new AFactory();

        //初始化SMS
        SEInitializer
            //这里之后的方法顺序可以任意排列，但是必须以initializer开始
            .initializer()
            //为了方便成功通过的演示，这里是自定义的服务商渠道，如果使用sms4j预置的渠道无需此配置
            .registerFactory(myFactory)
            //这里之前的方法顺序可以任意排列，但是必须以fromConfig结尾
            .fromConfig(smsConfig,blends);

        //获取任一smsBlend
        SmsBlend smsBlend = SmsFactory.getBySupplier("a");

        //加入黑名单
        smsBlend.joinInBlacklist("11111111111");

        //会报错并且发送失败
        try {
            smsBlend.sendMessage("11111111111", "200001");
        }catch (Exception e){
            System.out.println(e);
        }

        //移除黑名单
        smsBlend.removeFromBlacklist("11111111111");

        //发送成功
        smsBlend.sendMessage("11111111111", "200001");

        //批量加入黑名单
        smsBlend.batchJoinBlacklist(Collections.singletonList("11111111111"));

        //会报错并且发送失败
        try {
            smsBlend.sendMessage("11111111111", "200001");
        }catch (Exception e){
            System.out.println(e);
        }

        //批量移除黑名单
        smsBlend.batchRemovalFromBlacklist(Collections.singletonList("11111111111"));

        //发送成功
        smsBlend.sendMessage("11111111111", "200001");

        //命中记录获取
        System.out.println(smsBlend.getTriggerRecord());

        //命中记录清理
        smsBlend.clearTriggerRecord();

        //命中记录获取
        System.out.println(smsBlend.getTriggerRecord());
    }
}
