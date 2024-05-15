package org.dromara.sms4j.local;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * 用于测试的{@link SmsBlend}实现，
 * 总是在日志中输出参数信息，并成功返回调用结果。
 *
 * @author huangchengxing
 * @see SupplierConstant#LOCAL
 */
@Slf4j
public class LocalSmsImpl implements SmsBlend {

    @Override
    public String getConfigId() {
        return SupplierConstant.LOCAL;
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.LOCAL;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        log.info("send message: phone={}, message={}", phone, message);
        return getResponse(new JSONObject()
            .set("phone", phone)
            .set("message", message));
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        log.info("send message: phone={}, messages={}", phone, messages);
        return getResponse(new JSONObject()
            .set("phone", phone)
            .set("messages", new JSONObject(messages)));
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        log.info("send message: phone={}, templateId={}, messages={}", phone, templateId, messages);
        return getResponse(new JSONObject()
            .set("phone", phone)
            .set("templateId", templateId)
            .set("messages", new JSONObject(messages)));
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        log.info("mass texting: phones={}, message={}", phones, message);
        return getResponse(new JSONObject()
            .set("phones", phones)
            .set("message", message));
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        log.info("mass texting: phones={}, templateId={}, messages={}", phones, templateId, messages);
        return getResponse(new JSONObject()
            .set("phones", phones)
            .set("templateId", templateId)
            .set("messages", new JSONObject(messages)));
    }

    @Override
    public void sendMessageAsync(String phone, String message, CallBack callBack) {
        log.info("send message asynchronously: phone={}, message={}", phone, message);
        // do nothing
        callBack.callBack(getResponse(new JSONObject()
            .set("phone", phone)
            .set("message", message)));
    }

    @Override
    public void sendMessageAsync(String phone, String message) {
        log.info("send message asynchronously: phone={}, message={}", phone, message);
        // do nothing
    }

    @Override
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack) {
        log.info("send message asynchronously: phone={}, templateId={}, messages={}", phone, templateId, messages);
        // do nothing
        callBack.callBack(getResponse(new JSONObject()
            .set("phone", phone)
            .set("templateId", templateId)
            .set("messages", new JSONObject(messages))));
    }

    @Override
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages) {
        log.info("send message asynchronously: phone={}, templateId={}, messages={}", phone, templateId, messages);
        // do nothing
    }

    @Override
    public void delayedMessage(String phone, String message, Long delayedTime) {
        log.info("delayed message: phone={}, message={}, delayedTime={}", phone, message, delayedTime);
    }

    @Override
    public void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        log.info("delayed message: phone={}, templateId={}, messages={}, delayedTime={}", phone, templateId, messages, delayedTime);
    }

    @Override
    public void delayMassTexting(List<String> phones, String message, Long delayedTime) {
        log.info("delayed mass texting: phones={}, message={}, delayedTime={}", phones, message, delayedTime);
    }

    @Override
    public void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {
        log.info("delayed mass texting: phones={}, templateId={}, messages={}, delayedTime={}", phones, templateId, messages, delayedTime);
    }

    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(true);
        smsResponse.setData(resJson);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }
}
