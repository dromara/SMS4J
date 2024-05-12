package org.dromara.sms4j.jiguang.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsHttpUtils;
import org.dromara.sms4j.jiguang.config.JiguangConfig;

import java.util.Map;

/**
 * <p>类名: JiGuangHelper
 * <p>说明：极光 sms
 *
 * @author :SmartFire
 * 2024/3/15
 **/
@Slf4j
public class JiGuangHelper {

    private final JiguangConfig config;
    private final SmsHttpUtils http;
    private int retry = 0;

    public JiGuangHelper(JiguangConfig config, SmsHttpUtils http) {
        this.config = config;
        this.http = http;
    }

    public SmsResponse smsResponse(String jsonMessage, String url, String authCode, String jsonKey) {
        Map<String, String> headers = MapUtil.newHashMap(2, true);
        headers.put("Accept", Constant.ACCEPT);
        headers.put("Content-Type", Constant.APPLICATION_JSON_UTF8);
        headers.put("Authorization", authCode);
        SmsResponse smsResponse = null;
        try {
            smsResponse = getResponse(http.postJson(url, headers, jsonMessage), jsonKey);
        } catch (SmsBlendException e) {
            smsResponse = new SmsResponse();
            smsResponse.setSuccess(false);
            smsResponse.setData(e.getMessage());
        }
        if (smsResponse.isSuccess() || retry == config.getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(jsonMessage, url, authCode, jsonKey);

    }

    private SmsResponse requestRetry(String jsonMessage, String url, String authCode, String jsonKey) {
        http.safeSleep(config.getRetryInterval());
        retry++;
        log.warn("短信第 {} 次重新发送", retry);
        return smsResponse(jsonMessage, url, authCode, jsonKey);
    }

    private SmsResponse getResponse(JSONObject resJson, String jsonKey ) {
        SmsResponse smsResponse = new SmsResponse();
        String value = resJson.getStr(jsonKey);
        if (StrUtil.isNotEmpty(value)) {
            smsResponse.setSuccess(true);
        }
        smsResponse.setData(resJson);
        smsResponse.setConfigId(this.config.getConfigId());
        return smsResponse;
    }
}
