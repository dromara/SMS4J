package org.dromara.sms4j.core.config;

import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;

/**
 * SupplierFactory
 * <p> 差异化配置工厂
 * @author :Wind
 * 2023/4/8  15:02
 **/
public class SupplierFactory {
    private SupplierFactory() {
    }

    /** 阿里云差异化配置*/
    private static AlibabaConfig alibabaConfig;

    /** 华为云差异化配置*/
    private static HuaweiConfig huaweiConfig;

    /** 合一短信差异化配置*/
    private static UniConfig uniConfig;

    /** 腾讯云短信差异化配置*/
    private static TencentConfig tencentConfig;

    /** 云片短信差异配置*/
    private static YunpianConfig yunpianConfig;

    /** 京东云短信差异配置 */
    private static JdCloudConfig jdCloudConfig;

    /** 容联云短信差异配置 */
    private static CloopenConfig cloopenConfig;

    /**
     * 亿美软通短信差异配置
     */
    private static EmayConfig emayConfig;

    /** 阿里云配置获取*/
    public static AlibabaConfig getAlibabaConfig() {
        if (alibabaConfig == null){
            alibabaConfig = AlibabaConfig.builder().build();
        }
        return alibabaConfig;
    }

    /** 华为云配置获取*/
    public static HuaweiConfig getHuaweiConfig() {
        if (huaweiConfig == null){
            huaweiConfig = HuaweiConfig.builder().build();
        }
        return huaweiConfig;
    }

    /** 合一短信配置获取*/
    public static UniConfig getUniConfig() {
        if (uniConfig == null){
            uniConfig = UniConfig.builder().build();
        }
        return uniConfig;
    }

    /** 腾讯短信配置获取*/
    public static TencentConfig getTencentConfig() {
        if (tencentConfig == null){
            tencentConfig = TencentConfig.builder().build();
        }
        return tencentConfig;
    }

    /** 云片短信配置获取*/
    public static YunpianConfig getYunpianConfig() {
        if (yunpianConfig == null){
            yunpianConfig = YunpianConfig.builder().build();
        }
        return yunpianConfig;
    }

    /** 京东云短信配置获取 */
    public static JdCloudConfig getJdCloudConfig() {
        if (jdCloudConfig == null){
            jdCloudConfig = JdCloudConfig.builder().build();
        }
        return jdCloudConfig;
    }

    /** 容联云短信配置获取 */
    public static CloopenConfig getCloopenConfig() {
        if (cloopenConfig == null){
            cloopenConfig = CloopenConfig.builder().build();
        }
        return cloopenConfig;
    }

    /**
     * 亿美软通配置获取
     */
    public static EmayConfig getEmayConfig() {
        if (emayConfig == null) {
            emayConfig = EmayConfig.builder().build();
        }
        return emayConfig;
    }

    /**
     * 设置 alibabaConfig
     */
    public static void setAlibabaConfig(AlibabaConfig alibabaConfig) {
        SupplierFactory.alibabaConfig = alibabaConfig;
        SmsFactory.refresh(SupplierType.ALIBABA);
    }

    /**
     * 设置 huaweiConfig
     */
    public static void setHuaweiConfig(HuaweiConfig huaweiConfig) {
        SupplierFactory.huaweiConfig = huaweiConfig;
        SmsFactory.refresh(SupplierType.HUAWEI);
    }

    /**
     * 设置 uniConfig
     */
    public static void setUniConfig(UniConfig uniConfig) {
        SupplierFactory.uniConfig = uniConfig;
        SmsFactory.refresh(SupplierType.UNI_SMS);
    }

    /**
     * 设置 tencentConfig
     */
    public static void setTencentConfig(TencentConfig tencentConfig) {
        SupplierFactory.tencentConfig = tencentConfig;
        SmsFactory.refresh(SupplierType.TENCENT);
    }

    /**
     * 设置 yunpianConfig
     */
    public static void setYunpianConfig(YunpianConfig yunpianConfig) {
        SupplierFactory.yunpianConfig = yunpianConfig;
        SmsFactory.refresh(SupplierType.YUNPIAN);
    }

    /**
     * 设置 jdCloudConfig
     */
    public static void setJdCloudConfig(JdCloudConfig jdCloudConfig) {
        SupplierFactory.jdCloudConfig = jdCloudConfig;
        SmsFactory.refresh(SupplierType.JD_CLOUD);
    }

    /**
     * 设置 cloopenConfig
     */
    public static void setCloopenConfig(CloopenConfig cloopenConfig) {
        SupplierFactory.cloopenConfig = cloopenConfig;
        SmsFactory.refresh(SupplierType.CLOOPEN);
    }

    /**
     * 设置 emayConfig
     */
    public static void setEmayConfig(EmayConfig emayConfig) {
        SupplierFactory.emayConfig = emayConfig;
        SmsFactory.refresh(SupplierType.EMAY);
    }
}
