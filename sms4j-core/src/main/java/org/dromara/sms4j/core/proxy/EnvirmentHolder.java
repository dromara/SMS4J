package org.dromara.sms4j.core.proxy;

import org.dromara.sms4j.provider.config.SmsConfig;

import java.util.Map;

/**
 * 环境信息持有
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public class EnvirmentHolder {
    private static SmsConfig smsConfig = null;
    private static Map<String, Map<String, Object>> blends = null;

    public static void frozenEnvirmet(SmsConfig smsConfig, Map<String, Map<String, Object>> blends) {
        if (null!=EnvirmentHolder.smsConfig||null!=EnvirmentHolder.blends){
            return;
        }
        EnvirmentHolder.smsConfig = smsConfig;
        EnvirmentHolder.blends = blends;
    }

    //只有核心包执行器部分才能获取
    static SmsConfig getSmsConfig() {
        return smsConfig;
    }

    //只有核心包执行器部分才能获取
    static Map<String, Map<String, Object>> getBlends() {
        return blends;
    }
}
