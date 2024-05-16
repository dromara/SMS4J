package org.dromara.sms4j.starter.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.enumerate.ConfigType;
import org.dromara.sms4j.core.datainterface.SmsBlendsBeanConfig;
import org.dromara.sms4j.core.datainterface.SmsBlendsSelectedConfig;
import org.dromara.sms4j.core.datainterface.SmsReadConfig;
import org.dromara.sms4j.core.factory.SmsFactory;
import org.dromara.sms4j.core.initalize.AbstractInitalizer;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.dromara.sms4j.starter.adepter.ConfigCombineMapAdeptor;
import org.springframework.beans.factory.ObjectProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class SmsBlendsInitializer extends AbstractInitalizer {


    public SmsBlendsInitializer(List<BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig>> factoryList,
                                SmsConfig smsConfig,
                                Map<String, Map<String, Object>> blends,
                                ObjectProvider<SmsDao> smsDaos,
                                ObjectProvider<SmsBlendsBeanConfig> beanConfigs,
                                ObjectProvider<SmsBlendsSelectedConfig> selectedConfigs,
                                ObjectProvider<SmsMethodInterceptor> smsMethodInterceptorObjectProvider,
                                ObjectProvider<IInterceptorStrategy> interceptorStrategyObjectProvider) {
        //注册预置工厂
        this.registerDefaultFactory();

        // 注册自定义工厂
        ProviderFactoryHolder.registerFactory(factoryList);

        // 注册缓存实现
        doRegisterSmsDao(smsDaos.getIfAvailable());

        //注册缓存实现
        for (SmsMethodInterceptor smsMethodInterceptor : smsMethodInterceptorObjectProvider) {
            doRegisterSmsMethodInterceptor(smsMethodInterceptor);
        }

        //注册拦截器策略
        for (IInterceptorStrategy interceptorStrategy : interceptorStrategyObjectProvider) {
            doRegisterIInterceptorStrategy(interceptorStrategy);
        }

        //按照配置的配置文件类型处理加载及实例化
        switch (smsConfig.getConfigType()) {
            //yaml 只需处理yml中的配置信息
            case YAML:
                //装载拦截器和策略
                initInterceptor(blends, smsConfig);
                // 解析供应商配置
                doParseChannelConfigWithCreate(blends);
                break;

            //yaml 只需要处理实现了，SmsBlendsBeanConfig、 SmsBlendsSelectedConfig这两种接口的配置信息
            case INTERFACE:
                //如果是INTERFACE的情况，那么blends里面等于没有东西，同时配置的信息获取不到，所以需要进行转换，但是SmsBlendsSelectedConfig存在参数，只能懒转换，下方ConfigCombineMapAdeptor实现懒转换
                Map<String, Map<String, Object>> blendsInclude = new ConfigCombineMapAdeptor<String, Map<String, Object>>();
                blendsInclude.putAll(blends);
                //给一个序号区别需要懒转换的配置信息
                int num = 0;
                for (SmsBlendsBeanConfig smsBlendsBeanConfig : beanConfigs) {
                    String key = SmsReadConfig.class.getSimpleName() + num;
                    Map<String, Object> insideMap = new HashMap<>();
                    insideMap.put(key, smsBlendsBeanConfig);
                    blendsInclude.put(key, insideMap);
                    num++;
                }
                for (SmsBlendsSelectedConfig smsBlendsSelectedConfig : selectedConfigs) {
                    String key = SmsReadConfig.class.getSimpleName() + num;
                    Map<String, Object> insideMap = new HashMap<>();
                    insideMap.put(key, smsBlendsSelectedConfig);
                    blendsInclude.put(key, insideMap);
                    num++;
                }
                //装载拦截器和策略
                initInterceptor(blendsInclude, smsConfig);
                //SmsBlendsBeanConfig接口实现类的配置信息处理
                for (SmsBlendsBeanConfig beanConfig : beanConfigs) {
                    SmsFactory.createSmsBlend(beanConfig);

                }
                break;
        }
    }
}
