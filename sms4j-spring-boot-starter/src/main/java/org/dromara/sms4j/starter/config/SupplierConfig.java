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

    /**
     * 天翼云短信差异化配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.ctyun")
    protected CtyunConfig ctyunConfig(){
        return SupplierFactory.getCtyunConfig();
    }


    /**
     * 网易云信差异化配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.netease")
    protected NeteaseConfig neteaseConfig(){
        return SupplierFactory.getNeteaseConfig();
    }

    /**
     * 助通信差异化配置
     */
    @Bean
    @ConfigurationProperties(prefix = "sms.zhutong")
    protected ZhutongConfig zhutongConfig(){
        return SupplierFactory.getZhutongConfig();
    }
}
