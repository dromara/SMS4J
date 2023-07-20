package org.dromara.sms4j.jdcloud.service;

import cn.hutool.core.util.IdUtil;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import com.jdcloud.sdk.service.sms.model.BatchSendRequest;
import com.jdcloud.sdk.service.sms.model.BatchSendResult;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.AbstractSmsBlend;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 京东云短信实现
 *
 * @author Charles7c
 * @since 2023/4/10 20:01
 */
@Slf4j
public class JdCloudSmsImpl extends AbstractSmsBlend {

    private final SmsClient client;

    private final JdCloudConfig config;

    public JdCloudSmsImpl(SmsClient client, JdCloudConfig config, Executor pool, DelayedTime delayed) {
        super(pool, delayed);
        this.client = client;
        this.config = config;
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String message) {
        return massTexting(Collections.singletonList(phone), message);
    }

    @Override
    @Restricted
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        return massTexting(Collections.singletonList(phone), templateId, messages);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(IdUtil.fastSimpleUUID(), message);
        return massTexting(phones, config.getTemplateId(), map);
    }

    @Override
    @Restricted
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        try {
            BatchSendRequest request = new BatchSendRequest();
            request.setPhoneList(phones);
            request.setRegionId(config.getRegion());
            request.setTemplateId(templateId);
            request.setSignId(config.getSignature());
            List<String> params = messages.keySet().stream().map(messages::get)
                    .collect(Collectors.toList());
            request.setParams(params);

            BatchSendResult result = client.batchSend(request).getResult();
            return getSmsResponse(result);
        } catch (Exception e) {
            throw new SmsBlendException(e.getMessage());
        }
    }

    /**
     * 获取短信返回信息
     *
     * @param res 云商原始响应信息
     * @return 发送短信返回信息
     */
    private SmsResponse getSmsResponse(BatchSendResult res) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(res.getStatus() != null && res.getStatus());
        smsResponse.setData(res);
        smsResponse.setConfigId(this.config.getConfigId());
        return smsResponse;
    }
}
