package org.dromara.sms4j.solon.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaFactory;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.api.verify.PhoneVerify;
import org.dromara.sms4j.budingyun.config.BudingV2Factory;
import org.dromara.sms4j.cloopen.config.CloopenFactory;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.core.proxy.EnvirmentHolder;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;
import org.dromara.sms4j.core.proxy.processor.BlackListProcessor;
import org.dromara.sms4j.core.proxy.processor.BlackListRecordingProcessor;
import org.dromara.sms4j.core.proxy.processor.CoreMethodParamValidateProcessor;
import org.dromara.sms4j.core.proxy.processor.RestrictedProcessor;
import org.dromara.sms4j.core.proxy.processor.SingleBlendRestrictedProcessor;
import org.dromara.sms4j.ctyun.config.CtyunFactory;
import org.dromara.sms4j.dingzhong.config.DingZhongFactory;
import org.dromara.sms4j.emay.config.EmayFactory;
import org.dromara.sms4j.huawei.config.HuaweiFactory;
import org.dromara.sms4j.jdcloud.config.JdCloudFactory;
import org.dromara.sms4j.lianlu.config.LianLuFactory;
import org.dromara.sms4j.mas.config.MasFactory;
import org.dromara.sms4j.netease.config.NeteaseFactory;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.dromara.sms4j.qiniu.config.QiNiuFactory;
import org.dromara.sms4j.solon.holder.SolonSmsDaoHolder;
import org.dromara.sms4j.tencent.config.TencentFactory;
import org.dromara.sms4j.unisms.config.UniFactory;
import org.dromara.sms4j.yunpian.config.YunPianFactory;
import org.dromara.sms4j.zhutong.config.ZhutongFactory;
import org.noear.solon.core.AppContext;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;


@Slf4j
public class SmsBlendsInitializer {
    private final List<BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig>> factoryList;

    private final SmsConfig smsConfig;
    private final Map<String, Map<String, Object>> blends;
    private final AppContext context;

    public SmsBlendsInitializer(List<BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig>> factoryList,
                                SmsConfig smsConfig,
                                Map<String, Map<String, Object>> blends,
                                AppContext context
                                ){
        this.factoryList = factoryList;
        this.smsConfig = smsConfig;
        this.blends = blends;
        this.context = context;
        onApplicationEvent();
    }

    public void onApplicationEvent() {
        this.registerDefaultFactory();
        // 注册短信对象工厂
        ProviderFactoryHolder.registerFactory(factoryList);
        //持有初始化配置信息
        EnvirmentHolder.frozenEnvirmet(smsConfig, blends);
        //框架依赖持有缓存扩展
        new SolonSmsDaoHolder(context);
        //注册执行器实现
        SmsProxyFactory.addProcessor(new RestrictedProcessor());
        SmsProxyFactory.addProcessor(new BlackListProcessor());
        SmsProxyFactory.addProcessor(new BlackListRecordingProcessor());
        SmsProxyFactory.addProcessor(new SingleBlendRestrictedProcessor());
        //如果手机号校验器存在实现，则注册手机号校验器
        ServiceLoader<PhoneVerify> loader = ServiceLoader.load(PhoneVerify.class);
        if (loader.iterator().hasNext()) {
            loader.forEach(f -> {
                SmsProxyFactory.addProcessor(new CoreMethodParamValidateProcessor(f));
            });
        } else {
            SmsProxyFactory.addProcessor(new CoreMethodParamValidateProcessor(null));
        }
        // 解析供应商配置
        for(String configId : blends.keySet()) {
            Map<String, Object> configMap = blends.get(configId);
            Object supplierObj = configMap.get(Constant.SUPPLIER_KEY);
            String supplier = supplierObj == null ? "" : String.valueOf(supplierObj);
            supplier = StrUtil.isEmpty(supplier) ? configId : supplier;
            BaseProviderFactory<SmsBlend, SupplierConfig> providerFactory = (BaseProviderFactory<SmsBlend, SupplierConfig>) ProviderFactoryHolder.requireForSupplier(supplier);
            if(providerFactory == null) {
                log.warn("创建\"{}\"的短信服务失败，未找到供应商为\"{}\"的服务", configId, supplier);
                continue;
            }
            configMap.put("config-id", configId);
            SmsUtils.replaceKeysSeperator(configMap, "-", "_");
            JSONObject configJson = new JSONObject(configMap);
            SupplierConfig supplierConfig = JSONUtil.toBean(configJson, providerFactory.getConfigClass());
            SmsFactory.createSmsBlend(supplierConfig);
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
        ProviderFactoryHolder.registerFactory(LianLuFactory.instance());
        ProviderFactoryHolder.registerFactory(DingZhongFactory.instance());
        ProviderFactoryHolder.registerFactory(QiNiuFactory.instance());
        ProviderFactoryHolder.registerFactory(BudingV2Factory.instance());
        ProviderFactoryHolder.registerFactory(MasFactory.instance());
        if(SmsUtils.isClassExists("com.jdcloud.sdk.auth.CredentialsProvider")) {
            ProviderFactoryHolder.registerFactory(JdCloudFactory.instance());
        }
    }

}
