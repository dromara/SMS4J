package org.dromara.sms4j.example;

import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;
import org.dromara.oa.core.dingTalk.config.DingTalkConfig;
import org.dromara.oa.core.provider.factory.OaFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Random;


@Slf4j
@SpringBootTest
public class SmsOaTest {
    //***********************DingTalk-Test************************//
    /**
     * 填测试手机号
     */
    private static final String DingTalkPHONE = "";
    /**
     * 填access_token
     */
    private static final String DingTalkTOKENID = "";
    /**
     * 填secret
     */
    private static final String DingTalkSIGN = "";


    /**
     * DingTalk的Text测试
     */
    @Test
    public void oaDingTalkText() {
        String key = "oaDingTalk";
        DingTalkConfig dingTalkConfig = new DingTalkConfig();
        dingTalkConfig.setConfigId(key);
        dingTalkConfig.setSign(DingTalkSIGN);
        dingTalkConfig.setTokenId(DingTalkTOKENID);

        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(dingTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(DingTalkPHONE);
        //测试text
        request.setPhoneList(phones);
        request.setIsNoticeAll(false);
        request.setContent("测试消息");
        request.setTitle("测试消息标题");

        // 异步发送方式
        alarm.senderAsync(request, MessageType.TEXT);
        alarm.senderAsync(request, MessageType.TEXT, smsResponse -> System.out.println("ConfigId为" + smsResponse.getOaConfigId() + "的异步任务发送成功"));

        System.out.println("异步任务已全部提交");
        System.out.println("下面是同步任务");

        alarm.sender(request, MessageType.TEXT);

        System.out.println("同步任务已执行完");

    }

    /**
     * DingTalk的Markdown测试
     */
    @Test
    public void oaDingTalkMarkdown() {
        String key = "oaDingTalk";
        DingTalkConfig dingTalkConfig = new DingTalkConfig();
        dingTalkConfig.setConfigId(key);
        dingTalkConfig.setSign(DingTalkSIGN);
        dingTalkConfig.setTokenId(DingTalkTOKENID);

        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(dingTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(DingTalkPHONE);
        request.setPhoneList(phones);
        request.setIsNoticeAll(true);
        request.setContent("#### 杭州天气 @150XXXXXXXX \n > 9度，西北风1级，空气良89，相对温度73%\n > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\n > ###### 10点20分发布 [天气](https://www.dingtalk.com) \n");
        request.setTitle("测试消息标题");
        alarm.senderAsync(request, MessageType.MARKDOWN);

        alarm.senderAsync(request, MessageType.MARKDOWN, smsResponse -> System.out.println("ConfigId为" + smsResponse.getOaConfigId() + "的异步任务发送成功"));

        System.out.println("异步任务已全部提交");
        System.out.println("下面是同步任务");

        alarm.sender(request, MessageType.MARKDOWN);

        System.out.println("同步任务已执行完");
    }

    /**
     * DingTalk的Link测试
     */
    @Test
    public void oaDingTalkLink() {
        String key = "oaDingTalk";
        DingTalkConfig dingTalkConfig = new DingTalkConfig();
        dingTalkConfig.setConfigId(key);
        dingTalkConfig.setSign(DingTalkSIGN);
        dingTalkConfig.setTokenId(DingTalkTOKENID);

        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(dingTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(DingTalkPHONE);
        // 测试link
        request.setPhoneList(phones);
        request.setIsNoticeAll(true);
        request.setContent("这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林");
        request.setTitle("点击跳转到钉钉");
        request.setMessageUrl("https://www.dingtalk.com/s?__biz=MzA4NjMwMTA2Ng==&mid=2650316842&idx=1&sn=60da3ea2b29f1dcc43a7c8e4a7c97a16&scene=2&srcid=09189AnRJEdIiWVaKltFzNTw&from=timeline&isappinstalled=0&key=&ascene=2&uin=&devicetype=android-23&version=26031933&nettype=WIFI");
        request.setPicUrl("https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png");

        alarm.senderAsync(request, MessageType.LINK);

        alarm.senderAsync(request, MessageType.LINK, smsResponse -> System.out.println("ConfigId为" + smsResponse.getOaConfigId() + "的异步任务发送成功"));

        System.out.println("异步任务已全部提交");
        System.out.println("下面是同步任务");

        alarm.sender(request, MessageType.LINK);

        System.out.println("同步任务已执行完");
    }

    /**
     * 异步优先级尽可能优先级高的消息先发送,但是获取响应会受网络影响
     */
    @Test
    public void oaDingTalkAsyncByPriority() {
        String key = "oaDingTalk";
        DingTalkConfig dingTalkConfig = new DingTalkConfig();
        dingTalkConfig.setConfigId(key);
        dingTalkConfig.setSign(DingTalkSIGN);
        dingTalkConfig.setTokenId(DingTalkTOKENID);

        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(dingTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        // 模拟10条不同优先级的消息
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int priority = random.nextInt(10);

            Request request = new Request();
            ArrayList<String> phones = new ArrayList<>();
            phones.add(DingTalkPHONE);
            request.setPhoneList(phones);
            request.setIsNoticeAll(false);
            request.setPriority(priority);
            request.setTitle("优先级为" + priority);
            //测试-1-TEXT
//            request.setContent("该消息优先级为"+ priority);
//            alarm.senderAsyncByPriority(request, MessageType.TEXT);
//            System.out.println("优先级为"+priority+"的异步任务已提交");

            // 测试-2-MARKDOWN
//            request.setContent("该消息优先级为"+ priority+"\n#### ++杭州天气 @150XXXXXXXX \n > 9度，西北风1级，空气良89，相对温度73%\n > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\n > ###### 10点20分发布 [天气](https://www.dingtalk.com) \n");
//            alarm.senderAsyncByPriority(request, MessageType.MARKDOWN);

            // 测试-3-LINK
            request.setContent("该消息优先级为" + priority + "这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林");
            request.setMessageUrl("https://www.dingtalk.com/s?__biz=MzA4NjMwMTA2Ng==&mid=2650316842&idx=1&sn=60da3ea2b29f1dcc43a7c8e4a7c97a16&scene=2&srcid=09189AnRJEdIiWVaKltFzNTw&from=timeline&isappinstalled=0&key=&ascene=2&uin=&devicetype=android-23&version=26031933&nettype=WIFI");
            request.setPicUrl("https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png");

            // 发送
            alarm.senderAsyncByPriority(request, MessageType.LINK);

            System.out.println("优先级为" + priority + "的异步任务已提交");
        }
        while (true){
        }
    }

    @Test
    public void oaDingTalkByYamlTest() {
        String configId = "oaDingTalkByYaml";
        OaSender alarm = OaFactory.getSmsOaBlend(configId);
        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(DingTalkPHONE);
        request.setPhoneList(phones);
        request.setIsNoticeAll(false);
        request.setContent("HertzBeat");
        request.setTitle("HertzBeat");
        alarm.sender(request, MessageType.TEXT);
    }
}



