package org.dromara.sms4j.jiguang.service;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jdcloud.sdk.service.sms.model.BatchSendResult;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.jiguang.config.JiguangConfig;
import org.dromara.sms4j.jiguang.util.JiGuangHelper;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * <p>类名: JiguangSmsImpl
 * <p>说明：极光 sms
 *
 * @author :SmartFire
 * 2024/3/15
 **/
@Slf4j
public class JiguangSmsImpl extends AbstractSmsBlend<JiguangConfig> {

    private JiguangConfig config;
    private int retry = 0;


    public JiguangSmsImpl(JiguangConfig config, Executor pool, DelayedTime delayed) {
        super(config, pool, delayed);
        this.config = getConfig();
    }

    public JiguangSmsImpl(JiguangConfig config) {
        super(config);
        this.config = getConfig();
    }

    @Override
    public String getSupplier() {
        return SupplierConstant.JIGUANG;
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        SmsResponse response;
        JiGuangHelper helper = new JiGuangHelper(getConfig(), http);
        String url = String.format("%s/%s", config.getRequestUrl(), config.getCodeAction());
        String authCode = this.getBasicAuthorization(config.getAppKey(),config.getMasterSecret());
        //{"mobile":"xxxxxxxxxxx","sign_id":*,"temp_id":*}
        Map map= new HashMap<>();
        map.put("mobile", phone);
        map.put("temp_id", config.getTemplateId());
        map.put("sign_id", config.getSignId());

        String json = JSONUtil.toJsonStr(map);
        response = helper.smsResponse(json, url, authCode, "msg_id");
        return response;
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        if (Objects.isNull(messages)){
            messages = new LinkedHashMap<String, String>();
        }
        SmsResponse response;
        JiGuangHelper helper = new JiGuangHelper(getConfig(), http);
        String url = String.format("%s/%s", config.getRequestUrl(), config.getSingleTemplateAction());
        String authCode = this.getBasicAuthorization(config.getAppKey(),config.getMasterSecret());
        //{"mobile":"xxxxxxxxxxxxxx","sign_id":*,"temp_id":1,"temp_para":{"xxxx":"xxxx"}}

        JSONObject temp_para = new JSONObject();
        Optional<String> codekey = messages.keySet().stream().findFirst();

        LinkedHashMap<String, String> finalMessages = messages;
        codekey.ifPresent(key -> {
            temp_para.put(key, finalMessages.get(key));
        });

        Map map= new HashMap<>();
        map.put("mobile", phone);
        map.put("temp_id", config.getTemplateId());
        map.put("sign_id", config.getSignId());
        map.put("temp_para", temp_para);

        String json = JSONUtil.toJsonStr(map);
        response = helper.smsResponse(json, url, authCode, "msg_id");
        return response;
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        SmsResponse response;
        JiGuangHelper helper = new JiGuangHelper(getConfig(), http);
        String url = String.format("%s/%s", config.getRequestUrl(), config.getSingleTemplateAction());
        String authCode = this.getBasicAuthorization(config.getAppKey(),config.getMasterSecret());
        //{"mobile":"xxxxxxxxxxxxxx","sign_id":*,"temp_id":1,"temp_para":{"xxxx":"xxxx","xxxx":"xxxx"}}

        JSONObject temp_para = new JSONObject();

        for (Map.Entry<String, String> entry : messages.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            temp_para.put(key, value);
            System.out.println(key + ": " + value);
        }

        Map map= new HashMap<>();
        map.put("mobile", phone);
        map.put("temp_id", templateId);
        map.put("sign_id", config.getSignId());
        map.put("temp_para", temp_para);

        String json = JSONUtil.toJsonStr(map);
        response = helper.smsResponse(json, url, authCode, "msg_id");
        return response;
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put(IdUtil.fastSimpleUUID(), message);
        return massTexting(phones, getConfig().getTemplateId(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId,  LinkedHashMap<String, String> messages){
        SmsResponse response;
        JiGuangHelper helper = new JiGuangHelper(getConfig(), http);
        String url = String.format("%s/%s", config.getRequestUrl(), config.getBatchTemplateAction());
        String authCode = this.getBasicAuthorization(config.getAppKey(),config.getMasterSecret());

        List recipients = new ArrayList();
        JSONObject temp_para;
        JSONObject recipient;
        for (String phone : phones) {
            temp_para = new JSONObject();
            recipient = new JSONObject();
            recipient.put("mobile", phone);
            for (Map.Entry<String, String> entry : messages.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                temp_para.put(key, value);
                System.out.println(key + ": " + value);
            }
            recipient.put("temp_para", temp_para);
            recipients.add(recipient);
        }

        Map map= new HashMap<>();
        map.put("temp_id", templateId);
        map.put("sign_id", config.getSignId());
        map.put("tag", "标签");
        map.put("recipients", recipients);

        String json = JSONUtil.toJsonStr(map);
        response = helper.smsResponse(json, url, authCode, "success_count");
        return response;
    }



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
    private SmsResponse getSmsResponse(BatchSendResult res) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(res.getStatus() != null && res.getStatus());
        smsResponse.setData(res);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }

    private String getBasicAuthorization(String username, String password) {
        String encodeKey = username + ":" + password;
        return "Basic " + String.valueOf(Base64.encode(encodeKey));
    }
}
