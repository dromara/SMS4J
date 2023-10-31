package org.dromara.sms4j.provider.config;

import lombok.Data;
import org.dromara.sms4j.api.universal.SupplierConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;

/**
 * 短信配置属性基类
 *
 * @author Charles7c
 * @since 2023/4/20 23:03
 */
@Data
public abstract class BaseConfig implements SupplierConfig {

    /**
     * 自定义实现工厂路径
     */
    private String factory;

    /**
     * Access Key
     */
    private String accessKeyId;
    /**
     * Sdk App Id
     */
    private String sdkAppId;

    /**
     * Access Key Secret
     */
    private String accessKeySecret;

    /**
     * 短信签名
     */
    private String signature;

    /**
     * 模板 ID
     */
    private String templateId;

    /**
     * 权重
     * @since 3.0.0
     */
    private Integer weight = 1;

    /**
     * 配置标识名 如未配置取对应渠道名例如 Alibaba
     *
     * @since 3.0.0
     */
    private String configId;

    /**
     * 重试间隔（单位：秒），默认为5秒
     */
    private int retryInterval = 5;

    public void setRetryInterval(int retryInterval) {
        if (retryInterval <= 0){
            throw new SmsBlendException("重试间隔必须大于0秒");
        }
        this.retryInterval = retryInterval;
    }

    /**
     * 重试次数，默认为0次
     */
    private int maxRetries = 0;

    public void setMaxRetries(int maxRetries) {
        if (maxRetries < 0){
            throw new SmsBlendException("重试次数不能小于0次");
        }
        this.maxRetries = maxRetries;
    }
}
