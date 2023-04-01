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
@ConfigurationProperties(prefix = "sms.huawei")     //指定配置文件注入属性前缀
@ConditionalOnProperty(name = "sms.supplier", havingValue = "huawei")
public class HuaweiSmsConfig {

    /** appKey*/
    private String appKey ;
    /** appSecret */
    private String appSecret ;
    /** 短信签名*/
    private String signature;
    /** 国内短信签名通道号*/
    private String sender;
    /** 模板Id*/
    private String templateId;
    /** 短信状态报告接收地*/
    private String statusCallBack;
    /** APP接入地址*/
    private String url;
    /** 是否打印http请求日志*/
    private Boolean httpLog = false;

    @Bean("forestConfiguration")
    public ForestConfiguration forestConfiguration(){
        return Forest.config().setBackendName("httpclient").setLogEnabled(httpLog);
    }

    @Bean
    public SmsBlend smsBlend (){
        return new HuaweiSmsImpl();
    }

}
