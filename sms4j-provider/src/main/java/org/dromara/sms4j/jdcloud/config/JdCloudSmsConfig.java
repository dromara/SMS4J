package org.dromara.sms4j.jdcloud.config;

import com.jdcloud.sdk.auth.CredentialsProvider;
import com.jdcloud.sdk.auth.StaticCredentialsProvider;
import com.jdcloud.sdk.http.HttpRequestConfig;
import com.jdcloud.sdk.http.Protocol;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.jdcloud.service.JdCloudSmsImpl;

/**
 * 京东云短信配置
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
public class JdCloudSmsConfig {

    private static JdCloudSmsImpl jdCloudSms;

    private static JdCloudSmsConfig jdCloudSmsConfig;

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
    public static JdCloudSmsImpl createJdCloudSms(JdCloudConfig jdCloudConfig) {
        if (jdCloudSmsConfig == null) {
            jdCloudSmsConfig = new JdCloudSmsConfig();
        }
        if (jdCloudSms == null) {
            jdCloudSms = new JdCloudSmsImpl(
                    jdCloudSmsConfig.client(jdCloudConfig),
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
    public static JdCloudSmsImpl refresh(JdCloudConfig jdCloudConfig) {
        if (jdCloudSmsConfig == null) {
            jdCloudSmsConfig = new JdCloudSmsConfig();
        }
        jdCloudSms = new JdCloudSmsImpl(
                jdCloudSmsConfig.client(jdCloudConfig),
                jdCloudConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime()
        );
        return jdCloudSms;
    }
}
