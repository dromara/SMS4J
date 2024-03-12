package org.dromara.sms4j.jiguang.service;

import cn.hutool.core.util.IdUtil;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.jiguang.config.JiguangConfig;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 极光短信实现
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
@Slf4j
public class JiguangSmsImpl extends AbstractSmsBlend<JiguangConfig> {

    private final SMSClient client;

    private int retry = 0;

    public JiguangSmsImpl(SMSClient client, JiguangConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
        this.client = client;
    }

    public JiguangSmsImpl(SMSClient client, JiguangConfig config) {
        super(config);
        this.client = client;
    }

    @Override
    public String getSupplier() {
        return "jiguang";
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        return massTexting(Collections.singletonList(phone), message);
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<String, String>();
        }
        return sendMessage(phone, getConfig().getTemplateId(), messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return massTexting(Collections.singletonList(phone), templateId, messages);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(IdUtil.fastSimpleUUID(), message);
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        return null;
    }

//    @Override
//    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
//        BatchSendRequest request;
//        try {
//            request = new BatchSendRequest();
//            request.setPhoneList(phones);
//            request.setRegionId(getConfig().getRegion());
//            request.setTemplateId(templateId);
//            request.setSignId(getConfig().getSignature());
//            List<String> params = messages.keySet().stream().map(messages::get)
//                    .collect(Collectors.toList());
//            request.setParams(params);
//        } catch (Exception e) {
//            throw new SmsBlendException(e.getMessage());
//        }
//
//        BatchSendResult result = client.batchSend(request).getResult();
//        SmsResponse smsResponse;
//        try {
//            smsResponse = getSmsResponse(result);
//        } catch (SmsBlendException e) {
//            smsResponse = new SmsResponse();
//            smsResponse.setSuccess(false);
//            smsResponse.setData(e.getMessage());
//        }
//        if (smsResponse.isSuccess() || retry == getConfig().getMaxRetries()) {
//            retry = 0;
//            return smsResponse;
//        }
//        return requestRetry(phones, templateId, messages);
//    }

    private SmsResponse requestRetry(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        http.safeSleep(getConfig().getRetryInterval());
        retry++;
        log.warn("短信第 {" + retry + "} 次重新发送");
        return massTexting(phones, templateId, messages);
    }

    /**
     * 获取短信返回信息
     *
     * @param res 云商原始响应信息
     * @return 发送短信返回信息
     */
//    private SmsResponse getSmsResponse(BatchSendResult res) {
//        SmsResponse smsResponse = new SmsResponse();
//        smsResponse.setSuccess(res.getStatus() != null && res.getStatus());
//        smsResponse.setData(res);
//        smsResponse.setConfigId(getConfigId());
//        return smsResponse;
//    }
}
