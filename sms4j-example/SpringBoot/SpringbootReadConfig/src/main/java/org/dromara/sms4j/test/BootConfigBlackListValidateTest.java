package org.dromara.sms4j.test;

import org.dromara.sms4j.test.validate.BlackListValidate;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BootConfigBlackListValidateTest {

    public static void main(String[] args) {
        SpringApplication.run(BootConfigBlackListValidateTest.class, args);

        BlackListValidate blackListValidate = new BlackListValidate("a","11111111111");
        blackListValidate.test();
    }
}
