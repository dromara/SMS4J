package org.dromara.sms4j.yunpian.config;

import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.yunpian.service.YunPianSmsImpl;

public class YunPianSmsConfig {

    private static YunPianSmsImpl yunpianSmsImpl;

    private static YunPianSmsConfig yunPianSmsConfig;

    private YunPianSmsConfig() {
    }

    /**
     * 建造一个云片短信实现
     */
    public static YunPianSmsImpl createTencentSms(YunpianConfig yunpianConfig){
        if (yunPianSmsConfig == null) {
            yunPianSmsConfig = new YunPianSmsConfig();
        }
        if (yunpianSmsImpl == null){
            yunpianSmsImpl = new YunPianSmsImpl(
                    BeanFactory.getExecutor(),
                    BeanFactory.getDelayedTime(),
                    yunpianConfig
                    );
        }
        return yunpianSmsImpl;
    }

    /** 刷新对象*/
    public static YunPianSmsImpl refresh(YunpianConfig yunpianConfig){
        yunpianSmsImpl = new YunPianSmsImpl(
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime(),
                yunpianConfig
        );
        return yunpianSmsImpl;
    }
}
