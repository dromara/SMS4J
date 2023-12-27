package org.dromara.sms4j.dingzhong.service;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.dromara.sms4j.dingzhong.config.DingZhongConfig;
import org.dromara.sms4j.dingzhong.util.DingZhongHelper;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * <p>类名: DingZhongSmsImpl
 * <p>说明：  鼎众短信实现
 *
 * @author :Sh1yu
 * 2023/12/26  17:16
 **/
@Slf4j
public class DingZhongSmsImpl extends AbstractSmsBlend<DingZhongConfig> {


    /**
     * DingZhongSmsImpl
     * <p>构造器，用于构造短信实现模块
     *
     * @author :Sh1yu
     */
    public DingZhongSmsImpl(DingZhongConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    /**
     * DingZhongSmsImpl
     * <p>构造器，用于构造短信实现模块
     */
    public DingZhongSmsImpl(DingZhongConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.DINGZHONG;
    }

    public SmsResponse temp(){
        DingZhongHelper helper = new DingZhongHelper(getConfig(), http);
        Map<String, Object> paramMap = new LinkedHashMap<>(3);
        paramMap.put("cdkey", getConfig().getAccessKeyId());
        paramMap.put("password", getConfig().getAccessKeySecret());
        paramMap.put("content", "你正在申请手机注册，验证码为：<%code%>，5分钟内有效！");
        return helper.smsResponse(paramMap);
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        DingZhongHelper helper = new DingZhongHelper(getConfig(), http);
        Map<String, Object> paramMap = new LinkedHashMap<>(4);
        paramMap.put("cdkey", getConfig().getAccessKeyId());
        paramMap.put("password", getConfig().getAccessKeySecret());
        paramMap.put("mobile", phone);
        paramMap.put("msg", message);
        return helper.smsResponse(paramMap);
        //return temp();
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        return sendMessage(phone,getConfig().getTemplateId(),messages);
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        DingZhongHelper helper = new DingZhongHelper(getConfig(), http);
        Map<String, Object> paramMap = new LinkedHashMap<>(5);
        paramMap.put("cdkey", getConfig().getAccessKeyId());
        paramMap.put("password", getConfig().getAccessKeySecret());
        paramMap.put("mobile", phone);
        paramMap.put("templateId", templateId);
        JSONObject params = new JSONObject();
        params.putAll(messages);
        paramMap.put("msgParam", params.toString());
        return helper.smsResponse(paramMap);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
       return sendMessage(SmsUtils.arrayToString(phones),message);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
       return sendMessage(SmsUtils.arrayToString(phones),templateId,messages);
    }
}