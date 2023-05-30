package org.dromara.sms4j.core.config;

import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.aliyun.config.AlibabaFactory;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.cloopen.config.CloopenFactory;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.ctyun.config.CtyunConfig;
import org.dromara.sms4j.ctyun.config.CtyunFactory;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.emay.config.EmayFactory;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.huawei.config.HuaweiFactory;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.jdcloud.config.JdCloudFactory;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.netease.config.NeteaseFactory;
import org.dromara.sms4j.provider.enumerate.SupplierType;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.tencent.config.TencentFactory;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.unisms.config.UniFactory;
import org.dromara.sms4j.yunpian.config.YunPianFactory;
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

    /**
     * 阿里云配置获取
     */
    public static AlibabaConfig getAlibabaConfig() {
        return AlibabaFactory.instance().getConfig();
    }

    /**
     * 华为云配置获取
     */
    public static HuaweiConfig getHuaweiConfig() {
        return HuaweiFactory.instance().getConfig();
    }

    /**
     * 合一短信配置获取
     */
    public static UniConfig getUniConfig() {
        return UniFactory.instance().getConfig();
    }

    /**
     * 腾讯短信配置获取
     */
    public static TencentConfig getTencentConfig() {
        return TencentFactory.instance().getConfig();
    }

    /**
     * 云片短信配置获取
     */
    public static YunpianConfig getYunpianConfig() {
        return YunPianFactory.instance().getConfig();
    }

    /**
     * 京东云短信配置获取
     */
    public static JdCloudConfig getJdCloudConfig() {
        return JdCloudFactory.instance().getConfig();
    }

    /**
     * 容联云短信配置获取
     */
    public static CloopenConfig getCloopenConfig() {
        return CloopenFactory.instance().getConfig();
    }

    /**
     * 亿美软通配置获取
     */
    public static EmayConfig getEmayConfig() {
        return EmayFactory.instance().getConfig();
    }

    /**
     * 天翼云配置获取
     */
    public static CtyunConfig getCtyunConfig() {
        return CtyunFactory.instance().getConfig();
    }

    /**
     * 网易云信配置获取
     */
    public static NeteaseConfig getNeteaseConfig() {
        return NeteaseFactory.instance().getConfig();
    }

    /**
     *  setSupplierConfig
     * <p>通用化set，用于设置
     * @param t 配置对象
     * @author :Wind
    */
    public static <T extends SupplierConfig> void setSupplierConfig(T t) {
        if (t instanceof AlibabaConfig) {
            setAlibabaConfig((AlibabaConfig) t);
        } else if (t instanceof HuaweiConfig) {
            setHuaweiConfig((HuaweiConfig) t);
        } else if (t instanceof UniConfig) {
            setUniConfig((UniConfig) t);
        } else if (t instanceof TencentConfig) {
            setTencentConfig((TencentConfig) t);
        } else if (t instanceof YunpianConfig) {
            setYunpianConfig((YunpianConfig) t);
        } else if (t instanceof JdCloudConfig) {
            setJdCloudConfig((JdCloudConfig) t);
        } else if (t instanceof CloopenConfig) {
            setCloopenConfig((CloopenConfig) t);
        } else if (t instanceof EmayConfig) {
            setEmayConfig((EmayConfig) t);
        } else if (t instanceof CtyunConfig) {
            setCtyunConfig((CtyunConfig) t);
        } else if (t instanceof NeteaseConfig) {
            setNeteaseConfig((NeteaseConfig) t);
        } else {
            throw new SmsBlendException("Loading failure! Please check the configuration type.");
        }
    }

    /**
     * 设置 alibabaConfig
     */
    public static void setAlibabaConfig(AlibabaConfig alibabaConfig) {
        AlibabaFactory.instance().setConfig(alibabaConfig);
        SmsFactory.refresh(SupplierType.ALIBABA);
    }

    /**
     * 设置 huaweiConfig
     */
    public static void setHuaweiConfig(HuaweiConfig huaweiConfig) {
        HuaweiFactory.instance().setConfig(huaweiConfig);
        SmsFactory.refresh(SupplierType.HUAWEI);
    }

    /**
     * 设置 uniConfig
     */
    public static void setUniConfig(UniConfig uniConfig) {
        UniFactory.instance().setConfig(uniConfig);
        SmsFactory.refresh(SupplierType.UNI_SMS);
    }

    /**
     * 设置 tencentConfig
     */
    public static void setTencentConfig(TencentConfig tencentConfig) {
        TencentFactory.instance().setConfig(tencentConfig);
        SmsFactory.refresh(SupplierType.TENCENT);
    }

    /**
     * 设置 yunpianConfig
     */
    public static void setYunpianConfig(YunpianConfig yunpianConfig) {
        YunPianFactory.instance().setConfig(yunpianConfig);
        SmsFactory.refresh(SupplierType.YUNPIAN);
    }

    /**
     * 设置 jdCloudConfig
     */
    public static void setJdCloudConfig(JdCloudConfig jdCloudConfig) {
        JdCloudFactory.instance().setConfig(jdCloudConfig);
        SmsFactory.refresh(SupplierType.JD_CLOUD);
    }

    /**
     * 设置 cloopenConfig
     */
    public static void setCloopenConfig(CloopenConfig cloopenConfig) {
        CloopenFactory.instance().setConfig(cloopenConfig);
        SmsFactory.refresh(SupplierType.CLOOPEN);
    }

    /**
     * 设置 emayConfig
     */
    public static void setEmayConfig(EmayConfig emayConfig) {
        EmayFactory.instance().setConfig(emayConfig);
        SmsFactory.refresh(SupplierType.EMAY);
    }

    /**
     * 设置 ctyunConfig
     */
    public static void setCtyunConfig(CtyunConfig ctyunConfig) {
        CtyunFactory.instance().setConfig(ctyunConfig);
        SmsFactory.refresh(SupplierType.CTYUN);
    }

    /**
     * 设置 neteaseConfig
     */
    public static void setNeteaseConfig(NeteaseConfig neteaseConfig) {
        NeteaseFactory.instance().setConfig(neteaseConfig);
        SmsFactory.refresh(SupplierType.CTYUN);
    }
}
