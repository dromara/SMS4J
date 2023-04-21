package org.dromara.sms4j.tencent.config;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.dromara.sms4j.comm.config.BaseConfig;

@Data
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TencentConfig extends BaseConfig {

    /**
     * 短信sdkAppId
     */
    private String sdkAppId;

    /**
     * 地域信息默认为 ap-guangzhou
     */
    @Builder.Default
    private String territory = "ap-guangzhou";

    /**
     * 请求超时时间
     */
    @Builder.Default
    private Integer connTimeout = 60;
}
