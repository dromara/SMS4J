package org.dromara.sms4j.huyi.util;

import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.huyi.config.HuYiConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * 互亿工具类
 *
 * @author 初心
 * 2025/7/5
 */
public class HuYiUtils {


    /**
     * 获取请求头
     *
     * @param huYiConfig 互亿无限配置
     * @return 请求头
     */
    public static Map<String, String> getParams(HuYiConfig huYiConfig) {
        Map<String, String> params = new HashMap<>();
        params.put(Constant.CONTENT_TYPE, "application/x-www-form-urlencoded");
        params.put("format", "json");
        params.put("account", huYiConfig.getAccessKeyId());
        params.put("password", huYiConfig.getAccessKeySecret());
        return params;
    }
}
