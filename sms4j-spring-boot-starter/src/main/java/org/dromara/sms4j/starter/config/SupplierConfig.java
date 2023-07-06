package org.dromara.sms4j.starter.config;

import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.core.config.SupplierFactory;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
import org.dromara.sms4j.zhutong.config.ZhutongConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import javax.annotation.PostConstruct;

public class SupplierConfig {


    /** 阿里差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.alibaba")
    @ConditionalOnProperty(prefix = "sms", name = "alibaba")
    protected AlibabaConfig alibabaConfig(){
        return SupplierFactory.getAlibabaConfig();
    }

    /** 华为差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.huawei")
    @ConditionalOnProperty(prefix = "sms", name = "huawei")
    protected HuaweiConfig huaweiConfig(){
        return SupplierFactory.getHuaweiConfig();
    }

    /** 云片短信差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.yunpian")
    @ConditionalOnProperty(prefix = "sms", name = "yunpian")
    protected YunpianConfig yunpianConfig(){
        return SupplierFactory.getYunpianConfig();
    }

    /** 合一短信差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.uni")
    @ConditionalOnProperty(prefix = "sms", name = "uni")
    protected UniConfig uniConfig(){
        return SupplierFactory.getUniConfig();
    }

    /** 腾讯短信差异化配置*/
    @Bean
    @ConfigurationProperties(prefix = "sms.tencent")
    @ConditionalOnProperty(prefix = "sms", name = "tencent")
    protected TencentConfig tencentConfig(){
        return SupplierFactory.getTencentConfig();
    }

    /** 京东云短信差异化配置 */
    @Bean
    @ConfigurationProperties(prefix = "sms.jdcloud")
    @ConditionalOnProperty(prefix = "sms", name = "jdcloud")
    protected JdCloudConfig jdCloudConfig(){
        return SupplierFactory.getJdCloudConfig();
    }

    /** 容联云短信差异化配置 */
    @Bean
    @ConfigurationProperties(prefix = "sms.cloopen")
    @ConditionalOnProperty(prefix = "sms", name = "cloopen")
    protected CloopenConfig cloopenConfig(){
        return SupplierFactory.getCloopenConfig();
    }

    /**
     * 亿美软通短信差异化配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.emay")
    @ConditionalOnProperty(prefix = "sms", name = "emay")
    protected EmayConfig emayConfig(){
        return SupplierFactory.getEmayConfig();
    }

    /**
     * 天翼云短信差异化配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.ctyun")
    @ConditionalOnProperty(prefix = "sms", name = "ctyun")
    protected CtyunConfig ctyunConfig(){
        return SupplierFactory.getCtyunConfig();
    }


    /**
     * 网易云信差异化配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.netease")
    @ConditionalOnProperty(prefix = "sms", name = "netease")
    protected NeteaseConfig neteaseConfig(){
        return SupplierFactory.getNeteaseConfig();
    }

    /**
     * 助通信差异化配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.zhutong")
    @ConditionalOnProperty(prefix = "sms", name = "zhutong")
    protected ZhutongConfig zhutongConfig(){
        return SupplierFactory.getZhutongConfig();
    }

    @PostConstruct
    protected void init(){

    }
}
