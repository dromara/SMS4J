package kim.wind.sms.autoimmit.config;

import kim.wind.sms.aliyun.config.AlibabaConfig;
import kim.wind.sms.core.config.SupplierFactory;
import kim.wind.sms.huawei.config.HuaweiConfig;
import kim.wind.sms.tencent.config.TencentConfig;
import kim.wind.sms.unisms.config.UniConfig;
import kim.wind.sms.yunpian.config.YunpianConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

public class SupplierConfig {
    /** 阿里差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.alibaba")
    protected AlibabaConfig alibabaConfig(){
        return SupplierFactory.getAlibabaConfig();
    }

    /** 华为差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.huawei")
    protected HuaweiConfig huaweiConfig(){
        return SupplierFactory.getHuaweiConfig();
    }

    /** 云片短信差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.yunpian")
    protected YunpianConfig yunpianConfig(){
        return SupplierFactory.getYunpianConfig();
    }

    /** 合一短信差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.uni")
    protected UniConfig uniConfig(){
        return SupplierFactory.getUniConfig();
    }

    /** 腾讯短信差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.tencent")
    protected TencentConfig tencentConfig(){
        return SupplierFactory.getTencentConfig();
    }
}
