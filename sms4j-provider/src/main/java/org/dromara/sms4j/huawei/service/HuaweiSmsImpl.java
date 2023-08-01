package org.dromara.sms4j.huawei.service;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.Constant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.huawei.config.HuaweiConfig;
import org.dromara.sms4j.huawei.utils.HuaweiBuilder;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.*;
import java.util.concurrent.Executor;

import static org.dromara.sms4j.huawei.utils.HuaweiBuilder.listToString;

@Slf4j
public class HuaweiSmsImpl extends AbstractSmsBlend<HuaweiConfig> {

    public static final String SUPPLIER = "huawei";

    public HuaweiSmsImpl(HuaweiConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
    }

    public HuaweiSmsImpl(HuaweiConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SUPPLIER;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> mes = new LinkedHashMap<>();
        mes.put(UUID.randomUUID().toString().replaceAll("-", ""), message);
        return sendMessage(phone, getConfig().getTemplateId(), mes);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String url = getConfig().getUrl() + Constant.HUAWEI_REQUEST_URL;
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        String mess = listToString(list);
        String requestBody = HuaweiBuilder.buildRequestBody(getConfig().getSender(), phone, templateId, mess, getConfig().getStatusCallBack(), getConfig().getSignature());
        Map<String, String> headers = new LinkedHashMap<>();
        headers.put("Authorization", Constant.HUAWEI_AUTH_HEADER_VALUE);
        headers.put("X-WSSE", HuaweiBuilder.buildWsseHeader(getConfig().getAccessKeyId(), getConfig().getAccessKeySecret()));
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
    public SmsResponse massTexting(List<String> phones, String message) {
        return sendMessage(listToString(phones), message);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        return sendMessage(listToString(phones), templateId, messages);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess("000000".equals(resJson.getStr("Code")));
        smsResponse.setData(resJson);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }

}
