package sms4j.local.b;

import cn.hutool.json.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.provider.service.AbstractSmsBlend;
import sms4j.local.a.AConfig;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * 新增厂商的具体短信逻辑实现{@link SmsBlend}
 * 本类的逻辑：总是在日志中输出参数信息，并成功返回调用结果。
 *
 * @author huangchengxing
 */


@Slf4j
public class BSmsImpl extends AbstractSmsBlend<BConfig> {

    /**
     * ASmsImpl
     * <p>构造器，用于构造短信实现模块
     *
     * @author :Wind
     */
    public BSmsImpl(BConfig config, Executor pool, DelayedTime delayedTime) {
        super(config, pool, delayedTime);
    }

    /**
     * ASmsImpl
     * <p>构造器，用于构造短信实现模块
     */
    public BSmsImpl(BConfig config) {
        super(config);
    }

    @Override
    public String getSupplier() {
        return "b";
    }

    @Override
    public SmsResponse sendMessage(String phone, String message) {
        System.out.println("BBBBBBBBBBBBBBsend message: phone={"+phone+"}, message={"+message+"}" +this.getConfigId());
        return getResponse(new JSONObject()
                .set("phone", phone)
                .set("message", message));
    }

    @Override
    public SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages) {
        System.out.println("BBBBBBBBBBBBBBsend message: phone={"+phone+"}, messages={"+messages.size()+"}" );
        return getResponse(new JSONObject()
                .set("phone", phone)
                .set("messages", new JSONObject(messages)));
    }


    @Override
    public SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages) {
        System.out.println("BBBBBBBBBBBBBBsend message: phone={"+phone+"},templateId={"+templateId+"}, messages={"+(null == messages?null:messages.size())+"}" );
        return getResponse(new JSONObject()
                .set("phone", phone)
                .set("templateId", templateId)
                .set("messages", new JSONObject(messages)));
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String message) {
        System.out.println("BBBBBBBBBBBBBBsend message: phone={"+phones.size()+"}, messages={"+message+"}" );
        return getResponse(new JSONObject()
                .set("phones", phones)
                .set("message", message));
    }

    @Override
    public SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        System.out.println("BBBBBBBBBBBBBBsend message: phone={"+phones.size()+"},templateId={"+templateId+"}, messages={"+(null == messages?null:messages.size())+"}" );
        return getResponse(new JSONObject()
                .set("phones", phones)
                .set("templateId", templateId)
                .set("messages", new JSONObject(messages)));
    }



    /*
     * <p>说明：封装结果的方法（可以没有）
     * getResponse
     *
     * @param resJson      厂商的返回信息
     * @author :Wind
     * */
    private SmsResponse getResponse(JSONObject resJson) {
        SmsResponse smsResponse = new SmsResponse();
        smsResponse.setSuccess(true);
        smsResponse.setData(resJson);
        smsResponse.setConfigId(getConfigId());
        return smsResponse;
    }
}
