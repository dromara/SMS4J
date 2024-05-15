package org.dromara.sms4j.api;

import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * SmsBlend
 * <p> 通用接口，定义国内短信方法
 *
 * @author :Wind
 * 2023/5/16  16:03
 **/
public interface SmsBlend {

    /**
     * 获取短信实例唯一标识
     *
     * @return
     */
    String getConfigId();

    /**
     * 获取供应商标识
     *
     * @return
     */
    String getSupplier();

    /**
     * <p>说明：发送固定消息模板短信
     * <p>此方法将使用配置文件中预设的短信模板进行短信发送
     * <p>该方法指定的模板变量只能存在一个（配置文件中）
     * <p>如使用的是腾讯的短信，参数字符串中可以同时存在多个参数，使用 & 分隔例如：您的验证码为{1}在{2}分钟内有效，可以传为  message="xxxx"+"&"+"5"
     * sendMessage
     *
     * @param phone 接收短信的手机号
     *              message 消息内容
     * @author :Wind
     */
    SmsResponse sendMessage(String phone, String message);

    /**
     *  sendMessage
     * <p>说明：发送固定消息模板多模板参数短信
     * @param  phone 接收短信的手机号
     * @param messages 模板内容
     * @author :Wind
    */
    SmsResponse sendMessage(String phone, LinkedHashMap<String, String> messages);

    /**
     * <p>说明：使用自定义模板发送短信
     * sendMessage
     *
     * @param templateId 模板id
     * @param messages   key为模板变量名称 value为模板变量值
     * @author :Wind
     */
    SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages);

    /**
     * <p>说明：群发固定模板短信
     * massTexting
     *
     * @author :Wind
     */
    SmsResponse massTexting(List<String> phones, String message);

    /**
     * <p>说明：使用自定义模板群发短信
     * massTexting
     *
     * @author :Wind
     */
    SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages);

    /**
     * <p>说明：异步短信发送，固定消息模板短信
     * sendMessageAsync
     *
     * @param phone    要发送的号码
     * @param message  发送内容
     * @param callBack 回调
     * @author :Wind
     */
    void sendMessageAsync(String phone, String message, CallBack callBack);

    /**
     * <p>说明：异步发送短信，不关注发送结果
     * sendMessageAsync
     *
     * @param phone   要发送的号码
     * @param message 发送内容
     * @author :Wind
     */
    void sendMessageAsync(String phone, String message);

    /**
     * <p>说明：异步短信发送，使用自定义模板发送短信
     * sendMessage
     *
     * @param templateId 模板id
     * @param messages   key为模板变量名称 value为模板变量值
     * @param callBack   回调
     * @author :Wind
     */
    void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack);

    /**
     * <p>说明：异步短信发送，使用自定义模板发送短信，不关注发送结果
     * sendMessageAsync
     *
     * @param templateId 模板id
     * @param messages   key为模板变量名称 value为模板变量值
     * @author :Wind
     */
    void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages);

    /**
     * <p>说明：使用固定模板发送延时短信
     * delayedMessage
     *
     * @param phone       接收短信的手机号
     * @param message     要发送的短信
     * @param delayedTime 延迟时间
     * @author :Wind
     */
    void delayedMessage(String phone, String message, Long delayedTime);

    /**
     * <p>说明：使用自定义模板发送定时短信 sendMessage
     * delayedMessage
     *
     * @param templateId  模板id
     * @param messages    key为模板变量名称 value为模板变量值
     * @param phone       要发送的手机号
     * @param delayedTime 延迟的时间
     * @author :Wind
     */
    void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime);

    /**
     * <p>说明：群发延迟短信
     * delayMassTexting
     *
     * @param phones 要群体发送的手机号码
     * @author :Wind
     */
    void delayMassTexting(List<String> phones, String message, Long delayedTime);

    /**
     * <p>说明：使用自定义模板发送群体延迟短信
     * delayMassTexting
     *
     * @param phones      要群体发送的手机号码
     * @param templateId  模板id
     * @param messages    key为模板变量名称 value为模板变量值
     * @param delayedTime 延迟的时间
     * @author :Wind
     */
    void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime);

    /**
     * <p>说明：加入黑名单【这个需要有全局操作的同时需要操作缓存，那么不给smsblend实际处理，代理部分处理】
     * joinInBlacklist
     *
     * @param phone 需要加入黑名单的手机号
     * @author :sh1yu
     */
    default void joinInBlacklist(String phone) {
    }

    /**
     * <p>说明：从黑名单移除【为了sms4j组件有统一入口，同时这个需要有全局操作的同时需要操作缓存，那么不给smsblend实际处理，代理部分处理】
     * removeFromBlacklist
     *
     * @param phone 需要加入黑名单的手机号
     * @author :sh1yu
     */
    default void removeFromBlacklist(String phone) {
    }

    /**
     * <p>说明：批量加入黑名单【为了sms4j组件有统一入口，同时这个需要有全局操作的同时需要操作缓存，那么不给smsblend实际处理，代理部分处理】
     * batchJoinBlacklist
     *
     * @param phones 需要加入黑名单的手机号数组
     * @author :sh1yu
     */
    default void batchJoinBlacklist(List<String > phones) {
    }

    /**
     * <p>说明：批量从黑名单移除【为了sms4j组件有统一入口，同时这个需要有全局操作的同时需要操作缓存，那么不给smsblend实际处理，代理部分处理】
     * batchRemovalFromBlacklist
     *
     * @param phones 需要移除黑名单的手机号数组
     * @author :sh1yu
     */
    default void batchRemovalFromBlacklist(List<String > phones) {
    }

    /**
     * <p>说明：获取黑名单命中记录【为了sms4j组件有统一入口，同时这个需要有全局操作的同时需要操作缓存，那么不给smsblend实际处理，代理部分处理】
     * getTriggerRecording
     *
     * @author :sh1yu
     */
    default List<String> getTriggerRecord() {
        return new ArrayList<>();
    }

    /**
     * <p>说明：清理黑名单命中记录【为了sms4j组件有统一入口，同时这个需要有全局操作的同时需要操作缓存，那么不给smsblend实际处理，代理部分处理】
     * clearTriggerRecording
     *
     * @author :sh1yu
     */
    default void clearTriggerRecord() {
    }
}
