package org.dromara.sms4j.cloopen.service;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.cloopen.config.CloopenConfig;
import org.dromara.sms4j.cloopen.util.CloopenHelper;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * 容联云短信实现
 *
 * @author Charles7c
 * @since 2023/4/10 22:10
 */
@Slf4j
public class CloopenSmsImpl extends AbstractSmsBlend<CloopenConfig> {

    public CloopenSmsImpl(CloopenConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
    }

    public CloopenSmsImpl(CloopenConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.CLOOPEN;
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
        LinkedHashMap<String, String> map = new LinkedHashMap<>(1);
        map.put(IdUtil.fastSimpleUUID(), message);
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        CloopenHelper helper = new CloopenHelper(getConfig(), http);
        Map<String, Object> paramMap = MapUtil.newHashMap(4, true);
        paramMap.put("to", String.join(",", phones));
        paramMap.put("appId", getConfig().getSdkAppId());
        paramMap.put("templateId", templateId);
        paramMap.put("datas", messages.keySet().stream().map(messages::get).toArray(String[]::new));
        return helper.smsResponse(paramMap);
    }
}
