package org.dromara.sms4j.test.send;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.test.ITest;

import cn.hutool.core.lang.Assert;

public class SimpleSendMsgById implements ITest {

    private final String CHANNEL;
    private final String PHONE;

    public SimpleSendMsgById(String channel, String phone) {
        CHANNEL = channel;
        PHONE = phone;
    }
    //通过 配置id 获取 SmsBlend，发送消息给 CHANNEL 厂商
    public void test(){
        SmsBlend smsBlend = SmsFactory.getSmsBlend(CHANNEL);
        //基础发送
        SmsResponse smsResponse = smsBlend.sendMessage(PHONE, "10001");

        Assert.isTrue(smsResponse.isSuccess());
    }
}
