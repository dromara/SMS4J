package org.dromara.sms.core.config;

import org.dromara.sms.aliyun.config.AlibabaConfig;
import org.dromara.sms.huawei.config.HuaweiConfig;
import org.dromara.sms.jdcloud.config.JdCloudConfig;
import org.dromara.sms.tencent.config.TencentConfig;
import org.dromara.sms.unisms.config.UniConfig;
import org.dromara.sms.yunpian.config.YunpianConfig;

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

    /** 阿里云配置获取*/
    public static AlibabaConfig getAlibabaConfig() {
        if (alibabaConfig == null){
            alibabaConfig = new AlibabaConfig();
        }
        return alibabaConfig;
    }

    /** 华为云配置获取*/
    public static HuaweiConfig getHuaweiConfig() {
        if (huaweiConfig == null){
            huaweiConfig = new HuaweiConfig();
        }
        return huaweiConfig;
    }

    /** 合一短信配置获取*/
    public static UniConfig getUniConfig() {
        if (uniConfig == null){
            uniConfig = new UniConfig();
        }
        return uniConfig;
    }

    /** 腾讯短信配置获取*/
    public static TencentConfig getTencentConfig() {
        if (tencentConfig == null){
            tencentConfig = new TencentConfig();
        }
        return tencentConfig;
    }

    /** 云片短信配置获取*/
    public static YunpianConfig getYunpianConfig() {
        if (yunpianConfig == null){
            yunpianConfig = new YunpianConfig();
        }
        return yunpianConfig;
    }

    /** 京东云短信配置获取 */
    public static JdCloudConfig getJdCloudConfig() {
        if (jdCloudConfig == null){
            jdCloudConfig = new JdCloudConfig();
        }
        return jdCloudConfig;
    }
}
