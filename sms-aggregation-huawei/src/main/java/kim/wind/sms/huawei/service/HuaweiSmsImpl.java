package kim.wind.sms.huawei.service;

import kim.wind.sms.api.SmsBlend;
import kim.wind.sms.api.callback.CallBack;
import kim.wind.sms.comm.delayedTime.DelayedTime;
import kim.wind.sms.comm.entity.SmsResponse;
import kim.wind.sms.comm.utils.HTTPUtils;
import kim.wind.sms.comm.utils.http.OKResponse;
import kim.wind.sms.huawei.config.HuaweiSmsConfig;
import kim.wind.sms.huawei.constant.Constant;
import kim.wind.sms.huawei.entity.HuaweiError;
import kim.wind.sms.huawei.entity.HuaweiResponse;
import kim.wind.sms.huawei.entity.SmsId;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@EnableConfigurationProperties({HuaweiSmsConfig.class})
@Slf4j
public class HuaweiSmsImpl implements SmsBlend {

    @Autowired
    private HuaweiSmsConfig config;

    @Autowired
    @Qualifier("smsExecutor")
    private Executor pool;

    @Autowired
    private DelayedTime delayed;

    @Autowired
    private HTTPUtils http ;

    @Override
    public SmsResponse sendMessage(String phone, String message) {


        return null;
    }

    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        List<String> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : messages.entrySet()) {
            list.add(entry.getValue());
        }
        String mess = HuaweiBuilder.listToString(list);
        String requestBody = HuaweiBuilder.buildRequestBody(config.getSender(), phone, config.getTemplateId(), mess, config.getStatusCallBack(), config.getSignature());
        Map<String,String> headers = new LinkedHashMap<>();
        headers.put("Authorization",Constant.AUTH_HEADER_VALUE);
        headers.put("X-WSSE",HuaweiBuilder.buildWsseHeader(config.getAppKey(), config.getAppSecret()));
        OKResponse response = http.setBaseURL(config.getUrl()).builder()
                .setMediaType(Constant.CONTENT_TYPE)
                .headers(headers)
                .post(Constant.REQUEST_URL, requestBody)
                .sync();
        HuaweiResponse jsonBody = response.getJSONBody(HuaweiResponse.class);
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setCode(response.getCode());
        smsResponse.setMessage(jsonBody.getDescription());
        SmsId smsId = jsonBody.getSmsId().get(0);
        smsResponse.setBizId(smsId.getSmsMsgId());
        smsResponse.setData(jsonBody);
       if (response.getCode() != 200){
           smsResponse.setErrMessage(HuaweiError.getValue(smsId.getStatus()));
       }
        return smsResponse;
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        return null;
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        return null;
    }

    @Override
    public void sendMessageAsync(String phone, String message, CallBack callBack) {

    }

    @Override
    public void sendMessageAsync(String phone, String message) {

    }

    @Override
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack) {

    }

    @Override
    public void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages) {

    }

    @Override
    public void delayedMessage(String phone, String message, Long delayedTime) {

    }

    @Override
    public void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {

    }

    @Override
    public void delayMassTexting(List<String> phones, String message, Long delayedTime) {

    }

    @Override
    public void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime) {

    }
}
