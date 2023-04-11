package org.dromara.sms.autoimmit.config;

import kim.wind.emay.config.EmayConfig;
import org.dromara.sms.aliyun.config.AlibabaConfig;
import org.dromara.sms.cloopen.config.CloopenConfig;
import org.dromara.sms.core.config.SupplierFactory;
import org.dromara.sms.huawei.config.HuaweiConfig;
import org.dromara.sms.jdcloud.config.JdCloudConfig;
import org.dromara.sms.tencent.config.TencentConfig;
import org.dromara.sms.unisms.config.UniConfig;
import org.dromara.sms.yunpian.config.YunpianConfig;
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

    /** 京东云短信差异化配置 */
    @Bean
    @ConfigurationProperties(prefix = "sms.jdcloud")
    protected JdCloudConfig jdCloudConfig(){
        return SupplierFactory.getJdCloudConfig();
    }

    /** 容联云短信差异化配置 */
    @Bean
    @ConfigurationProperties(prefix = "sms.cloopen")
    protected CloopenConfig cloopenConfig(){
        return SupplierFactory.getCloopenConfig();
    }

    /**
     * 亿美软通短信差异化配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.emay")
    protected EmayConfig emayConfig(){
        return SupplierFactory.getEmayConfig();
    }

    /** 为的是延后执行*/
    protected void init(){

    }
}
