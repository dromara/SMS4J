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
@ConfigurationProperties(prefix = "sms.yunpian")     //指定配置文件注入属性前缀
@ConditionalOnProperty(name = "sms.supplier", havingValue = "yunpian")
public class YunPianSmsConfig {
    /**
     * 账号唯一标识
     */
    private String apikey;

    /**
     * 短信发送后将向这个地址推送(运营商返回的)发送报告
     */
    private String callbackUrl;

    /**
     * 模板Id
     */
    private String templateId;

    /**
     * 模板变量名称
     */
    private String templateName;

    /**
     * 是否打印http请求日志
     */
    private Boolean httpLog = false;

    @Bean
    public ForestConfiguration forestConfiguration() {
        return Forest.config().setBackendName("httpclient").setLogEnabled(httpLog);
    }

    @Bean
    public SmsBlend smsBlend() {
        return new YunPianSmsImpl();
    }
}
