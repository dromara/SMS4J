package org.dromara.sms4j.example;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.core.config.SupplierFactory;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Sms4jTest {
    public static final String USER_AGENT = "uni-java-sdk" + "/0.0.4";

    @Test
    public void uniSmsTest() {
        UniConfig build = UniConfig.builder()
                .signature("洙旭阁")
                .accessKeyId("7Cr1ZaQVJQ11Ap4HBQMo7xmFg")
                .templateId("2001")
                .templateName("message")
                .isSimple(true)
                .build();
        SupplierFactory.setUniConfig(build);

        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.UNI_SMS).sendMessage("17531165952", "123123");
        System.out.println(smsResponse);
//        UniResponse sms = UniSMS.buildMessage().setTo("17531165952").setSignature("洙旭阁").setTemplateId("2001").setTemplateData(map).send();
//        System.out.println(sms);
//        Uni.getClient().request("sms.message.send",mes)
//        UniClient.request()
    }

    @Test
    public void alibabaSmsTest() {
        String phone = "";
        if (StrUtil.isBlank(phone)) {
            return;
        }
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.ALIBABA).sendMessage(phone, "123456");
        Assert.isTrue("OK".equals(smsResponse.getCode()));
    }

}
