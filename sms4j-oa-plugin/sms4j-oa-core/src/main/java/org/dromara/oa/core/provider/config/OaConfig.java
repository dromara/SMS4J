package org.dromara.oa.core.provider.config;

import lombok.Data;

/**
 * Oa线程池配置
 * @author dongfeng
 * 2023-11-01 17:55
 */
@Data
public class OaConfig {
    /**
     * 核心线程池大小
     */
    private Integer corePoolSize = 10;

    /**
     * 最大线程数
     */
    private Integer maxPoolSize = 30;

    /**
     * 队列容量
     */
    private Integer queueCapacity = 50;

    /**
     * 设置线程池关闭的时候等待所有任务都完成再继续销毁其他的Bean
     */
    private Boolean shutdownStrategy = true;
}
