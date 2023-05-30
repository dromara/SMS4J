package org.dromara.sms4j.provider.enumerate;

import org.dromara.sms4j.aliyun.config.AlibabaFactory;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.cloopen.config.CloopenFactory;
import org.dromara.sms4j.ctyun.config.CtyunFactory;
import org.dromara.sms4j.emay.config.EmayFactory;
import org.dromara.sms4j.huawei.config.HuaweiFactory;
import org.dromara.sms4j.jdcloud.config.JdCloudFactory;
import org.dromara.sms4j.netease.config.NeteaseFactory;
import org.dromara.sms4j.provider.base.BaseProviderFactory;
import org.dromara.sms4j.tencent.config.TencentFactory;
import org.dromara.sms4j.unisms.config.UniFactory;
import org.dromara.sms4j.yunpian.config.YunPianFactory;

/**
 * SupplierType
 * <p> 短信供应商枚举
 * @author :Wind
 * 2023/4/7  20:55
 **/
public enum SupplierType {
    /** 阿里云*/
    ALIBABA("阿里云短信", AlibabaFactory.instance()),
    /** 华为云*/
    HUAWEI("华为云短信", HuaweiFactory.instance()),
    /** 云片*/
    YUNPIAN("云片短信", YunPianFactory.instance()),
    /** 腾讯云*/
    TENCENT("腾讯云短信", TencentFactory.instance()),
    /** 合一短信*/
    UNI_SMS("合一短信", UniFactory.instance()),
    /** 京东云 */
    JD_CLOUD("京东云短信", JdCloudFactory.instance()),
    /** 容联云 */
    CLOOPEN("容联云短信", CloopenFactory.instance()),
    /** 亿美软通*/
    EMAY("亿美软通", EmayFactory.instance()),
    /** 天翼云 */
    CTYUN("天翼云短信", CtyunFactory.instance()),
    /** 网易云信 */
    NETEASE("网易云短信", NeteaseFactory.instance())
    ;


    /**
     * 渠道名称
     */
    private final String name;
    /**
     * 短信对象配置
     */
    private final BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> providerFactory;


    SupplierType(String name, BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> providerFactory) {
        this.name = name;
        this.providerFactory = providerFactory;
    }

    public String getName() {
        return name;
    }

    public BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> getProviderFactory() {
        return providerFactory;
    }
}
