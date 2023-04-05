package kim.wind.sms.unisms.config;

import com.apistd.uni.Uni;
import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.unisms.service.UniSmsImpl;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(name = "sms.supplier", havingValue = "uniSms")
public class UniSmsConfig {

    @Bean
    @ConfigurationProperties(prefix = "sms.uni-sms")     //指定配置文件注入属性前缀
    public UniConfig uniConfig(){
    return new UniConfig();
    }

    /** 自动注入短信配置*/
    @Bean
    public void buildSms(UniConfig uniConfig){
        if ("true".equals(uniConfig.getIsSimple())){
            Uni.init(uniConfig.getAccessKeyId());
        }else {
            Uni.init(uniConfig.getAccessKeyId(),uniConfig.getAccessKeySecret());
        }
    }

    @Bean
    public SmsBlend smsBlend(){
        return new UniSmsImpl();
    }
}
