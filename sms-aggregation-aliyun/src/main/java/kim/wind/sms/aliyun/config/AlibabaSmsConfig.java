package kim.wind.sms.aliyun.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import kim.wind.sms.aliyun.service.AlibabaSmsImpl;
import kim.wind.sms.api.SmsBlend;
import lombok.Data;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms.alibaba")     //指定配置文件注入属性前缀
@Data
@ConditionalOnProperty(prefix = "sms", name = "supplier", havingValue = "alibaba")
public class AlibabaSmsConfig {

    private String accessKeyId;
    /** 访问键秘钥 */
    private String accessKeySecret;
    /** 短信签名*/
    private String signature;
    /** 模板Id*/
    private String templateId;
    /** 模板变量名称*/
    private String templateName;

    private String requestUrl = "dysmsapi.aliyuncs.com";


    @Bean
    public Client config() throws Exception {
       Config config = new Config()
                //  AccessKey ID
                .setAccessKeyId(accessKeyId)
                //  AccessKey Secret
                .setAccessKeySecret(accessKeySecret);
        // 访问的域名
        config.endpoint = requestUrl;
        return new Client(config);
    }

//    @Bean
//    public SmsBlend smsBlend(){
//        return new AlibabaSmsImpl();
//    }
}
