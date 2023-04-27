package org.dromara.sms4j.aliyun.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.service.AlibabaSmsImpl;
import org.dromara.sms4j.comm.factory.BeanFactory;


/**
 * AlibabaSmsConfig
 * <p> 阿里巴巴对象建造者
 *
 * @author :Wind
 * 2023/4/8  14:54
 **/
@Slf4j
public class AlibabaSmsConfig {

    private static AlibabaSmsImpl alibabaSms;

    private static AlibabaSmsConfig alibabaSmsConfig;

    /**
     * getAlibabaSms
     * <p> 建造一个短信实现对像
     *
     * @author :Wind
     */
    public static AlibabaSmsImpl createAlibabaSms(AlibabaConfig alibabaConfig) {
        if (alibabaSmsConfig == null) {
            alibabaSmsConfig = new AlibabaSmsConfig();
        }
        if (alibabaSms == null) {
            alibabaSms = new AlibabaSmsImpl(
                    alibabaConfig,
                    BeanFactory.getExecutor(),
                    BeanFactory.getDelayedTime());
        }
        return alibabaSms;
    }

    /**
     * refresh
     * <p> 刷新对象
     *
     * @author :Wind
     */
    public static AlibabaSmsImpl refresh(AlibabaConfig alibabaConfig) {
        // 如果配置对象为空则创建一个
        if (alibabaSmsConfig == null) {
            alibabaSmsConfig = new AlibabaSmsConfig();
        }
        //重新构造一个实现对象
        alibabaSms = new AlibabaSmsImpl(
                alibabaConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime());
        return alibabaSms;
    }

    private AlibabaSmsConfig() {
    }
}
