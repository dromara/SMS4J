package org.dromara.sms4j.cloopen.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.config.BaseConfig;

/**
 * 容联云短信配置属性
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class CloopenConfig extends BaseConfig implements SupplierConfig {

    /**
     * 应用 ID
     */
    private String appId;

    /**
     * REST API Base URL
     */
    @Builder.Default
    private String baseUrl = "https://app.cloopen.com:8883/2013-12-26";

    /**
     * Rest URL 域名
     *
     * @see #baseUrl
     * @deprecated v2.0.1
     */
    @Deprecated
    private String serverIp;

    /**
     * Rest URL 端口
     *
     * @see #baseUrl
     * @deprecated v2.0.1
     */
    @Deprecated
    private String serverPort;
}
