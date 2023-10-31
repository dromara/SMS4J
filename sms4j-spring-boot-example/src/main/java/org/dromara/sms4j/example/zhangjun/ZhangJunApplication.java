package org.dromara.sms4j.example.zhangjun;

import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 自定义广州掌骏短信实现
 *
 * @author 4n
 */
@SpringBootApplication
public class ZhangJunApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhangJunApplication.class, args);
        SmsFactory.getBySupplier("zhangjun").sendMessage("17*****598", "154468");
    }

}
