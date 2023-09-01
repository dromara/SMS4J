package org.dromara.sms4j.starter.config;

import org.dromara.sms4j.aliyun.config.AlibabaFactory;
import org.dromara.sms4j.cloopen.config.CloopenFactory;
import org.dromara.sms4j.ctyun.config.CtyunFactory;
import org.dromara.sms4j.emay.config.EmayFactory;
import org.dromara.sms4j.huawei.config.HuaweiFactory;
import org.dromara.sms4j.jdcloud.config.JdCloudFactory;
import org.dromara.sms4j.netease.config.NeteaseFactory;
import org.dromara.sms4j.tencent.config.TencentFactory;
import org.dromara.sms4j.unisms.config.UniFactory;
import org.dromara.sms4j.yunpian.config.YunPianFactory;
import org.dromara.sms4j.zhutong.config.ZhutongFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
public class SmsProviderFactoryConfig {

    @Bean
    @Lazy
    public AlibabaFactory smsAlibabaFactory() {
        return AlibabaFactory.instance();
    }

    @Bean
    @Lazy
    public CloopenFactory smsCloopenFactory() {
        return CloopenFactory.instance();
    }

    @Bean
    @Lazy
    public CtyunFactory smsCtyunFactory() {
        return CtyunFactory.instance();
    }

    @Bean
    @Lazy
    public EmayFactory smsEmayFactory() {
        return EmayFactory.instance();
    }

    @Bean
    @Lazy
    public HuaweiFactory smsHuaweiFactory() {
        return HuaweiFactory.instance();
    }

    @Bean
    @Lazy
    public JdCloudFactory smsJdCloudFactory() {
        return JdCloudFactory.instance();
    }

    @Bean
    @Lazy
    public NeteaseFactory smsNeteaseFactory() {
        return NeteaseFactory.instance();
    }

    @Bean
    @Lazy
    public TencentFactory smsTencentFactory() {
        return TencentFactory.instance();
    }

    @Bean
    @Lazy
    public UniFactory smsUniFactory() {
        return UniFactory.instance();
    }

    @Bean
    @Lazy
    public YunPianFactory smsYunPianFactory() {
        return YunPianFactory.instance();
    }

    @Bean
    @Lazy
    public ZhutongFactory smsZhutongFactory() {
        return ZhutongFactory.instance();
    }

}
