package kim.wind.sms.starter.config;

import kim.wind.sms.autoimmit.config.SmsAutowiredConfig;
import kim.wind.sms.core.utils.SpringUtil;
import lombok.Data;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@Data
public class SmsMainConfig {

    @Bean
    public SpringUtil springUtil(DefaultListableBeanFactory defaultListableBeanFactory){
        return new SpringUtil(defaultListableBeanFactory);
    }

    /** 主要配置注入 确保springUtil注入后再注入*/
    @Bean(initMethod = "init")
    public SmsAutowiredConfig smsAutowiredConfig(SpringUtil springUtil){
        return new SmsAutowiredConfig(springUtil);
    }

}
