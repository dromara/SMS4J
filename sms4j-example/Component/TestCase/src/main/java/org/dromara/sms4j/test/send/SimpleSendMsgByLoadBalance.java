package org.dromara.sms4j.test.send;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.test.ITest;

import cn.hutool.core.lang.Assert;

public class SimpleSendMsgByLoadBalance implements ITest {

    private final String PHONE;

    public SimpleSendMsgByLoadBalance(String channel, String phone) {
        PHONE = phone;
    }
    //通过 负载均衡器 获取 SmsBlend，发送消息给 CHANNEL 厂商
    public void test(){
        SmsBlend smsBlend = SmsFactory.getSmsBlend();
        //基础发送
        SmsResponse smsResponse = smsBlend.sendMessage(PHONE, "10002");

        Assert.isTrue(smsResponse.isSuccess());
    }
}
