package org.dromara.sms4j.jiguang.service;

import cn.hutool.core.util.IdUtil;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.SendSMSResult;
import cn.jsms.api.common.SMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.jdcloud.sdk.service.sms.model.BatchSendResult;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.aliyun.config.AlibabaConfig;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.constant.SupplierConstant;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.jiguang.config.JiguangConfig;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;

import java.util.*;
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
        return massTexting(phones, getConfig().getTemplateid(), map);
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateid,  LinkedHashMap<String, String> messages) {
        SmsResponse response = new SmsResponse();
        Optional<String> message = messages.keySet()
                .stream().findFirst();
        try {//构建发送短信
            SMSPayload payload = SMSPayload.newBuilder()
                    .setMobileNumber(phones.get(0)) // 手机号码
                    .setTempId(Integer.valueOf(templateid))            // 短信模板ID 需要自行申请 模板id为：1的则自带验证码模板id
                    .addTempPara("code", "123456")  // key模板参数value：参数值  您的手机验证码：{{code}}，有效期5分钟，请勿泄露。如非本人操作，请忽略此短信。谢谢！
                    .setSignId(Integer.valueOf(getConfig().getSignid()))// 签名id 需要自行申请审核。个人也可以申请
                    .build();

            //发送短信 会返回msg_id
            SendSMSResult res = this.client.sendTemplateSMS(payload);
            if(res != null && res.isResultOK()){
                response.setSuccess(true);
            }else{
                response.setSuccess(false);
            }
            //执行业务/
            //指向保存短信发送记录业务逻辑 可以直接扔到MQ
            /**
             * 第一个参数极光返回的消息id
             * 第二个发送的手机号
             * 第三个发送内容
             * 第四个发送时间
             * 保存到DB
             */
            //insertSendSmsLog(res.getMessageId(),phoneNumber,code,0,System.currentTimeMillis()/1000);
            //执行业务/

        } catch (APIConnectionException e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
            response.setSuccess(false);
        } catch (APIRequestException e) {
            log.error(e.getStackTrace().toString());
            e.printStackTrace();
            response.setSuccess(false);
        }
        return response;
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
    private SmsResponse getSmsResponse(BatchSendResult res) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(res.getStatus() != null && res.getStatus());
        smsResponse.setData(res);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }
//    private SmsResponse getSmsResponse(BatchSendResult res) {
//        SmsResponse smsResponse = new SmsResponse();
//        smsResponse.setSuccess(res.getStatus() != null && res.getStatus());
//        smsResponse.setData(res);
//        smsResponse.setConfigId(getConfigId());
//        return smsResponse;
//    }
}
