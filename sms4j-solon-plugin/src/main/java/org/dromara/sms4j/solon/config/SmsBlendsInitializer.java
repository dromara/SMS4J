package org.dromara.sms4j.solon.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.core.initalize.AbstractInitalizer;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BaseProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.noear.solon.core.AppContext;

import java.util.List;
import java.util.Map;


@Slf4j
public class SmsBlendsInitializer extends AbstractInitalizer {

    public SmsBlendsInitializer(List<BaseProviderFactory<? extends SmsBlend, ? extends SupplierConfig>> factoryList,
                                SmsConfig smsConfig,
                                Map<String, Map<String, Object>> blends,
                                AppContext context
    ) {
        // 注册短信对象工厂
        this.registerDefaultFactory();

        //注册缓存实现
        doRegisterSmsDao(context.getBean(SmsDao.class));

        //注册拦截器
        List<SmsMethodInterceptor> smsMethodInterceptors = context.getBeansOfType(SmsMethodInterceptor.class);
        for (SmsMethodInterceptor smsMethodInterceptor : smsMethodInterceptors) {
            doRegisterSmsMethodInterceptor(smsMethodInterceptor);
        }

        //注册拦截器策略
        List<IInterceptorStrategy> interceptorStrategies = context.getBeansOfType(IInterceptorStrategy.class);
        for (IInterceptorStrategy interceptorStrategy : interceptorStrategies) {
            doRegisterIInterceptorStrategy(interceptorStrategy);
        }

        //装在拦截器和拦截器策略
        initInterceptor(blends, smsConfig);

        // 解析供应商配置
        doParseChannelConfigWithCreate(blends);
    }
}
