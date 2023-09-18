package org.dromara.sms4j.javase.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaFactory;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.dao.SmsDaoDefaultImpl;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.cloopen.config.CloopenFactory;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.core.proxy.RestrictedProcessDefaultImpl;
import org.dromara.sms4j.core.proxy.SmsInvocationHandler;
import org.dromara.sms4j.ctyun.config.CtyunFactory;
import org.dromara.sms4j.emay.config.EmayFactory;
import org.dromara.sms4j.huawei.config.HuaweiFactory;
import org.dromara.sms4j.javase.util.YamlUtils;
import org.dromara.sms4j.jdcloud.config.JdCloudFactory;
import org.dromara.sms4j.netease.config.NeteaseFactory;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.BeanFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.dromara.sms4j.tencent.config.TencentFactory;
import org.dromara.sms4j.unisms.config.UniFactory;
import org.dromara.sms4j.yunpian.config.YunPianFactory;
import org.dromara.sms4j.zhutong.config.ZhutongFactory;

import java.util.List;
import java.util.Map;

/**
 * 初始化类
 */
@Slf4j
public class SEInitializer {

    private static final SEInitializer INSTANCE = new SEInitializer();

    public static SEInitializer initializer() {
        return INSTANCE;
    }

    private SmsDao smsDao;

    /**
     * 默认从sms4j.yml文件中读取配置
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
        InitConfig config = YamlUtils.toBean(yaml, InitConfig.class);
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

    /**
     * 从配置bean对象中加载配置
     *
     * @param smsConfig 短信公共配置
     * @param configList 各短信服务商配置列表
     */
    public void fromConfig(SmsConfig smsConfig, List<SupplierConfig> configList) {
        // 注册默认工厂
        registerDefaultFactory();
        // 初始化SmsConfig整体配置文件
        BeanUtil.copyProperties(smsConfig, BeanFactory.getSmsConfig());
        // 创建短信服务对象
        if(CollUtil.isEmpty(configList)) {
            return ;
        }
        for(SupplierConfig supplierConfig : configList) {
            if(Boolean.TRUE.equals(smsConfig.getRestricted())) {
                SmsFactory.createRestrictedSmsBlend(supplierConfig);
            } else {
                SmsFactory.createSmsBlend(supplierConfig);
            }
        }
    }

    /**
     * 注册服务商工厂
     * @param factory 服务商工厂
     */
    public SEInitializer registerFactory(BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> factory) {
        ProviderFactoryHolder.registerFactory(factory);
        return this;
    }

    /**
     * 注册DAO实例
     * @param smsDao DAO实例
     */
    public SEInitializer registerSmsDao(SmsDao smsDao) {
        if(smsDao == null) {
            throw new SmsBlendException("注册DAO实例失败，实例不能为空");
        }
        RestrictedProcessDefaultImpl process = new RestrictedProcessDefaultImpl();
        process.setSmsDao(smsDao);
        SmsInvocationHandler.setRestrictedProcess(process);
        this.smsDao = smsDao;
        return this;
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

        //注册默认DAO实例
        if(this.smsDao == null) {
            this.registerSmsDao(SmsDaoDefaultImpl.getInstance());
        }

        //注册默认工厂
        registerDefaultFactory();

        //初始化SmsConfig整体配置文件
        BeanUtil.copyProperties(smsConfig, BeanFactory.getSmsConfig());
        // 解析服务商配置
        Map<String, Map<String, Object>> blends = smsConfig.getBlends();
        for(String configId : blends.keySet()) {
            Map<String, Object> configMap = blends.get(configId);
            Object supplierObj = configMap.get(Constant.SUPPLIER_KEY);
            String supplier = supplierObj == null ? "" : String.valueOf(supplierObj);
            supplier = StrUtil.isEmpty(supplier) ? configId : supplier;
            BaseProviderFactory<SmsBlend, SupplierConfig> providerFactory = (BaseProviderFactory<SmsBlend, SupplierConfig>) ProviderFactoryHolder.requireForSupplier(supplier);
            if(providerFactory == null) {
                log.warn("创建\"{}\"的短信服务失败，未找到服务商为\"{}\"的服务", configId, supplier);
                continue;
            }
            configMap.put("config-id", configId);
            SmsUtils.replaceKeysSeperator(configMap, "-", "_");
            JSONObject configJson = new JSONObject(configMap);
            SupplierConfig supplierConfig = JSONUtil.toBean(configJson, providerFactory.getConfigClass());
            if(Boolean.TRUE.equals(smsConfig.getRestricted())) {
                SmsFactory.createRestrictedSmsBlend(supplierConfig);
            } else {
                SmsFactory.createSmsBlend(supplierConfig);
            }
        }
    }

    /**
     * 注册默认工厂实例
     */
    private void registerDefaultFactory() {
        ProviderFactoryHolder.registerFactory(AlibabaFactory.instance());
        ProviderFactoryHolder.registerFactory(CloopenFactory.instance());
        ProviderFactoryHolder.registerFactory(CtyunFactory.instance());
        ProviderFactoryHolder.registerFactory(EmayFactory.instance());
        ProviderFactoryHolder.registerFactory(HuaweiFactory.instance());
        ProviderFactoryHolder.registerFactory(NeteaseFactory.instance());
        ProviderFactoryHolder.registerFactory(TencentFactory.instance());
        ProviderFactoryHolder.registerFactory(UniFactory.instance());
        ProviderFactoryHolder.registerFactory(YunPianFactory.instance());
        ProviderFactoryHolder.registerFactory(ZhutongFactory.instance());
        if(SmsUtils.isClassExists("com.jdcloud.sdk.auth.CredentialsProvider")) {
            ProviderFactoryHolder.registerFactory(JdCloudFactory.instance());
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
        private Map<String, Map<String, Object>> blends;
    }

}
