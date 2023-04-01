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
@ConfigurationProperties(prefix = "sms.tencent")     //指定配置文件注入属性前缀
@ConditionalOnProperty(name = "sms.supplier", havingValue = "tencent")
public class TencentSmsConfig {

    /** 应用accessKey*/
    private String accessKeyId;
    /**
     * 访问键秘钥
     */
    private String accessKeySecret;
    /**
     * 短信签名
     */
    private String signature;
    /**
     * 模板Id
     */
    private String templateId;
    /** 短信sdkAppId*/
    private String sdkAppId;
    /** 地域信息默认为 ap-guangzhou*/
    private String territory ="ap-guangzhou";
    /**请求超时时间 */
    private Integer connTimeout = 60;

    @Bean
    public SmsClient tencentBean() {
        Credential cred = new Credential(accessKeyId, accessKeySecret);
        HttpProfile httpProfile = new HttpProfile();
        httpProfile.setReqMethod("POST");
        httpProfile.setConnTimeout(connTimeout);
        httpProfile.setEndpoint("sms.tencentcloudapi.com");
        ClientProfile clientProfile = new ClientProfile();
        clientProfile.setSignMethod("HmacSHA256");
        clientProfile.setHttpProfile(httpProfile);
        return new SmsClient(cred, territory,clientProfile);
    }

    @Bean
    public SmsBlend smsBlend(){
        return new TencentSmsImpl();
    }
}
