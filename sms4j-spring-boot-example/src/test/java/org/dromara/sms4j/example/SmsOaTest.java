package org.dromara.sms4j.example;

import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.api.OaSender;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;
import org.dromara.oa.comm.enums.OaType;
import org.dromara.oa.core.byteTalk.config.ByteTalkConfig;
import org.dromara.oa.core.dingTalk.config.DingTalkConfig;
import org.dromara.oa.core.provider.factory.OaFactory;
import org.dromara.oa.core.weTalk.config.WeTalkConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;


@Slf4j
@SpringBootTest
public class SmsOaTest {
    /**
     * 填测试手机号
     */
    private static final String PHONE = "";
    /**
     * 填access_token
     */
    private static final String TOKENID = "";
    /**
     * 填secret
     */
    private static final String SIGN = "";

    @Test
    public void oaDingTalkTest() {
        String key = "oaDingTalk";
        DingTalkConfig dingTalkConfig = new DingTalkConfig();
        dingTalkConfig.setConfigId(key);
        dingTalkConfig.setSign(SIGN);
        dingTalkConfig.setTokenId(TOKENID);
//        OaFactory.createAndRegisterOaSender(dingTalkConfig);
        OaSender alarm = OaFactory.createAndGetOa(dingTalkConfig);
        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(PHONE);
        request.setPhoneList(phones);
        request.setIsNoticeAll(true);
        request.setContent("测试消息");
        request.setTitle("测试消息标题");
        alarm.sender(request, MessageType.TEXT);

        // 测试markdown,无法@
//        request.setContent("#### 杭州天气 @150XXXXXXXX \n > 9度，西北风1级，空气良89，相对温度73%\n > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\n > ###### 10点20分发布 [天气](https://www.dingtalk.com) \n");
//        request.setTitle("杭州天气");
//        alarm.sender(request, MessageType.MARKDOWN);

    }

    @Test
    public void oaDingTalkByYamlTest() {
        String configId = "oaDingTalkByYaml";
        OaSender alarm = OaFactory.getSmsOaBlend(configId);
        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(PHONE);
        request.setPhoneList(phones);
        request.setIsNoticeAll(false);
        request.setContent("HertzBeat");
        request.setTitle("HertzBeat");
        alarm.sender(request, MessageType.TEXT);

    }

    @Test
    public void oaByteTalkTest() {
        String configId = "oaByteTalk";
        ByteTalkConfig dingTalkConfig = new ByteTalkConfig();
        dingTalkConfig.setConfigId(configId);
        dingTalkConfig.setSign(SIGN);
        dingTalkConfig.setTokenId(TOKENID);
        OaSender alarm = OaFactory.createAndGetOa(dingTalkConfig);
        Request request = new Request();
        ArrayList<String> userNameList = new ArrayList<>();
        userNameList.add("user1");
        userNameList.add("user2");
        request.setUserNamesList(userNameList);
        request.setContent("测试消息");
        request.setIsNoticeAll(true);
//        request.setTitle("测试消息标题");
        alarm.sender(request, MessageType.TEXT);
    }


    @Test
    public void oaByteTalkByYamlTest() {
        String configId = "oaByteTalkByYaml";
        OaSender alarm = OaFactory.getSmsOaBlend(configId);
        Request request = new Request();
        request.setOaType(OaType.BYTETALK.getType());
        ArrayList<String> userNameList = new ArrayList<>();
        userNameList.add("user1");
        userNameList.add("user2");
        request.setUserNamesList(userNameList);
        request.setContent("测试消息");
        request.setIsNoticeAll(true);
//        request.setTitle("测试消息标题");
        alarm.sender(request, MessageType.TEXT);
    }

    @Test
    public void oaWeTalkTest() {
        String configId = "oaWeTalk";
        WeTalkConfig weTalkConfig = new WeTalkConfig();
        weTalkConfig.setConfigId(configId);
        weTalkConfig.setTokenId(TOKENID);
        OaSender alarm = OaFactory.createAndGetOa(weTalkConfig);
        Request request = new Request();
        ArrayList<String> phones = new ArrayList<>();
        phones.add(PHONE);
        phones.add("131");
        request.setPhoneList(phones);
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add("123");
        request.setUserIdList(userIds);
//        request.setIsNoticeAll(true);
        request.setContent("测试消息");
        request.setTitle("测试消息标题");
        alarm.sender(request, MessageType.TEXT);

        // 测试markdown,无法@
        // 企业微信的markdown直接是content，没有title
//        request.setContent("#### 杭州天气 @150XXXXXXXX \n > 9度，西北风1级，空气良89，相对温度73%\n > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\n > ###### 10点20分发布 [天气](https://www.dingtalk.com) \n");
//        request.setTitle("杭州天气");
//        alarm.sender(request, MessageType.MARKDOWN);
    }


    @Test
    public void oaWeTalkByYamlTest() {
        String configId = "oaWeTalkByYaml";
        OaSender alarm = OaFactory.getSmsOaBlend(configId);
        Request request = new Request();
        request.setOaType(OaType.WETALK.getType());
        ArrayList<String> phones = new ArrayList<>();
        phones.add(PHONE);
        phones.add("131");
        request.setPhoneList(phones);
        ArrayList<String> userIds = new ArrayList<>();
        userIds.add("123");
        request.setUserIdList(userIds);
//        request.setIsNoticeAll(true);
        request.setContent("测试消息");
        request.setTitle("测试消息标题");
        alarm.sender(request, MessageType.TEXT);

        // 测试markdown,无法@
        // 企业微信的markdown直接是content，没有title
//        request.setContent("#### 杭州天气 @150XXXXXXXX \n > 9度，西北风1级，空气良89，相对温度73%\n > ![screenshot](https://img.alicdn.com/tfs/TB1NwmBEL9TBuNjy1zbXXXpepXa-2400-1218.png)\n > ###### 10点20分发布 [天气](https://www.dingtalk.com) \n");
//        request.setTitle("杭州天气");
//        alarm.sender(request, MessageType.MARKDOWN);
    }
}



