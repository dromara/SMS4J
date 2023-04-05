package kim.wind.sms.sql.config;


import kim.wind.sms.autoimmit.config.SmsConfig;
import kim.wind.sms.comm.utils.SpringUtil;
import kim.wind.sms.sql.utils.JDBCTool;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;


public class SmsAutoConfig {

    private SpringUtil springUtil;

    private SmsConfig smsConfig;


    @Bean
    @ConfigurationProperties(prefix = "sms.database")     //指定配置文件注入属性前缀
    public SmsSqlConfig smsSqlConfig() {
        return new SmsSqlConfig();
    }

    @Bean
    @ConditionalOnBean(SmsSqlConfig.class)
    public JDBCTool jdbcTool(SmsSqlConfig smsSqlConfig) {
        return new JDBCTool(smsSqlConfig);
    }


}
