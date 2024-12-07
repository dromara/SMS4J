package org.dromara.oa.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author chen_lei
 * @date 2024/12/7 19:32
 */

@Data
@ConfigurationProperties(prefix = "sms-oa")
public class OaConfigProperties {

    private String configType;
    private Map<String, Map<String, Object>> oas = new HashMap<>();

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
