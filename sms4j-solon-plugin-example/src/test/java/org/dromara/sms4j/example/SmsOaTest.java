package org.dromara.sms4j.example;

import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.entity.WeTalkRequestArticle;
import org.dromara.oa.comm.enums.MessageType;
import org.dromara.oa.core.byteTalk.config.ByteTalkConfig;
import org.dromara.oa.core.dingTalk.config.DingTalkConfig;
import org.dromara.oa.core.provider.factory.OaFactory;
import org.dromara.oa.core.weTalk.config.WeTalkConfig;
import org.junit.jupiter.api.Test;
import org.noear.solon.test.SolonTest;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.CountDownLatch;


@Slf4j
@SolonTest
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
        // 支持通过手机号@
        request.setPhoneList(phones);
        // 支持@all
//        request.setIsNoticeAll(true);
        request.setContent("测试消息");

        alarm.sender(request, MessageType.DING_TALK_TEXT);

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
        // 支持@all
        request.setIsNoticeAll(true);
        request.setContent("#### 杭州天气 @150XXXXXXXX \n > 9度，西北风1级，空气良89，相对温度73%\n > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\n > ###### 10点20分发布 [天气](https://www.dingtalk.com) \n");
        request.setTitle("标题");
        alarm.sender(request, MessageType.DING_TALK_MARKDOWN);

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
        request.setContent("这个即将发布的新版本，创始人xx称它为红树林。而在此之前，每当面临重大升级，产品经理们都会取一个应景的代号，这一次，为什么是红树林");
        request.setTitle("点击跳转到钉钉");
        request.setMessageUrl("https://www.dingtalk.com/s?__biz=MzA4NjMwMTA2Ng==&mid=2650316842&idx=1&sn=60da3ea2b29f1dcc43a7c8e4a7c97a16&scene=2&srcid=09189AnRJEdIiWVaKltFzNTw&from=timeline&isappinstalled=0&key=&ascene=2&uin=&devicetype=android-23&version=26031933&nettype=WIFI");
        request.setPicUrl("https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png");
        alarm.sender(request, MessageType.DING_TALK_LINK);


    }

    /**
     * DingTalk的异步消息发送
     */
    @Test
    public void oaDingTalkAsyncTest() {
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
        // 支持通过手机号@
        request.setPhoneList(phones);
        // 支持@all
//        request.setIsNoticeAll(true);
        request.setContent("测试消息");

        // 异步发送方式
        alarm.senderAsync(request, MessageType.DING_TALK_TEXT);
        alarm.senderAsync(request, MessageType.DING_TALK_TEXT, smsResponse -> System.out.println("ConfigId为" + smsResponse.getOaConfigId() + "的异步任务发送成功"));

        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
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
        CountDownLatch user = new CountDownLatch(1);
        // 模拟10条不同优先级的消息
        for (int i = 0; i < 3; i++) {
            Random random = new Random();
            new Thread(() -> {
                int priority = random.nextInt(10);
                try {
                    // 等待十个请求
                    System.out.println(priority + "等待");
                    user.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Request request = new Request();
                ArrayList<String> phones = new ArrayList<>();
                phones.add(DingTalkPHONE);
                request.setPhoneList(phones);
                request.setIsNoticeAll(false);
                request.setPriority(priority);
                //测试-1-TEXT
                request.setContent("该消息优先级为" + priority);
                alarm.senderAsyncByPriority(request, MessageType.DING_TALK_TEXT);
                System.out.println("优先级为" + priority + "的异步任务已提交");

            }).start();
        }
        System.out.println("开始模拟");
        user.countDown();
        // 防止主线程挂掉
        try {
            Thread.sleep(2000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void oaDingTalkByYamlTest() {
        String configId = "oaDingTalkByYaml";
        OaSender alarm = OaFactory.getSmsOaBlend(configId);
        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(DingTalkPHONE);
        // 支持通过手机号@
        request.setPhoneList(phones);
        // 支持@all
//        request.setIsNoticeAll(true);
        request.setContent("测试消息");
        alarm.sender(request, MessageType.DING_TALK_TEXT);
    }
    //***********************ByteTalk-Test************************//
    /**
     * 填测试手机号
     */
    private static final String ByteTalkUSERID = "";
    /**
     * 填access_token
     */
    private static final String ByteTalkTOKENID = "";
    /**
     * 填secret
     */
    private static final String ByteTalkSIGN = "";

    /**
     * ByteTalk的Text测试
     */
    @Test
    public void oaByteTalkText() {
        String key = "oaByteTalk";
        ByteTalkConfig byteTalkConfig = new ByteTalkConfig();
        byteTalkConfig.setConfigId(key);
        byteTalkConfig.setTokenId(ByteTalkTOKENID);
        byteTalkConfig.setSign(ByteTalkSIGN);
        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(byteTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        Request request = new Request();
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(ByteTalkUSERID);
        //测试text
        request.setUserIdList(userIds);
        request.setIsNoticeAll(true);
        request.setContent("测试消息");
        alarm.sender(request, MessageType.BYTE_TALK_TEXT);
    }


    /**
     * ByteTalk的异步消息发送
     */
    @Test
    public void oaByteTalkAsyncText() {
        String key = "oaByteTalk";
        ByteTalkConfig byteTalkConfig = new ByteTalkConfig();
        byteTalkConfig.setConfigId(key);
        byteTalkConfig.setTokenId(ByteTalkTOKENID);
        byteTalkConfig.setSign(ByteTalkSIGN);
        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(byteTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        Request request = new Request();
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(ByteTalkUSERID);
        //测试text
        request.setUserIdList(userIds);
        request.setIsNoticeAll(true);
        request.setContent("测试消息");

        alarm.senderAsync(request, MessageType.BYTE_TALK_TEXT);
        alarm.senderAsync(request, MessageType.BYTE_TALK_TEXT, smsResponse -> System.out.println("ConfigId为" + smsResponse.getOaConfigId() + "的异步任务发送成功"));

        // 防止主线程挂掉
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void oaByteTalkByYamlTest() {
        String configId = "oaByteTalkByYaml";
        OaSender alarm = OaFactory.getSmsOaBlend(configId);
        Request request = new Request();
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add(ByteTalkUSERID);
        //测试text
        request.setUserIdList(userIds);
        request.setIsNoticeAll(true);
        request.setContent("测试消息");
        alarm.sender(request, MessageType.BYTE_TALK_TEXT);
    }
    //***********************WeTalk-Test************************//
    /**
     * 填测试手机号
     */
    private static final String WeTalkPHONE = "";
    /**
     * 填测试UserId
     */
    private static final String WeTalkUSERID = "";
    /**
     * 填access_token
     */
    private static final String WeTalkTOKENID = "";

    /**
     * WeTalk的Text测试
     */
    @Test
    public void oaWeTalkText() {
        String key = "oaWeTalk";
        WeTalkConfig WeTalkConfig = new WeTalkConfig();
        WeTalkConfig.setConfigId(key);
        WeTalkConfig.setTokenId(WeTalkTOKENID);

        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(WeTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(WeTalkPHONE);
        //测试text
        request.setPhoneList(phones);
        request.setIsNoticeAll(true);
        request.setContent("测试消息");

        alarm.sender(request, MessageType.WE_TALK_TEXT);

    }

    /**
     * WeTalk的Markdown测试--不支持@all,只能通过userId进行
     */
    @Test
    public void oaWeTalkMarkdown() {
        String key = "oaWeTalk";
        WeTalkConfig WeTalkConfig = new WeTalkConfig();
        WeTalkConfig.setConfigId(key);
        WeTalkConfig.setTokenId(WeTalkTOKENID);

        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(WeTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        Request request = new Request();
        // 管理后台-通讯录 账号就是userid，或者通过接口获取部门列表 再获取部门成员详情获取userid
        ArrayList<String> userIdList = new ArrayList<>();
        userIdList.add(WeTalkUSERID);
        request.setUserIdList(userIdList);

        request.setContent(
                "实时新增用户反馈<font color=\"warning\">132例</font>，请相关同事注意。\n" +
                        ">类型:<font color=\"comment\">用户反馈</font>" +
                        ">普通用户反馈:<font color=\"comment\">117例</font>" +
                        ">VIP用户反馈:<font color=\"comment\">15例</font>");

        alarm.sender(request, MessageType.WE_TALK_MARKDOWN);


    }

    /**
     * WeTalk的News测试
     */
    @Test
    public void oaWeTalkNews() {
        String key = "oaWeTalk";
        WeTalkConfig WeTalkConfig = new WeTalkConfig();
        WeTalkConfig.setConfigId(key);
        WeTalkConfig.setTokenId(WeTalkTOKENID);

        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(WeTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        Request request = new Request();
        ArrayList<WeTalkRequestArticle> articles = new ArrayList<>();
        articles.add(new WeTalkRequestArticle("中秋节礼品领取", "今年中秋节公司有豪礼相送", "www.qq.com", "http://res.mail.qq.com/node/ww/wwopenmng/images/independent/doc/test_pic_msg1.png"));
        request.setArticleList(articles);

        alarm.sender(request, MessageType.WE_TALK_NEWS);
    }

    /**
     * WeTalk的异步消息发送
     */
    @Test
    public void oaWeTalkAsyncText() {
        String key = "oaWeTalk";
        WeTalkConfig WeTalkConfig = new WeTalkConfig();
        WeTalkConfig.setConfigId(key);
        WeTalkConfig.setTokenId(WeTalkTOKENID);

        // 根据配置创建服务实例并注册
        OaFactory.createAndRegisterOaSender(WeTalkConfig);
        OaSender alarm = OaFactory.getSmsOaBlend(key);

        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(WeTalkPHONE);
        //测试text
        request.setPhoneList(phones);
        request.setIsNoticeAll(true);
        request.setContent("测试消息");

        // 异步发送方式
        alarm.senderAsync(request, MessageType.WE_TALK_TEXT);
        alarm.senderAsync(request, MessageType.WE_TALK_TEXT, smsResponse -> System.out.println("ConfigId为" + smsResponse.getOaConfigId() + "的异步任务发送成功"));

        // 防止主线程挂掉
        try {
            Thread.sleep(3000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void oaWeTalkByYamlTest() {
        String configId = "oaWeTalkByYaml";
        OaSender alarm = OaFactory.getSmsOaBlend(configId);
        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(WeTalkPHONE);
        request.setPhoneList(phones);
        request.setIsNoticeAll(true);
        request.setContent("SMS4JContent");
        alarm.sender(request, MessageType.WE_TALK_TEXT);
    }
}



