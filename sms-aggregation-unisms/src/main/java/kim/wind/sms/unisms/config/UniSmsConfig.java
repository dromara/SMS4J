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
@ConfigurationProperties(prefix = "sms.uni-sms")     //指定配置文件注入属性前缀
@ConditionalOnProperty(name = "sms.supplier", havingValue = "uniSms")
public class UniSmsConfig {

    /** 访问键标识*/
    private String accessKeyId;
    /** 访问键秘钥 简易模式不需要配置*/
    private String accessKeySecret;
    /** 是否为简易模式*/
    private String isSimple = "true";
    /** 短信签名*/
    private String signature;
    /** 模板Id*/
    private String templateId;
    /** 模板变量名称*/
    private String templateName;

    /** 自动注入短信配置*/
    @Bean
    public void buildSms(){
        if ("true".equals(isSimple)){
            Uni.init(accessKeyId);
        }else {
            Uni.init(accessKeyId,accessKeySecret);
        }
    }

    @Bean
    public SmsBlend smsBlend(){
        return new UniSmsImpl();
    }
}
