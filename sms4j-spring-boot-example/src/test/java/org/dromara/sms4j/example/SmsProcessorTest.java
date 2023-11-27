package org.dromara.sms4j.example;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * @author sh1yu
 */
@Slf4j
@SpringBootTest
public class SmsProcessorTest {
    /**
     * 填测试手机号
     */
    private static final String PHONE = "13899998888";

    //参数校验测试
    @Test
    public void testParamValidate() {
        SmsBlendException knowEx = null;
        try {
            SmsFactory.getBySupplier(SupplierConstant.ALIBABA).sendMessage("", SmsUtils.getRandomInt(6));
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        knowEx = null;
        try {
            SmsFactory.getBySupplier(SupplierConstant.ALIBABA).sendMessage(PHONE, "");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        knowEx = null;
        try {
            SmsFactory.getBySupplier(SupplierConstant.ALIBABA).sendMessage(PHONE, "111", new LinkedHashMap<>());
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        knowEx = null;
        try {
            SmsFactory.getBySupplier(SupplierConstant.ALIBABA).massTexting(Collections.singletonList(PHONE), "");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        knowEx = null;
        try {
            SmsFactory.getBySupplier(SupplierConstant.ALIBABA).massTexting(Collections.singletonList(PHONE), "2222", new LinkedHashMap<>());
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        knowEx = null;
        try {
            SmsFactory.getBySupplier(SupplierConstant.ALIBABA).massTexting(new ArrayList<String>(), "321321");
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
    }

    //黑名单测试 黑名单手机号13899998888
    @Test
    public void testBlackList() {
        SmsBlend smsBlend = SmsFactory.getBySupplier(SupplierConstant.UNISMS);
        //单黑名单添加
        smsBlend.joinInBlacklist(PHONE);
        SmsBlendException knowEx = null;
        try {
            smsBlend.sendMessage(PHONE, SmsUtils.getRandomInt(6));
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        //单黑名单移除
        smsBlend.removeFromBlacklist(PHONE);
        SmsResponse smsResponse = smsBlend.sendMessage(PHONE, SmsUtils.getRandomInt(6));
        Assert.isTrue(smsResponse.isSuccess());
        //批量黑名单添加
        smsBlend.batchJoinBlacklist(Collections.singletonList(PHONE));
        knowEx = null;
        try {
            smsBlend.sendMessage(PHONE, SmsUtils.getRandomInt(6));
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
        //批量黑名单添加
        smsBlend.removeFromBlacklist(PHONE);
        smsResponse = smsBlend.sendMessage(PHONE, SmsUtils.getRandomInt(6));
        Assert.isTrue(smsResponse.isSuccess());

    }

    //账号级上限测试、需成功发送一笔，特殊配置
    @Test
    public void testAcctLimt() {
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.UNISMS).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        Assert.isTrue(smsResponse.isSuccess());
        SmsBlendException knowEx = null;
        try {
            SmsFactory.getBySupplier(SupplierConstant.UNISMS).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
    }

    //账号级上限测试、需成功发送一笔，特殊配置
    @Test
    public void testChannelLimt() {
        SmsResponse smsResponse = SmsFactory.getBySupplier(SupplierConstant.UNISMS).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        Assert.isTrue(smsResponse.isSuccess());

        SmsBlendException knowEx = null;
        try {
            SmsFactory.getBySupplier(SupplierConstant.UNISMS).sendMessage(PHONE, SmsUtils.getRandomInt(6));
        } catch (SmsBlendException e) {
            knowEx = e;
        }
        Assert.notNull(knowEx);
    }

}
