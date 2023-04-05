package kim.wind.sms.huawei.config;

import com.dtflys.forest.Forest;
import com.dtflys.forest.config.ForestConfiguration;
import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.huawei.service.HuaweiSmsImpl;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(name = "sms.supplier", havingValue = "huawei")
public class HuaweiSmsConfig {

    @Bean
    @ConfigurationProperties(prefix = "sms.huawei")     //指定配置文件注入属性前缀
    public HuaweiConfig huaweiConfig(){
        return new HuaweiConfig();
    }

    @Bean("forestConfiguration")
    public ForestConfiguration forestConfiguration(HuaweiConfig huaweiConfig){
        return Forest.config().setBackendName("httpclient").setLogEnabled(huaweiConfig.getHttpLog());
    }

    @Bean
    public SmsBlend smsBlend (){
        return new HuaweiSmsImpl();
    }

}
