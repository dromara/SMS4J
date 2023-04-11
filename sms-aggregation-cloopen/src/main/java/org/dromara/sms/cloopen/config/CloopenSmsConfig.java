package org.dromara.sms.cloopen.config;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.dromara.sms.cloopen.service.CloopenSmsImpl;
import org.dromara.sms.comm.factory.BeanFactory;

/**
 * 容联云短信配置
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
public class CloopenSmsConfig {

    private static CloopenSmsImpl cloopenSms;

    private static CloopenSmsConfig cloopenSmsConfig;

    /**
     * 客户端对象
     *
     * @param cloopenConfig 容联云短信配置属性
     * @return 客户端对象
     */
    public CCPRestSmsSDK client(CloopenConfig cloopenConfig) {
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init(cloopenConfig.getServerIp(), cloopenConfig.getServerPort());
        sdk.setAccount(cloopenConfig.getAccessKeyId(), cloopenConfig.getAccessKeySecret());
        sdk.setAppId(cloopenConfig.getAppId());
        sdk.setBodyType(BodyType.Type_JSON);
        return sdk;
    }

    /**
     * 创建容联云短信实现
     */
    public static CloopenSmsImpl createCloopenSms(CloopenConfig cloopenConfig) {
        if (cloopenSmsConfig == null) {
            cloopenSmsConfig = new CloopenSmsConfig();
        }
        if (cloopenSms == null) {
            cloopenSms = new CloopenSmsImpl(
                    cloopenSmsConfig.client(cloopenConfig),
                    cloopenConfig,
                    BeanFactory.getExecutor(),
                    BeanFactory.getDelayedTime()
            );
        }
        return cloopenSms;
    }

    /**
     * 刷新对象
     */
    public static CloopenSmsImpl refresh(CloopenConfig cloopenConfig) {
        if (cloopenSmsConfig == null) {
            cloopenSmsConfig = new CloopenSmsConfig();
        }
        cloopenSms = new CloopenSmsImpl(
                cloopenSmsConfig.client(cloopenConfig),
                cloopenConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime()
        );
        return cloopenSms;
    }
}
