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
import org.dromara.sms4j.core.factory.SmsFactory;
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
            SmsFactory.createSmsBlend(alibabaConfig);
        }
        CloopenConfig cloopenConfig = smsConfig.getCloopen();
        if (cloopenConfig != null) {
            SmsFactory.createSmsBlend(cloopenConfig);
        }
        EmayConfig emayConfig = smsConfig.getEmay();
        if (emayConfig != null) {
            SmsFactory.createSmsBlend(emayConfig);
        }
        HuaweiConfig huaweiConfig = smsConfig.getHuawei();
        if (huaweiConfig != null) {
            SmsFactory.createSmsBlend(huaweiConfig);
        }
        JdCloudConfig jdCloudConfig = smsConfig.getJdCloud();
        if (jdCloudConfig != null) {
            SmsFactory.createSmsBlend(jdCloudConfig);
        }
        TencentConfig tencentConfig = smsConfig.getTencent();
        if (tencentConfig != null) {
            SmsFactory.createSmsBlend(tencentConfig);
        }
        UniConfig uniConfig = smsConfig.getUni();
        if (uniConfig != null) {
            SmsFactory.createSmsBlend(uniConfig);
        }
        YunpianConfig yunpianConfig = smsConfig.getYunpian();
        if (yunpianConfig != null) {
            SmsFactory.createSmsBlend(yunpianConfig);
        }
        NeteaseConfig neteaseConfig = smsConfig.getNeteaseConfig();
        if (neteaseConfig != null){
            SmsFactory.createSmsBlend(neteaseConfig);
        }
        ZhutongConfig zhutongConfig = smsConfig.getZhutongConfig();
        if (zhutongConfig != null){
            SmsFactory.createSmsBlend(zhutongConfig);
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
