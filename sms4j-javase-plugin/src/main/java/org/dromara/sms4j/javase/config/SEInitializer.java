package org.dromara.sms4j.javase.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.comm.config.SmsConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.core.config.SupplierFactory;
import org.dromara.sms4j.emay.config.EmayConfig;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.javase.util.YamlUtil;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.netease.config.NeteaseConfig;
import org.dromara.sms4j.tencent.config.TencentConfig;
import org.dromara.sms4j.unisms.config.UniConfig;
import org.dromara.sms4j.yunpian.config.YunpianConfig;
import org.dromara.sms4j.zhutong.config.ZhutongConfig;

/**
 * 初始化类
 */
@Slf4j
public class SEInitializer {

    private static final SEInitializer INSTANCE = new SEInitializer();

    public static SEInitializer initializer() {
        return INSTANCE;
    }

    /**
     * 初始化短信公共配置
     *
     * @param smsConfig 短信公共配置
     * @return 当前初始化类实例
     */
    public SEInitializer initSmsConfig(SmsConfig smsConfig) {
        BeanUtil.copyProperties(smsConfig, BeanFactory.getSmsConfig());
        return this;
    }

    /**
     * 初始化阿里配置
     *
     * @param alibabaConfig 阿里配置
     * @return 当前初始化类实例
     */
    public SEInitializer initAlibaba(AlibabaConfig alibabaConfig) {
        BeanUtil.copyProperties(alibabaConfig, SupplierFactory.getAlibabaConfig());
        return this;
    }

    /**
     * 初始化容连云配置
     *
     * @param cloopenConfig 容连云配置
     * @return 当前初始化类实例
     */
    public SEInitializer initCloopen(CloopenConfig cloopenConfig) {
        BeanUtil.copyProperties(cloopenConfig, SupplierFactory.getCloopenConfig());
        return this;
    }

    /**
     * 初始化亿美软通配置
     *
     * @param emayConfig 亿美软通配置
     * @return 当前初始化类实例
     */
    public SEInitializer initEmay(EmayConfig emayConfig) {
        BeanUtil.copyProperties(emayConfig, SupplierFactory.getEmayConfig());
        return this;
    }

    /**
     * 初始化华为配置
     *
     * @param huaweiConfig 华为配置
     * @return 当前初始化类实例
     */
    public SEInitializer initHuawei(HuaweiConfig huaweiConfig) {
        BeanUtil.copyProperties(huaweiConfig, SupplierFactory.getHuaweiConfig());
        return this;
    }

    /**
     * 初始化京东配置
     *
     * @param jdCloudConfig 京东配置
     * @return 当前初始化类实例
     */
    public SEInitializer initJdCloud(JdCloudConfig jdCloudConfig) {
        BeanUtil.copyProperties(jdCloudConfig, SupplierFactory.getJdCloudConfig());
        return this;
    }

    /**
     * 初始化腾讯配置
     *
     * @param tencentConfig 腾讯配置
     * @return 当前初始化类实例
     */
    public SEInitializer initTencent(TencentConfig tencentConfig) {
        BeanUtil.copyProperties(tencentConfig, SupplierFactory.getTencentConfig());
        return this;
    }

    /**
     * 初始化合一配置
     *
     * @param uniConfig 合一配置
     * @return 当前初始化类实例
     */
    public SEInitializer initUniSms(UniConfig uniConfig) {
        BeanUtil.copyProperties(uniConfig, SupplierFactory.getUniConfig());
        return this;
    }

    /**
     * 初始化云片配置
     *
     * @param yunpianConfig 云片配置
     * @return 当前初始化类实例
     */
    public SEInitializer initYunpian(YunpianConfig yunpianConfig) {
        BeanUtil.copyProperties(yunpianConfig, SupplierFactory.getYunpianConfig());
        return this;
    }

    /**
     * initializer
     * <p>初始化网易云短信配置
     *
     * @return 当前初始化类实例
     * @author :Wind
     */
    public SEInitializer initNetase(NeteaseConfig neteaseConfig) {
        BeanUtil.copyProperties(neteaseConfig, SupplierFactory.getNeteaseConfig());
        return this;
    }

    /**
     * initZhuTong
     * <p>初始化助通短信配置
     *
     * @return 当前初始化类实例
     * @author :Wind
     */
    public SEInitializer initZhuTong(ZhutongConfig zhutongConfig) {
        BeanUtil.copyProperties(zhutongConfig, SupplierFactory.getZhutongConfig());
        return this;
    }

    /**
     * 默认从sms-aggregation.yml文件中读取配置
     *
     * @return
     */
    public void fromYaml() {
        ClassPathResource yamlResouce = new ClassPathResource("sms4j.yml");
        this.fromYaml(yamlResouce.readUtf8Str());
    }

    /**
     * 从yaml中读取配置
     *
     * @param yaml yaml配置字符串
     */
    public void fromYaml(String yaml) {
        InitConfig config = YamlUtil.toBean(yaml, InitConfig.class);
        this.initConfig(config);
    }

    /**
     * 从json中读取配置
     *
     * @param json json配置字符串
     */
    public void fromJson(String json) {
        InitConfig config = JSONUtil.toBean(json, InitConfig.class);
        this.initConfig(config);
    }

    private void initConfig(InitConfig config) {
        if (config == null) {
            log.error("初始化配置失败");
            throw new SmsBlendException("初始化配置失败");
        }
        InitSmsConfig smsConfig = config.getSms();
        if (smsConfig == null) {
            log.error("初始化配置失败");
            throw new SmsBlendException("初始化配置失败");
        }

        this.initSmsConfig(smsConfig);
        AlibabaConfig alibabaConfig = smsConfig.getAlibaba();
        if (alibabaConfig != null) {
            this.initAlibaba(alibabaConfig);
        }
        CloopenConfig cloopenConfig = smsConfig.getCloopen();
        if (cloopenConfig != null) {
            this.initCloopen(cloopenConfig);
        }
        EmayConfig emayConfig = smsConfig.getEmay();
        if (emayConfig != null) {
            this.initEmay(emayConfig);
        }
        HuaweiConfig huaweiConfig = smsConfig.getHuawei();
        if (huaweiConfig != null) {
            this.initHuawei(huaweiConfig);
        }
        JdCloudConfig jdCloudConfig = smsConfig.getJdCloud();
        if (jdCloudConfig != null) {
            this.initJdCloud(jdCloudConfig);
        }
        TencentConfig tencentConfig = smsConfig.getTencent();
        if (tencentConfig != null) {
            this.initTencent(tencentConfig);
        }
        UniConfig uniConfig = smsConfig.getUni();
        if (uniConfig != null) {
            this.initUniSms(uniConfig);
        }
        YunpianConfig yunpianConfig = smsConfig.getYunpian();
        if (yunpianConfig != null) {
            this.initYunpian(yunpianConfig);
        }
        NeteaseConfig neteaseConfig = smsConfig.getNeteaseConfig();
        if (neteaseConfig != null){
            this.initNetase(neteaseConfig);
        }
        ZhutongConfig zhutongConfig = smsConfig.getZhutongConfig();
        if (zhutongConfig != null){
            this.initZhuTong(zhutongConfig);
        }
    }

    /**
     * 初始化配置bean
     */
    @Data
    @EqualsAndHashCode
    @ToString
    public static class InitConfig {
        private InitSmsConfig sms;
    }

    /**
     * 初始化短信配置bean
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    public static class InitSmsConfig extends SmsConfig {
        private AlibabaConfig alibaba;
        private CloopenConfig cloopen;
        private EmayConfig emay;
        private HuaweiConfig huawei;
        private JdCloudConfig jdCloud;
        private TencentConfig tencent;
        private UniConfig uni;
        private YunpianConfig yunpian;
        private NeteaseConfig neteaseConfig;
        private ZhutongConfig zhutongConfig;
    }

}
