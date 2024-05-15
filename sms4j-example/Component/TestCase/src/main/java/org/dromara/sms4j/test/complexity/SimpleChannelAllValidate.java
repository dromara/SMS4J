
package org.dromara.sms4j.test.complexity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.manage.InterceptorStrategySmsManager;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.test.ITest;

import cn.hutool.core.lang.Assert;
import sms4j.interceptor.MySecIntercepterStrategy;

/*
* 账号级上限：4 7
* 时段极上限：3 5
* 渠道级上限：5 8
* */
public class SimpleChannelAllValidate implements ITest {

    /**
     * 填测试手机号
     */
    private   String PHONE = "";
    private   String PHONE1 = "";
    private   String CHANNEL1 = "";
    public SimpleChannelAllValidate(String PHONE, String PHONE1, String CHANNEL1) {
        this.PHONE = PHONE;
        this.PHONE1 = PHONE1;
        this.CHANNEL1 = CHANNEL1;
    }

    public void test() {
        this.test0();
        this.test1();
        this.test2();
        this.test3();
        this.test4();
        this.test5();
    }

    //基础带参发送
    public void test0() {
        SmsBlend smsBlend = SmsFactory.getBySupplier(CHANNEL1);
        LinkedHashMap map = new LinkedHashMap();
        map.put("test","400001");
        SmsResponse smsResponse = smsBlend.sendMessage(PHONE, map);
        Assert.isTrue(smsResponse.isSuccess());
        SmsResponse smsResponse1 = smsBlend.sendMessage(PHONE, map);
        Assert.isTrue(smsResponse1.isSuccess());
        SmsResponse smsResponse2 = smsBlend.sendMessage(PHONE, map);
        Assert.isTrue(smsResponse2.isSuccess());
    }


    //基础发送
    public void test1() {
        SmsBlend smsBlend = SmsFactory.getBySupplier(CHANNEL1);
        SmsResponse smsResponse = smsBlend.sendMessage(PHONE1, "400002");
        Assert.isTrue(smsResponse.isSuccess());
    }


    //参数校验测试，以下同时列举了多种错误情况，会被框架校验住并报错
    public void test2() {
        SmsBlendException knowEx = null;
        try {
            SmsFactory.getBySupplier(CHANNEL1).sendMessage("", "400003");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        knowEx = null;
        try {
            SmsFactory.getBySupplier(CHANNEL1).massTexting(new ArrayList<String>(), "400004");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
    }

    //黑名单测试
    public void test3() {
        SmsBlend smsBlend = SmsFactory.getBySupplier(CHANNEL1);
        //单黑名单添加
        smsBlend.joinInBlacklist(PHONE);
        SmsBlendException knowEx = null;
        try {
            smsBlend.sendMessage(PHONE, "400005");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        //单黑名单移除
        smsBlend.removeFromBlacklist(PHONE);
        SmsResponse smsResponse = smsBlend.sendMessage(PHONE, "400006");
        Assert.isTrue(smsResponse.isSuccess());
        //批量黑名单添加
        smsBlend.batchJoinBlacklist(Collections.singletonList(PHONE));
        knowEx = null;
        try {
            smsBlend.sendMessage(PHONE, "400007");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        //批量黑名单添加
        smsBlend.batchRemovalFromBlacklist(Collections.singletonList(PHONE));
        smsResponse = smsBlend.sendMessage(PHONE, "400008");
        Assert.isTrue(smsResponse.isSuccess());
    }

    //时段级上限测试、前面PHONE1在1分钟内已经成功发送3笔，再发就会报错 参数配置 3 停顿1分钟刷新时段级计数
    public void test4()  {
        SmsBlendException knowEx = null;
        try {
            SmsFactory.getBySupplier(CHANNEL1).sendMessage(PHONE, "400009");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        System.out.println("等待时段刷新");
        try {
            Thread.sleep(60 * 1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("时段过期");
    }


    //账户级上限测试、前面已经PHONE成功发送3笔再发一笔成功的，再发就会报错 参数配置 4
    public void test5() {
        MySecIntercepterStrategy mySecIntercepterStrategy = new MySecIntercepterStrategy();
        InterceptorStrategySmsManager.setInterceptorStrategyMapping(mySecIntercepterStrategy);
        SmsResponse smsResponse = SmsFactory.getBySupplier(CHANNEL1).sendMessage(PHONE, "400010");
        Assert.isTrue(smsResponse.isSuccess());
        SmsBlendException knowEx = null;
        try {
            SmsFactory.getBySupplier(CHANNEL1).sendMessage(PHONE, "400012");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
    }

    //渠道级上限测试、前面已经CHANNEL1成功发送5笔，再发就会报错 参数配置 5
    public void test6() {
        SmsBlendException knowEx = null;
        try {
            SmsFactory.getBySupplier(CHANNEL1).sendMessage(PHONE1, "400013");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
    }
}

