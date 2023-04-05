package kim.wind.sms.tencent.config;

import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20210111.SmsClient;
import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.tencent.service.TencentSmsImpl;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConditionalOnProperty(name = "sms.supplier", havingValue = "tencent")
public class TencentSmsConfig {

    @Bean
    @ConfigurationProperties(prefix = "sms.tencent")     //指定配置文件注入属性前缀
   public TencentConfig tencentConfig(){
       return new TencentConfig();
   }

    @Bean
    public SmsClient tencentBean( TencentConfig tencentConfig) {
        Credential cred = new Credential(tencentConfig.getAccessKeyId(),tencentConfig.getAccessKeySecret());
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(tencentConfig.getConnTimeout());
        httpProfile.setEndpoint("sms.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, tencentConfig.getTerritory(),clientProfile);
    }

    @Bean
    public SmsBlend smsBlend(){
        return new TencentSmsImpl();
    }
}
