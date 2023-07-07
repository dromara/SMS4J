package org.dromara.sms4j.huawei.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.huawei.utils.HuaweiBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executor;

import static org.dromara.sms4j.huawei.utils.HuaweiBuilder.listToString;

@Slf4j
public class HuaweiSmsImpl extends AbstractSmsBlend {
    public HuaweiSmsImpl(HuaweiConfig config, Executor pool, DelayedTime delayed) {
        super(pool, delayed);
        this.config = config;
    }

    private final HuaweiConfig config;

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> mes = new LinkedHashMap<>();
        mes.put(UUID.randomUUID().toString().replaceAll("-", ""), message);
        return sendMessage(phone, config.getTemplateId(), mes);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String url = config.getUrl() + Constant.HUAWEI_REQUEST_URL;
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        String mess = listToString(list);
        String requestBody = HuaweiBuilder.buildRequestBody(config.getSender(), phone, templateId, mess, config.getStatusCallBack(), config.getSignature());
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", Constant.HUAWEI_AUTH_HEADER_VALUE);
        headers.put("X-WSSE", HuaweiBuilder.buildWsseHeader(config.getAppKey(), config.getAppSecret()));
        headers.put("Content-Type", Constant.FROM_URLENCODED);
        try(HttpResponse response = HttpRequest.post(url)
                .addHeaders(headers)
                .body(requestBody)
                .execute()){
            JSONObject body = JSONUtil.parseObj(response.body());
            return this.getResponse(body);
        }
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        return sendMessage(listToString(phones), message);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        return sendMessage(listToString(phones), templateId, messages);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess("000000".equals(resJson.getStr("Code")));
        smsResponse.setData(resJson);
        return smsResponse;
    }

}
