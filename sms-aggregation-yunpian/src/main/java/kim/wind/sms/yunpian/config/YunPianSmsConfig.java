package kim.wind.sms.yunpian.config;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.yunpian.service.YunPianSmsImpl;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(name = "sms.supplier", havingValue = "yunpian")
public class YunPianSmsConfig {

    @Bean
    @ConfigurationProperties(prefix = "sms.yunpian")     //指定配置文件注入属性前缀
    public YunpianConfig yunpianConfig(){
        return new YunpianConfig();
    }

    @Bean
    public ForestConfiguration forestConfiguration(YunpianConfig yunpianConfig) {
        return Forest.config().setBackendName("httpclient").setLogEnabled(yunpianConfig.getHttpLog());
    }

    @Bean
    public SmsBlend smsBlend() {
        return new YunPianSmsImpl();
    }
}
