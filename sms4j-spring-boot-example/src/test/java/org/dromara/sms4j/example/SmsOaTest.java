package org.dromara.sms4j.example;

import lombok.extern.slf4j.Slf4j;
import org.dromara.oa.comm.config.OaConfig;
import org.dromara.oa.comm.entity.Request;
import org.dromara.oa.comm.enums.MessageType;
import org.dromara.oa.comm.enums.OaType;
import org.dromara.oa.core.factory.OaFactory;
import org.dromara.oa.core.service.SenderImpl;
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
    /**
     * 填oa的key
     * value是一个OaConfig
     */
    private static final String oaKey = "";

    @Test
    public void oaDingTalkTest() {
        OaFactory.put(oaKey, OaConfig.builder()
                .OaType(OaType.DINGTALK.getType())
                .tokenId(TOKENID)
                .sign(SIGN).build());
        SenderImpl alarm = OaFactory.createSender(oaKey);
        Request request = new Request();
        request.setOaType(OaType.DINGTALK.getType());
        ArrayList<String> phones = new ArrayList<>();
        phones.add(PHONE);
        request.setPhones(phones);
        request.setContent("测试消息");
        request.setTitle("测试消息标题");
        alarm.sender(request, MessageType.TEXT);
    }
}



