package org.dromara.sms4j.test.paramvalidate;

import org.dromara.sms4j.test.validate.ParamValidate;
import org.springframework.boot.SpringApplication;

public class BootConfigParamValidateTest {

    public static void main(String[] args) {
        SpringApplication.run(org.dromara.sms4j.test.blacklist.BootConfigBlackListValidateTest.class, args);


        ParamValidate paramValidate = new ParamValidate("a","11111111111");
        paramValidate.test();
    }
}
