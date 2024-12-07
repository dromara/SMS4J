package org.dromara.oa.core.config;

import lombok.Data;
import org.dromara.oa.comm.task.delayed.DelayedTime;
import org.dromara.oa.core.provider.config.OaConfig;
import org.dromara.oa.core.provider.factory.OaBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;

import java.util.concurrent.Executor;

/**
 * @author dongfeng
 * 2023-11-01 18:05
 */
@Data
public class OaMainConfig {

    @Bean
    @ConditionalOnProperty(prefix = "sms-oa", name = "config-type", havingValue = "yaml")
    protected OaConfig oaConfig(OaConfigProperties properties) {
        OaConfig smsConfig = OaBeanFactory.getSmsConfig();
        smsConfig.setCorePoolSize(properties.getCorePoolSize());
        smsConfig.setMaxPoolSize(properties.getMaxPoolSize());
        smsConfig.setQueueCapacity(properties.getQueueCapacity());
        smsConfig.setShutdownStrategy(properties.getShutdownStrategy());
        return smsConfig;
    }

    /**
     * 注入一个定时器
     */
    @Bean("oaDelayedTime")
    @Lazy
    protected DelayedTime delayedTime() {
        return OaBeanFactory.getDelayedTime();
    }

    /**
     * 注入线程池
     */
    @Bean("oaExecutor")
    @ConditionalOnProperty(prefix = "sms-oa", name = "config-type", havingValue = "yaml")
    protected Executor taskExecutor(OaConfig config) {
        return OaBeanFactory.setExecutor(config);
    }

}
