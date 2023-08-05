package org.dromara.sms4j.jdcloud.config;

import com.jdcloud.sdk.auth.CredentialsProvider;
import com.jdcloud.sdk.auth.StaticCredentialsProvider;
import com.jdcloud.sdk.http.HttpRequestConfig;
import com.jdcloud.sdk.http.Protocol;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.dromara.sms4j.jdcloud.service.JdCloudSmsImpl;
import org.dromara.sms4j.provider.factory.AbstractProviderFactory;
import org.dromara.sms4j.provider.factory.ProviderFactoryHolder;

/**
 * 京东云短信配置
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JdCloudFactory extends AbstractProviderFactory<JdCloudSmsImpl, JdCloudConfig> {

    private static final JdCloudFactory INSTANCE = new JdCloudFactory();

    static {
        ProviderFactoryHolder.registerFactory(INSTANCE);
    }

    /**
     * 获取建造者实例
     * @return 建造者实例
     */
    public static JdCloudFactory instance() {
        return INSTANCE;
    }

    /**
     * 客户端对象
     *
     * @param jdCloudConfig 京东云短信配置属性
     * @return 客户端对象
     */
    public SmsClient client(JdCloudConfig jdCloudConfig) {
        CredentialsProvider credentialsProvider = new StaticCredentialsProvider(jdCloudConfig.getAccessKeyId(),
                jdCloudConfig.getAccessKeySecret());
        return SmsClient.builder().credentialsProvider(credentialsProvider)
                .httpRequestConfig(new HttpRequestConfig.Builder().protocol(Protocol.HTTP).build()).build();
    }

    /**
     * 创建京东云短信实现
     */
    @Override
    public JdCloudSmsImpl createSms(JdCloudConfig jdCloudConfig) {
        return new JdCloudSmsImpl(
                this.client(jdCloudConfig),
                jdCloudConfig
        );
    }

    /**
     * 获取供应商
     * @return 供应商
     */
    @Override
    public String getSupplier() {
        return JdCloudSmsImpl.SUPPLIER;
    }

}
