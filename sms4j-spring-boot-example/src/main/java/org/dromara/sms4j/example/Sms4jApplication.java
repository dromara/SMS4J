package org.dromara.sms4j.example;

import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 主类
 *
 * @author handy
 */
@SpringBootApplication
public class Sms4jApplication {

    public static void main(String[] args) {
        SpringApplication.run(Sms4jApplication.class, args);
    }

}
