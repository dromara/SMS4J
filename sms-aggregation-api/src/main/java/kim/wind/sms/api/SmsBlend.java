package kim.wind.sms.api;

import kim.wind.sms.comm.entity.SmsResponse;

import java.util.LinkedHashMap;
import java.util.List;

public interface SmsBlend {

    /**
     * <p>说明：发送固定消息模板短信
     * <p>此方法将使用配置文件中预设的短信模板进行短信发送
     * <p>该方法指定的模板变量只能存在一个（配置文件中）
     * <p>如使用的是腾讯的短信，参数字符串中可以同时存在多个参数，使用 & 分隔例如：您的验证码为{1}在{2}分钟内有效，可以传为  message="xxxx"+"&"+"5"
     * sendMessage
     * @param phone 接收短信的手机号
     * message 消息内容
     * @author :Wind
     */
    SmsResponse sendMessage(String phone,String message);

    /**
     * <p>说明：使用自定义模板发送短信
     * sendMessage
     * @param templateId 模板id
     * @param messages key为模板变量名称 value为模板变量值
     * @author :Wind
     */
    SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String,String> messages);

    /**
     * <p>说明：群发固定模板短信
     * massTexting
     * @author :Wind
     */
    SmsResponse massTexting(List<String> phones, String message);

    /**
     * <p>说明：使用自定义模板群发短信
     * massTexting
     * @author :Wind
     */
    SmsResponse massTexting(List<String> phones,String templateId, LinkedHashMap<String, String> messages);

    void sendMessageAsync(String phone,String message);
}
