package org.dromara.sms4j.javase.config;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.json.JSONUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.core.initalize.AbstractInitalizer;
import org.dromara.sms4j.javase.util.YamlUtils;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.BeanFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化类
 */
@Slf4j
public class SEInitializer extends AbstractInitalizer {

    private static final SEInitializer INSTANCE = new SEInitializer();

    public static SEInitializer initializer() {
        return INSTANCE;
    }


    /**
     * 默认从sms4j.yml文件中读取配置
     */
    public void fromYaml() {
        ClassPathResource yamlResource = new ClassPathResource("sms4j.yml");
        this.fromYaml(yamlResource.readUtf8Str());
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
     * @param smsConfig  短信公共配置
     * @param configList 各短信服务商配置列表
     */
    public void fromConfig(SmsConfig smsConfig, List<SupplierConfig> configList) {
        // 注册默认工厂
        registerDefaultFactory();
        // 初始化SmsConfig整体配置文件
        BeanUtil.copyProperties(smsConfig, BeanFactory.getSmsConfig());
        // 创建短信服务对象
        if (CollUtil.isEmpty(configList)) {
            return;
        }
        try {
            Map<String, Map<String, Object>> blends = new HashMap<>();
            for (SupplierConfig supplierConfig : configList) {
                Map<String, Object> param = new HashMap<>();
                String channel = supplierConfig.getConfigId();
                if (null == channel){
                    throw new IllegalArgumentException("自定义Config对象未设置configId");
                }
                Class<? extends SupplierConfig> clazz = supplierConfig.getClass();
                BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    Method readMethod = propertyDescriptor.getReadMethod();
                    Object item = readMethod.invoke(supplierConfig);
                    param.put(propertyDescriptor.getName(), item);
                }
                blends.put(channel, param);
            }
            initInterceptor(blends, smsConfig);
        } catch (IntrospectionException | InvocationTargetException e) {
            log.error("配置对象转换配置信息失败，但不影响基础功能的使用。【注意】：未加载SMS4J扩展功能模块，拦截器，参数校验可能失效！");
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        for (SupplierConfig supplierConfig : configList) {
            SmsFactory.createSmsBlend(supplierConfig);
        }
    }

    /**
     * 注册拦截器
     *
     * @param interceptor 拦截器
     */
    public SEInitializer registerSmsMethodInterceptor(SmsMethodInterceptor interceptor) {
        doRegisterSmsMethodInterceptor(interceptor);
        return this;
    }

    /**
     * 注册拦截器执行策略
     *
     * @param interceptorStrategy 拦截器执行策略
     */
    public SEInitializer registerIInterceptorStrategy(IInterceptorStrategy interceptorStrategy) {
        doRegisterIInterceptorStrategy(interceptorStrategy);
        return this;
    }

    /**
     * 注册DAO实例
     *
     * @param smsDao DAO实例
     */
    public SEInitializer registerSmsDao(SmsDao smsDao) {
        if (smsDao == null) {
            throw new SmsBlendException("注册DAO实例失败，实例不能为空");
        }
        doRegisterSmsDao(smsDao);
        return this;
    }

    /**
     * 注册服务商工厂
     *
     * @param factory 服务商工厂
     */
    public SEInitializer registerFactory(BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig> factory) {
        ProviderFactoryHolder.registerFactory(factory);
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

        //注册默认工厂
        registerDefaultFactory();

        //初始化SmsConfig整体配置文件
        BeanUtil.copyProperties(smsConfig, BeanFactory.getSmsConfig());

        //获取各厂商配置
        Map<String, Map<String, Object>> blends = smsConfig.getBlends();

        //装在拦截器和拦截器策略
        initInterceptor(blends, smsConfig);

        // 解析供应商配置
        doParseChannelConfigWithCreate(blends);
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
