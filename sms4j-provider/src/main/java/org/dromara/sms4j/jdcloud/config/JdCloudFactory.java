package org.dromara.sms4j.jdcloud.config;

import com.jdcloud.sdk.auth.CredentialsProvider;
import com.jdcloud.sdk.auth.StaticCredentialsProvider;
import com.jdcloud.sdk.http.HttpRequestConfig;
import com.jdcloud.sdk.http.Protocol;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.jdcloud.service.JdCloudSmsImpl;
import org.dromara.sms4j.provider.base.BaseProviderFactory;

/**
 * 京东云短信配置
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
public class JdCloudFactory implements BaseProviderFactory<JdCloudSmsImpl, JdCloudConfig> {

    private static JdCloudSmsImpl jdCloudSms;

    private static final JdCloudFactory INSTANCE = new JdCloudFactory();

    private static final class ConfigHolder {
        private static JdCloudConfig config = JdCloudConfig.builder().build();
    }

    private JdCloudFactory() {
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
        if (jdCloudSms == null) {
            jdCloudSms = new JdCloudSmsImpl(
                    this.client(jdCloudConfig),
                    jdCloudConfig,
                    BeanFactory.getExecutor(),
                    BeanFactory.getDelayedTime()
            );
        }
        return jdCloudSms;
    }

    /**
     * 刷新对象
     */
    @Override
    public JdCloudSmsImpl refresh(JdCloudConfig jdCloudConfig) {
        jdCloudSms = new JdCloudSmsImpl(
                this.client(jdCloudConfig),
                jdCloudConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime()
        );
        return jdCloudSms;
    }

    /**
     * 获取配置
     * @return 配置对象
     */
    @Override
    public JdCloudConfig getConfig() {
        return ConfigHolder.config;
    }

    /**
     * 设置配置
     * @param config 配置对象
     */
    @Override
    public void setConfig(JdCloudConfig config) {
        ConfigHolder.config = config;
    }

}
