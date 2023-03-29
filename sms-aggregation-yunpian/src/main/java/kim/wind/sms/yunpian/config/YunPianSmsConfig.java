package kim.wind.sms.yunpian.config;

import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms.yunpian")     //指定配置文件注入属性前缀
@Data
@ConditionalOnProperty(prefix = "sms", name = "supplier", havingValue = "yunpian")
public class YunPianSmsConfig {
    /** 账号唯一标识*/
    private String apikey;
}
