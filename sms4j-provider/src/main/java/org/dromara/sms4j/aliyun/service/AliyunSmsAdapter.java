package org.dromara.sms4j.aliyun.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.aliyun.utils.AliyunUtils;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.enumerate.SupplierType;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.core.AbstractSmsAdapter;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * <p>类名: AlibabaSmsImpl
 * <p>说明：  阿里云短信实现
 *
 * @author :Wind
 * 2023/3/26  17:16
 **/

@Slf4j
public class AliyunSmsAdapter extends AbstractSmsAdapter<AlibabaConfig> {

    /**
     * AlibabaSmsImpl
     * <p>构造器，用于构造短信实现模块
     *
     * @author :Wind
     */
    public AliyunSmsAdapter(AlibabaConfig config) {
        super(config);
    }

    // TODO 这些看能否统一抽出来，最好达到的效果是大家只要实现getSmsResponse方法就可以了
    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(config.getTemplateName(), message);
        return sendMessage(phone, config.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSON.toJSONString(messages);
        return getSmsResponse(phone, messageStr, templateId);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(config.getTemplateName(), message);
        return massTexting(phones, config.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        String messageStr = JSON.toJSONString(messages);
        return getSmsResponse(arrayToString(phones), messageStr, templateId);
    }

    private SmsResponse getSmsResponse(String phone, String message, String templateId) {
        SmsResponse smsResponse = new SmsResponse();
        String requestUrl;
        String paramStr;
        try {
            requestUrl = AliyunUtils.generateSendSmsRequestUrl(this.config, message, phone, templateId);
            paramStr = AliyunUtils.generateParamBody(config, phone, message, templateId);
        } catch (Exception e) {
            log.error("aliyun send message error", e);
            throw new SmsBlendException(e.getMessage());
        }
        log.info("requestUrl {}", requestUrl);
        http.post(requestUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addBody(paramStr)
                .onSuccess(((data, req, res) -> {
                    JSONObject jsonBody = res.get(JSONObject.class);
                    log.info(jsonBody.toJSONString());
                }))
                .onError((ex, req, res) -> {
                    JSONObject jsonBody = res.get(JSONObject.class);
                    log.info(jsonBody.toJSONString());
                })
                .execute();
        return smsResponse;
    }

    private String arrayToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String s : list) {
            sb.append(",").append("+86").append(s);
        }
        return sb.substring(1);
    }

    @Override
    protected SupplierType type() {
        return SupplierType.ALIBABA;
    }

}
