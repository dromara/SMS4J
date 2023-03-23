package kim.wind.sms.starter.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "sms")     //指定配置文件注入属性前缀
@Data
public class SmsMainConfig {
    /** 短信服务商*/
    @Value("${sms.supplier}")
    private String supplier;
    /** 是否开启短信限制*/
    private String restricted;
    /** 单账号每日最大发送量*/
    private Integer accountMax;
    /** 单账号每分钟最大发送*/
    private Integer minuteMax;
}
