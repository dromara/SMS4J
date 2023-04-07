package kim.wind.sms.aliyun.config;

import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.teaopenapi.models.Config;
import kim.wind.sms.aliyun.service.AlibabaSmsImpl;
import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.comm.delayedTime.DelayedTime;
import lombok.Data;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executor;


@Data
@Configuration
@ConditionalOnProperty(name = "sms.supplier", havingValue = "alibaba")
public class AlibabaSmsConfig {


    @Bean
    @ConfigurationProperties(prefix = "sms.alibaba")     //指定配置文件注入属性前缀
    public AlibabaConfig alibabaConfig(){
        return new AlibabaConfig();
    }


    @Bean
    public Client client(AlibabaConfig alibabaConfig) throws Exception {
       Config config = new Config()
                //  AccessKey ID
                .setAccessKeyId(alibabaConfig.getAccessKeyId())
                //  AccessKey Secret
                .setAccessKeySecret(alibabaConfig.getAccessKeySecret());
        // 访问的域名
        config.endpoint = alibabaConfig.getRequestUrl();
        return new Client(config);
    }

    @Bean
    public SmsBlend smsBlend(Client client, AlibabaConfig alibabaConfig){
        return new AlibabaSmsImpl(client,alibabaConfig);
    }
}
