package org.dromara.sms4j.api.universal;

import lombok.Data;

import java.io.Serializable;

/**
 * 短信代理配置类
 */
@Data
public class ProxyConfig implements Serializable {

    /**
     * 是否启用代理 默认不启用
     */
    private Boolean enable = false;

    /**
     * 代理服务器地址
     */
    private String host;

    /**
     * 代理服务器端口
     */
    private Integer port;
}
