package org.dromara.sms4j.test.validate;

import cn.hutool.core.lang.Assert;

import java.util.ArrayList;
import java.util.Collections;

import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.test.ITest;

public class ParamValidate implements ITest {

    private final String CHANNEL;
    private final String PHONE;

    public ParamValidate(String channel, String phone) {
        CHANNEL = channel;
        PHONE = phone;
    }

    /*
    * 验证参数是否正确，传入空 电话号会被校验住
    * */
    public void test() {
        SmsBlendException knowEx = null;
        try {
            SmsFactory.getBySupplier(CHANNEL).sendMessage("", "200001");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        knowEx = null;
        try {
            SmsFactory.getBySupplier(CHANNEL).massTexting(new ArrayList<String>(), "200002");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        SmsResponse smsResponse = SmsFactory.getBySupplier(CHANNEL).massTexting(Collections.singletonList(PHONE), "200003");
        Assert.isTrue(smsResponse.isSuccess());

    }
}
