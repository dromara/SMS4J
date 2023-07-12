package org.dromara.sms4j.cloopen.service;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.cloopen.util.CloopenHelper;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * 容联云短信实现
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@Slf4j
public class CloopenSmsImpl extends AbstractSmsBlend {

    private final CloopenConfig config;

    public CloopenSmsImpl(CloopenConfig config, Executor pool, DelayedTime delayed) {
        super(pool,delayed);
        this.config = config;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        return massTexting(Collections.singletonList(phone), message);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return massTexting(Collections.singletonList(phone), templateId, messages);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(IdUtil.fastSimpleUUID(), message);
        return massTexting(phones, config.getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        Map<String, Object> paramMap = new LinkedHashMap<>(4);
        paramMap.put("to", String.join(",", phones));
        paramMap.put("appId", config.getAppId());
        paramMap.put("templateId", templateId);
        paramMap.put("datas", messages.keySet().stream().map(messages::get).toArray(String[]::new));
        String timestamp = DateUtil.format(DateUtil.date(), DatePattern.PURE_DATETIME_PATTERN);

        String url = String.format("%s/Accounts/%s/SMS/TemplateSMS?sig=%s",
                config.getBaseUrl(),
                config.getAccessKeyId(),
                CloopenHelper.generateSign(config.getAccessKeyId(), config.getAccessKeySecret(), timestamp));

        Map<String, String> headers = new LinkedHashMap<>(3);
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json;charset=utf-8");
        headers.put("Authorization", CloopenHelper.generateAuthorization(config.getAccessKeyId(), timestamp));
        return this.getResponse(http.postJson(url, headers, paramMap));
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess("000000".equals(resJson.getStr("statusCode")));
        smsResponse.setData(resJson);
        return smsResponse;
    }
}
