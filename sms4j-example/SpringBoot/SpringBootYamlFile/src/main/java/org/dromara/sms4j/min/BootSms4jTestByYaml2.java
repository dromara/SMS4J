package org.dromara.sms4j.min;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"org.dromara.sms4j.source"})
public class BootSms4jTestByYaml2 {

    public static void main(String[] args) {
        SpringApplication.run(BootSms4jTestByYaml2.class, args);

    }

}
