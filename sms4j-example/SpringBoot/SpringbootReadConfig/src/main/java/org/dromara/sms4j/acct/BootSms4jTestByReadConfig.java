package org.dromara.sms4j.acct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.dromara.sms4j.source"})
public class BootSms4jTestByReadConfig {

    public static void main(String[] args) {
        SpringApplication.run(BootSms4jTestByReadConfig.class, args);

    }

}
