package org.dromara.sms4j.starter.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.dingzhong.config.DingZhongConfig;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.lianlu.config.LianLuConfig;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.qiniu.config.QiNiuConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
import org.dromara.sms4j.zhutong.config.ZhutongConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 此写法主要解决:由于定义抽象的ConfigMap定义,Spring boot 配置文件无法给予友好提示的问题
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties("sms.blends")
public class MultiAccountConfig {

    AbstractMultiConfig<AlibabaConfig> alibaba;
    AbstractMultiConfig<CloopenConfig> cloopen;
    AbstractMultiConfig<CtyunConfig> ctyun;
    AbstractMultiConfig<DingZhongConfig> dingzhong;
    AbstractMultiConfig<EmayConfig> emay;
    AbstractMultiConfig<HuaweiConfig> huawei;
    AbstractMultiConfig<JdCloudConfig> jdcloud;
    AbstractMultiConfig<LianLuConfig> lianlu;
    AbstractMultiConfig<NeteaseConfig> netease;
    AbstractMultiConfig<QiNiuConfig> qiniu;
    AbstractMultiConfig<TencentConfig> tencent;
    AbstractMultiConfig<UniConfig> unisms;
    AbstractMultiConfig<YunpianConfig> yunpian;
    AbstractMultiConfig<ZhutongConfig> zhutong;
}
