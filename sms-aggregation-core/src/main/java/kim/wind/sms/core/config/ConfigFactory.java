package kim.wind.sms.core.config;

import kim.wind.sms.aliyun.config.AlibabaConfig;
import kim.wind.sms.huawei.config.HuaweiConfig;
import kim.wind.sms.tencent.config.TencentConfig;
import kim.wind.sms.unisms.config.UniConfig;
import kim.wind.sms.yunpian.config.YunpianConfig;

/**
 * ConfigFactory
 * <p>差异化配置工厂用于获取差异化配置对象
 * @author :Wind
 * 2023/4/8  14:20
 **/
public class ConfigFactory {
    private ConfigFactory(){}

    /** 阿里配置*/
    private static AlibabaConfig alibabaConfig;

    /** 华为配置*/
    private static HuaweiConfig huaweiConfig;

    /** uniSms配置*/
    private static UniConfig uniConfig;

    /** 云片配置*/
    private static YunpianConfig yunpianConfig;

    /** 腾讯配置*/
    private static TencentConfig tencentConfig;

    /** 获取阿里云配置*/
    public static AlibabaConfig getAlibabaConfig() {
        if (alibabaConfig == null){
            alibabaConfig = new AlibabaConfig();
        }
        return alibabaConfig;
    }

    /** 获取华为云配置*/
    public static HuaweiConfig getHuaweiConfig() {
        if (huaweiConfig == null){
            huaweiConfig = new HuaweiConfig();
        }
        return huaweiConfig;
    }

    /** uniSms配置*/
    public static UniConfig getUniConfig() {
        if (uniConfig == null){
            uniConfig = new UniConfig();
        }
        return uniConfig;
    }

    /** 云片配置*/
    public static YunpianConfig getYunpianConfig() {
        if (yunpianConfig == null){
            yunpianConfig = new YunpianConfig();
        }
        return yunpianConfig;
    }

    /** 腾讯云配置*/
    public static TencentConfig getTencentConfig() {
        if (tencentConfig == null){
            tencentConfig = new TencentConfig();
        }
        return tencentConfig;
    }
}
