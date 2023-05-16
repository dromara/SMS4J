package org.dromara.sms4j.test;

import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.core.config.SupplierFactory;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.noear.solon.test.SolonJUnit4ClassRunner;
import org.noear.solon.test.SolonTest;

@RunWith(SolonJUnit4ClassRunner.class)
@SolonTest
public class Sms4jTest {

    @Test
    public void uniSmsTest() {
        UniConfig build = UniConfig.builder()
                .signature("***")
                .accessKeyId("7Cr1***VJQ11Ap4***Mo7xmFg")
                .templateId("2001")
                .templateName("message")
                .isSimple(true)
                .build();
        SupplierFactory.setUniConfig(build);
        SmsResponse smsResponse = SmsFactory.createSmsBlend(SupplierType.UNI_SMS).sendMessage("175***65952", "123123");
        System.out.println(smsResponse);
    }
}
