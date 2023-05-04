package org.dromara.sms4j.starter.adapter.aliyun.autoconfigure;

import org.dromara.sms4j.aliyun.service.AliyunSmsAdapter;
import org.dromara.sms4j.autoimmit.config.SmsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href = "mailto:kamtohung@gmail.com">KamTo Hung</a>
 */
@Configuration
public class AliyunAutoConfiguration {

    @Bean
    public AliyunSmsAdapter aliyunSmsAdapter(SmsProperties smsProperties){
        // TODO
        return new AliyunSmsAdapter(smsProperties.getAlibaba());
    }

}
