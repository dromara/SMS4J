package org.dromara.sms4j.solon.config;

import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
import org.noear.solon.Solon;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Bean;
import org.noear.solon.annotation.Configuration;
import org.noear.solon.core.Props;

@Configuration
public class SupplierConfig {
    private <T> T injectObj(String prefix, T obj) {
        //@Inject 只支持在字段、参数、类型上注入
        Props props = Solon.cfg().getProp(prefix);
        Utils.injectProperties(obj, props);
        return obj;
    }


    /** 阿里差异化配置*/
    @Bean
    public AlibabaConfig alibabaConfig(){
        return injectObj("sms.alibaba", new AlibabaConfig());
    }

    /** 华为差异化配置*/
    @Bean
    public HuaweiConfig huaweiConfig(){
        return injectObj("sms.huawei", new HuaweiConfig());
    }

    /** 云片短信差异化配置*/
    @Bean
    public YunpianConfig yunpianConfig(){
        return injectObj("sms.yunpian", new YunpianConfig());
    }

    /** 合一短信差异化配置*/
    @Bean
    public UniConfig uniConfig(){
        return injectObj("sms.uni", new UniConfig());
    }

    /** 腾讯短信差异化配置*/
    @Bean
    public TencentConfig tencentConfig(){
        return injectObj("sms.tencent", new TencentConfig());
    }

    /** 京东云短信差异化配置 */
    @Bean
    public JdCloudConfig jdCloudConfig(){
        return injectObj("sms.jdcloud", new JdCloudConfig());
    }

    /** 容联云短信差异化配置 */
    @Bean
    public CloopenConfig cloopenConfig(){
        return injectObj("sms.cloopen", new CloopenConfig());
    }

    /**
     * 亿美软通短信差异化配置
     */
    @Bean
    public EmayConfig emayConfig(){
        return injectObj("sms.emay", new EmayConfig());
    }

    /** 天翼云短信差异化配置 */
    @Bean
    public CtyunConfig ctyunConfig(){
        return injectObj("sms.ctyun", new CtyunConfig());
    }
}
