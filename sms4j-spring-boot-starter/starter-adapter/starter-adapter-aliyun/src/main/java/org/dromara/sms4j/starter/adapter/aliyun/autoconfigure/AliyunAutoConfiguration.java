package org.dromara.sms4j.starter.adapter.aliyun.autoconfigure;

import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.aliyun.service.AliyunSmsAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href = "mailto:kamtohung@gmail.com">KamTo Hung</a>
 */
@Configuration
public class AliyunAutoConfiguration {

    @Bean
    public AliyunSmsAdapter aliyunSmsAdapter(){
        return new AliyunSmsAdapter(AlibabaConfig.builder().build());
    }

}
