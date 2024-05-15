package org.dromara.sms4j.source;

import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.stereotype.Component;
import sms4j.local.a.AFactory;
import sms4j.local.b.BFactory;
import sms4j.local.c.CFactory;

@Component
public class EarlyPostProcessor implements BeanFactoryPostProcessor {
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        // 注入工厂实例
        ProviderFactoryHolder.registerFactory(new AFactory());
        ProviderFactoryHolder.registerFactory(new BFactory());
        ProviderFactoryHolder.registerFactory(new CFactory());
    }
}
