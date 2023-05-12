package org.dromara.sms4j.ctyun.config;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.factory.BeanFactory;
import org.dromara.sms4j.ctyun.service.CtyunSmsImpl;

/**
 * <p>类名: CtyunSmsConfig
 * <p>说明： 天翼云 云通信短信配置器
 *
 * @author :bleachhtred
 * 2023/5/12  15:06
 **/
@Slf4j
public class CtyunSmsConfig {

    private static CtyunSmsImpl ctyunSms;

    private static CtyunSmsConfig ctyunSmsConfig;

    /**
     * getCtyunSms
     * <p> 建造一个短信实现对像
     *
     * @author :bleachhtred
     */
    public static CtyunSmsImpl createCtyunSms(CtyunConfig ctyunConfig) {
        if (ctyunSmsConfig == null) {
            ctyunSmsConfig = new CtyunSmsConfig();
        }
        if (ctyunSms == null) {
            ctyunSms = new CtyunSmsImpl(
                    ctyunConfig,
                    BeanFactory.getExecutor(),
                    BeanFactory.getDelayedTime());
        }
        return ctyunSms;
    }

    /**
     * refresh
     * <p> 刷新对象
     *
     * @author :bleachhtred
     */
    public static CtyunSmsImpl refresh(CtyunConfig ctyunConfig) {
        // 如果配置对象为空则创建一个
        if (ctyunSmsConfig == null) {
            ctyunSmsConfig = new CtyunSmsConfig();
        }
        //重新构造一个实现对象
        ctyunSms = new CtyunSmsImpl(
                ctyunConfig,
                BeanFactory.getExecutor(),
                BeanFactory.getDelayedTime());
        return ctyunSms;
    }

    private CtyunSmsConfig() {
    }
}
