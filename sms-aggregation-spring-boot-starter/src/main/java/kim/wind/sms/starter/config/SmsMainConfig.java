package kim.wind.sms.starter.config;

import kim.wind.sms.autoimmit.config.SmsAutowiredConfig;
import kim.wind.sms.comm.utils.SpringUtil;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@Data
public class SmsMainConfig {

    @Bean
    public SpringUtil springUtil(){
        return new SpringUtil();
    }

    /** 主要配置注入*/
    @Bean
    public SmsAutowiredConfig smsAutowiredConfig(){
        return new SmsAutowiredConfig();
    }

}
