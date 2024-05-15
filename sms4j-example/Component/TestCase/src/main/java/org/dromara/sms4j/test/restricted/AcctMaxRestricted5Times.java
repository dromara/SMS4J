package org.dromara.sms4j.test.restricted;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.test.ITest;

import cn.hutool.core.lang.Assert;

public class AcctMaxRestricted5Times implements ITest {

    private final String CHANNEL;
    private final String PHONE;

    public AcctMaxRestricted5Times(String channel, String phone) {
        CHANNEL = channel;
        PHONE = phone;
    }

    @Override
    public void test() {
        SmsBlend smsBlend = SmsFactory.getBySupplier(CHANNEL);
        SmsResponse smsResponse1 = smsBlend.sendMessage(PHONE,"300001");
        Assert.isTrue(smsResponse1.isSuccess());
        SmsResponse smsResponse2 = smsBlend.sendMessage(PHONE,"300002");
        Assert.isTrue(smsResponse2.isSuccess());
        SmsResponse smsResponse3 = smsBlend.sendMessage(PHONE,"300003");
        Assert.isTrue(smsResponse3.isSuccess());
        SmsResponse smsResponse4 = smsBlend.sendMessage(PHONE,"300004");
        Assert.isTrue(smsResponse4.isSuccess());
        SmsResponse smsResponse5 = smsBlend.sendMessage(PHONE,"300005");
        Assert.isTrue(smsResponse5.isSuccess());
        //第六次发送已经达到渠道上限应该抛出异常
        SmsBlendException knowEx = null;
        try {
             smsBlend.sendMessage(PHONE,"300006");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);

    }
}
