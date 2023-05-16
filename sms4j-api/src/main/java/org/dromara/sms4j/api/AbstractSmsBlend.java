package org.dromara.sms4j.api;

import com.dtflys.forest.config.ForestConfiguration;
import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.entity.SmsResponse;
import org.dromara.sms4j.comm.annotation.Restricted;
import org.dromara.sms4j.comm.delayedTime.DelayedTime;
import org.dromara.sms4j.comm.factory.BeanFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public abstract class AbstractSmsBlend implements SmsBlend{

    protected final Executor pool;
    protected final DelayedTime delayed;

    protected final ForestConfiguration http = BeanFactory.getForestConfiguration();
    protected AbstractSmsBlend(Executor pool, DelayedTime delayed) {
        this.pool = pool;
        this.delayed = delayed;
    }

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

    public abstract SmsResponse sendMessage(String phone, String message);

    /**
     * <p>说明：使用自定义模板发送短信
     * sendMessage
     *
     * @param templateId 模板id
     * @param messages   key为模板变量名称 value为模板变量值
     * @author :Wind
     */

    public abstract SmsResponse sendMessage(String phone, String templateId, LinkedHashMap<String, String> messages);

    /**
     * <p>说明：群发固定模板短信
     * massTexting
     *
     * @author :Wind
     */

    public abstract SmsResponse massTexting(List<String> phones, String message);

    /**
     * <p>说明：使用自定义模板群发短信
     * massTexting
     *
     * @author :Wind
     */

    public abstract SmsResponse massTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages);

    /**
     * <p>说明：异步短信发送，固定消息模板短信
     * sendMessageAsync
     *
     * @param phone    要发送的号码
     * @param message  发送内容
     * @param callBack 回调
     * @author :Wind
     */
    @Restricted
    public final void sendMessageAsync(String phone, String message, CallBack callBack){
        CompletableFuture<SmsResponse> smsResponseCompletableFuture = CompletableFuture.supplyAsync(() -> sendMessage(phone, message), pool);
        smsResponseCompletableFuture.thenAcceptAsync(callBack::callBack);
    }

    /**
     * <p>说明：异步发送短信，不关注发送结果
     * sendMessageAsync
     *
     * @param phone   要发送的号码
     * @param message 发送内容
     * @author :Wind
     */
    @Restricted
    public final void sendMessageAsync(String phone, String message){
        pool.execute(() -> {
            sendMessage(phone, message);
        });
    }

    /**
     * <p>说明：异步短信发送，使用自定义模板发送短信
     * sendMessage
     *
     * @param templateId 模板id
     * @param messages   key为模板变量名称 value为模板变量值
     * @param callBack   回调
     * @author :Wind
     */

    @Restricted
    public final void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages, CallBack callBack){
        CompletableFuture<SmsResponse> smsResponseCompletableFuture = CompletableFuture.supplyAsync(() -> sendMessage(phone,templateId, messages), pool);
        smsResponseCompletableFuture.thenAcceptAsync(callBack::callBack);
    }

    /**
     * <p>说明：异步短信发送，使用自定义模板发送短信，不关注发送结果
     * sendMessageAsync
     *
     * @param templateId 模板id
     * @param messages   key为模板变量名称 value为模板变量值
     * @author :Wind
     */
    @Restricted
    public final void sendMessageAsync(String phone, String templateId, LinkedHashMap<String, String> messages){
        pool.execute(() -> {
            sendMessage(phone, templateId, messages);
        });
    }

    /**
     * <p>说明：使用固定模板发送延时短信
     * delayedMessage
     *
     * @param phone       接收短信的手机号
     * @param message     要发送的短信
     * @param delayedTime 延迟时间
     * @author :Wind
     */
    @Restricted
    public final void delayedMessage(String phone, String message, Long delayedTime){
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(phone, message);
            }
        }, delayedTime);
    }

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
    @Restricted
    public final void delayedMessage(String phone, String templateId, LinkedHashMap<String, String> messages, Long delayedTime){
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                sendMessage(phone, templateId, messages);
            }
        }, delayedTime);
    }

    /**
     * <p>说明：群发延迟短信
     * delayMassTexting
     *
     * @param phones 要群体发送的手机号码
     * @author :Wind
     */
    @Restricted
    public final void delayMassTexting(List<String> phones, String message, Long delayedTime){
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                massTexting(phones, message);
            }
        }, delayedTime);
    }

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
    @Restricted
    public final void delayMassTexting(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Long delayedTime){
        this.delayed.schedule(new TimerTask() {
            @Override
            public void run() {
                massTexting(phones, templateId, messages);
            }
        }, delayedTime);
    }
}
