package org.dromara.sms4j.jdcloud.service;

import cn.hutool.core.util.IdUtil;
import com.jdcloud.sdk.service.sms.client.SmsClient;
import com.jdcloud.sdk.service.sms.model.BatchSendRequest;
import com.jdcloud.sdk.service.sms.model.BatchSendResult;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.jdcloud.config.JdCloudConfig;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

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
public class JdCloudSmsImpl extends AbstractSmsBlend<JdCloudConfig> {

    public static final String SUPPLIER = "jdCloud";

    private final SmsClient client;

    public JdCloudSmsImpl(SmsClient client, JdCloudConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
        this.client = client;
    }

    public JdCloudSmsImpl(SmsClient client, JdCloudConfig config) {
        super(config);
        this.client = client;
    }

    @Override
    public String getSupplier() {
        return SUPPLIER;
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
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(IdUtil.fastSimpleUUID(), message);
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        try {
            BatchSendRequest request = new BatchSendRequest();
            request.setPhoneList(phones);
            request.setRegionId(getConfig().getRegion());
            request.setTemplateId(templateId);
            request.setSignId(getConfig().getSignature());
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
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }
}
