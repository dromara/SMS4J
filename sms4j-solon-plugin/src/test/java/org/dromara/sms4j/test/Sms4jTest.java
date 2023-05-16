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
    public static final String USER_AGENT = "uni-java-sdk" + "/0.0.4" ;

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
    }
}
