package org.dromara.sms4j.test.validate;

import cn.hutool.core.lang.Assert;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.test.ITest;

public class BlackListValidate implements ITest {

    private final String CHANNEL;
    private final String PHONE;

    public BlackListValidate(String channel, String phone) {
        CHANNEL = channel;
        PHONE = phone;
    }

    //黑名单的添加删除的功能测试，以及黑名单命中记录的处理
    @Override
    public void test() {
        SmsBlend smsBlend = SmsFactory.getBySupplier(CHANNEL);
        //单黑名单添加
        smsBlend.joinInBlacklist(PHONE);
        SmsBlendException knowEx = null;
        //下面会因为被黑名单触发拦截所以报错
        try {
            smsBlend.sendMessage(PHONE, "200003");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        //查看命中记录是否正确记录
        checkBlackListRecord(smsBlend);
        //单黑名单移除
        smsBlend.removeFromBlacklist(PHONE);
        //正常发送
        SmsResponse smsResponse = smsBlend.sendMessage(PHONE, "200004");
        Assert.isTrue(smsResponse.isSuccess());
        //清理黑名单命中记录
        smsBlend.clearTriggerRecord();
        //清理黑名单命中记录后，黑名单记录中应该没有该记录所以checkBlackListRecord会报错
        RuntimeException runtimeException = null;
        try{
            checkBlackListRecord(smsBlend);
        }catch (RuntimeException e){
            runtimeException = e;
        }
        Assert.notNull(runtimeException);
        //批量黑名单添加
        smsBlend.batchJoinBlacklist(Collections.singletonList(PHONE));
        knowEx = null;
        //下面会因为被黑名单触发拦截所以报错
        try {
            smsBlend.sendMessage(PHONE, "200005");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        //查看命中记录是否正确记录
        checkBlackListRecord(smsBlend);
        //批量黑名单删除
        smsBlend.batchRemovalFromBlacklist(Collections.singletonList(PHONE));
        //应该正常发送
        smsResponse = smsBlend.sendMessage(PHONE, "200006");
        Assert.isTrue(smsResponse.isSuccess());
    }

    public void checkBlackListRecord(SmsBlend smsBlend){
        List<String> triggerRecord = smsBlend.getTriggerRecord();
        if (triggerRecord.stream().filter(record -> record.contains(PHONE)).collect(Collectors.toList()).size() == 0) {
            throw new RuntimeException("blackList model fail");
        }
    }
}
