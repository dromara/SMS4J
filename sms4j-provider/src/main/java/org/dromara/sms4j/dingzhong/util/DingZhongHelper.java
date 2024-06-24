package org.dromara.sms4j.dingzhong.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.api.utils.SmsRespUtils;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsHttpUtils;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.dingzhong.config.DingZhongConfig;

import java.util.Map;

/**
 * 鼎众 Helper
 *
 * @author sh1yu
 * @since 2023/12/26 20:57
 */
@Slf4j
public class DingZhongHelper {

    private final DingZhongConfig config;
    private final SmsHttpUtils http;
    private int retry = 0;

    public DingZhongHelper(DingZhongConfig config, SmsHttpUtils http) {
        this.config = config;
        this.http = http;
    }

    public SmsResponse smsResponse(Map<String, Object> paramMap) {
        String url = String.format("%s/%s", config.getRequestUrl(), SmsUtils.isEmpty(paramMap.get("templateId"))?config.getBaseAction():config.getTemplateAction());
        Map<String, String> headers = MapUtil.newHashMap(2, true);
        headers.put(Constant.ACCEPT, Constant.APPLICATION_JSON);
        headers.put(Constant.CONTENT_TYPE, Constant.APPLICATION_FROM_URLENCODED);
        SmsResponse smsResponse;
        try {
            smsResponse = getResponse(http.postFrom(url, headers, paramMap));
        } catch (SmsBlendException e) {
            smsResponse = SmsRespUtils.error(e.message, config.getConfigId());
        }
        if (smsResponse.isSuccess() || retry == config.getMaxRetries()) {
            retry = 0;
            return smsResponse;
        }
        return requestRetry(paramMap);

    }

    private SmsResponse requestRetry(Map<String, Object> paramMap) {
        http.safeSleep(config.getRetryInterval());
        retry++;
        log.warn("短信第 {} 次重新发送", retry);
        return smsResponse(paramMap);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        return SmsRespUtils.resp(resJson, "0".equals(resJson.getStr("resCode")), config.getConfigId());
    }
}
