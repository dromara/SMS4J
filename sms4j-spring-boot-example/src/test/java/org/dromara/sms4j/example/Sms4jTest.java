package org.dromara.sms4j.example;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.provider.enumerate.SupplierType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
class Sms4jTest {

    /**
     * 填测试手机号
     */
    private static final String PHONE = "";

    @Test
    public void alibabaSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 阿里
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.ALIBABA).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue("OK".equals(smsResponse.getCode()) && smsResponse.isSuccess());
    }

    @Test
    public void huaweiSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 华为
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.HUAWEI).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue("000000".equals(smsResponse.getCode()) && smsResponse.isSuccess());
    }

    @Test
    public void cloopenSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 容联云
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.CLOOPEN).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue("000000".equals(smsResponse.getCode()) && smsResponse.isSuccess());
    }

    @Test
    public void emaySmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 亿美软通
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.EMAY).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue("success".equals(smsResponse.getCode()) && smsResponse.isSuccess());
    }

    @Test
    public void jdCloudSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 京东云
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.JD_CLOUD).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(smsResponse.isSuccess());
    }

    @Test
    public void yunPianSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 云片
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.YUNPIAN).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue("0".equals(smsResponse.getCode()) && smsResponse.isSuccess());
    }

    @Test
    public void tencentSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 腾讯
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.TENCENT).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue("Ok".equals(smsResponse.getCode()) && smsResponse.isSuccess());
    }

    @Test
    public void uniSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 合一
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.UNI_SMS).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue("Success".equals(smsResponse.getMessage()) && smsResponse.isSuccess());
    }

    @Test
    public void cyYunSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 天翼云
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.CTYUN).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue("OK".equals(smsResponse.getCode()) && smsResponse.isSuccess());
    }

    @Test
    public void neteaseSmsTest() {
        if (StrUtil.isBlank(PHONE)) {
            return;
        }
        // 网易云短信
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.NETEASE).sendMessage(PHONE, SmsUtil.getRandomInt(6));
        log.info(JSONUtil.toJsonStr(smsResponse));
        Assert.isTrue(Long.parseLong(smsResponse.getCode()) <= 200 && smsResponse.isSuccess());
    }

}