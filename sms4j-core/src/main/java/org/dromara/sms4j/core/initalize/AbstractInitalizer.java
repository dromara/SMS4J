package org.dromara.sms4j.core.initalize;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaFactory;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.dao.SmsDaoDefaultImpl;
import org.dromara.sms4j.api.manage.InterceptorStrategySmsManager;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.cloopen.config.CloopenFactory;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;
import org.dromara.sms4j.core.proxy.interceptor.*;
import org.dromara.sms4j.core.proxy.strategy.impl.*;
import org.dromara.sms4j.ctyun.config.CtyunFactory;
import org.dromara.sms4j.dingzhong.config.DingZhongFactory;
import org.dromara.sms4j.emay.config.EmayFactory;
import org.dromara.sms4j.huawei.config.HuaweiFactory;
import org.dromara.sms4j.jdcloud.config.JdCloudFactory;
import org.dromara.sms4j.lianlu.config.LianLuFactory;
import org.dromara.sms4j.local.LocalFactory;
import org.dromara.sms4j.netease.config.NeteaseFactory;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.dromara.sms4j.qiniu.config.QiNiuFactory;
import org.dromara.sms4j.tencent.config.TencentFactory;
import org.dromara.sms4j.unisms.config.UniFactory;
import org.dromara.sms4j.yunpian.config.YunPianFactory;
import org.dromara.sms4j.zhutong.config.ZhutongFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * AbstractInitalizer
 * <p>抽象初始化器
 *
 * @author :Wind
 * 2023/4/8  15:55
 **/
@Slf4j
public abstract class AbstractInitalizer {

    //Dao的实现
    private SmsDao smsDao = SmsDaoDefaultImpl.getInstance();

    //所有注册进来的拦截器
    private List<SmsMethodInterceptor> smsMethodInterceptors = new ArrayList<>();

    //所有注册进来的拦截器策略
    private List<IInterceptorStrategy> interceptorStrategies = new ArrayList<>();

    /**
     * 注册拦截器
     *
     * @param interceptor 拦截器
     */
    public void doRegisterSmsMethodInterceptor(SmsMethodInterceptor interceptor) {
        smsMethodInterceptors.add(interceptor);
    }

    /**
     * 注册拦截器执行策略
     *
     * @param interceptorStrategy 拦截器执行策略
     */
    public void doRegisterIInterceptorStrategy(IInterceptorStrategy interceptorStrategy) {
        this.interceptorStrategies.add(interceptorStrategy);
    }

    /**
     * 注册DAO实例
     *
     * @param smsDao DAO实例
     */
    public void doRegisterSmsDao(SmsDao smsDao) {
        if (smsDao == null) {
            smsDao =  SmsDaoDefaultImpl.getInstance();
        }
        this.smsDao = smsDao;
    }

    /**
     * 装载拦截器以及拦截器策略
     *
     * @param blends 渠道配置
     * @param smsConfig sms4j公共基础配置
     */
    protected void initInterceptor(Map<String, Map<String, Object>> blends, SmsConfig smsConfig) {
        InterceptorStrategySmsManager.setBlends(blends);
        InterceptorStrategySmsManager.setSmsConfig(smsConfig);
        InterceptorStrategySmsManager.setSmsDao(this.smsDao);
        DefaultAcctMaxRestrictedMethodStrategy defaultAcctMaxRestrictedMethodStrategy = new DefaultAcctMaxRestrictedMethodStrategy();
        InterceptorStrategySmsManager.setInterceptorStrategyMapping( defaultAcctMaxRestrictedMethodStrategy);
        DefaultBlackListCheckStrategy defaultBlackListCheckStrategy = new DefaultBlackListCheckStrategy();
        InterceptorStrategySmsManager.setInterceptorStrategyMapping( defaultBlackListCheckStrategy);
        DefaultBlackListManageStrategy defaultBlackListManageStrategy = new DefaultBlackListManageStrategy();
        InterceptorStrategySmsManager.setInterceptorStrategyMapping( defaultBlackListManageStrategy);
        DefaultChannelMaxRestrictedMethodStrategy defaultChannelMaxRestrictedMethodStrategy = new DefaultChannelMaxRestrictedMethodStrategy();
        InterceptorStrategySmsManager.setInterceptorStrategyMapping( defaultChannelMaxRestrictedMethodStrategy);
        DefaultSpanMaxRestrictedMethodStrategy defaultSpanMaxRestrictedMethodStrategy = new DefaultSpanMaxRestrictedMethodStrategy();
        InterceptorStrategySmsManager.setInterceptorStrategyMapping( defaultSpanMaxRestrictedMethodStrategy);
        DefaultSyncMethodParamValidateStrategy defaultSyncMethodParamValidateStrategy = new DefaultSyncMethodParamValidateStrategy();
        InterceptorStrategySmsManager.setInterceptorStrategyMapping( defaultSyncMethodParamValidateStrategy);
        for (IInterceptorStrategy interceptorStrategy : this.interceptorStrategies) {
            InterceptorStrategySmsManager.setInterceptorStrategyMapping( interceptorStrategy);
        }
        InterceptorStrategySmsManager.freezes();
        SmsProxyFactory.addInterceptor(new AcctMaxRestrictedMethodInterceptor());
        SmsProxyFactory.addInterceptor(new BlackListMethodInterceptor());
        SmsProxyFactory.addInterceptor(new BlackListRecordingProxyInterceptor());
        SmsProxyFactory.addInterceptor(new ChannelMaxRestrictedMethodInterceptor());
        SmsProxyFactory.addInterceptor(new SpanMaxRestrictedMethodInterceptor());
        SmsProxyFactory.addInterceptor(new SyncMethodParamValidateMethodInterceptor());
        for (SmsMethodInterceptor smsMethodInterceptor : this.smsMethodInterceptors) {
            SmsProxyFactory.addInterceptor(smsMethodInterceptor);
        }
    }

    /**
     * 解析各渠道配置并创建对应实现
     *
     * @param blends 渠道配置
     */
    protected void doParseChannelConfigWithCreate(Map<String, Map<String, Object>> blends){
        blends.forEach((configId, configMap) -> {
            Object supplierObj = configMap.get(Constant.SUPPLIER_KEY);
            String supplier = supplierObj == null ? "" : String.valueOf(supplierObj);
            supplier = StrUtil.isEmpty(supplier) ? configId : supplier;
            BaseProviderFactory<SmsBlend, SupplierConfig> providerFactory = (BaseProviderFactory<SmsBlend, SupplierConfig>) ProviderFactoryHolder.requireForSupplier(supplier);
            if (providerFactory == null) {
                log.warn("创建'{}'的短信服务失败，未找到服务商为'{}'的服务", configId, supplier);
                return;
            }
            configMap.put("config-id", configId);
            SmsUtils.replaceKeysSeperator(configMap, "-", "_");
            JSONObject configJson = new JSONObject(configMap);
            SupplierConfig supplierConfig = JSONUtil.toBean(configJson, providerFactory.getConfigClass());
            SmsFactory.createSmsBlend(supplierConfig);
        });
    }

    /**
     * 注册预置工厂实例
     */
    protected void registerDefaultFactory() {
        ProviderFactoryHolder.registerFactory(LocalFactory.instance());
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
        ProviderFactoryHolder.registerFactory(LianLuFactory.instance());
        ProviderFactoryHolder.registerFactory(DingZhongFactory.instance());
        ProviderFactoryHolder.registerFactory(QiNiuFactory.instance());
        if(SmsUtils.isClassExists("com.jdcloud.sdk.auth.CredentialsProvider")) {
            ProviderFactoryHolder.registerFactory(JdCloudFactory.instance());
        }
    }
}
