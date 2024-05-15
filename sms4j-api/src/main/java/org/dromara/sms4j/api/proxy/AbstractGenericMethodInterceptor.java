package org.dromara.sms4j.api.proxy;

import org.dromara.sms4j.api.callback.CallBack;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

/**
 * {@link SmsMethodInterceptor}的通用实现，
 * 能够根据{@link SmsMethodType}将调用分发到某个具体的拦截方法上。
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public abstract class AbstractGenericMethodInterceptor<C extends IInterceptorStrategy> implements SmsMethodInterceptor<C> {

    /**
     * 前置拦截，在方法执行前调用 interceptor尽量仅作分发，使用 IInterceptorStrategy 进行具体处理
     *
     * @param methodType 方法类型，若不是{@link SmsMethodType}则可能为{@code null}
     * @param method     方法
     * @param target     调用对象
     * @param params     调用参数
     * @return 调用参数
     * @implNote 若重写此方法，则务必调用{@link #doBeforeInvoke}方法实现调用分发
     */
    @Override
    public Object[] beforeInvoke(SmsMethodType methodType, Method method, Object target, Object[] params) {
        if (Objects.nonNull(methodType)) {
            // 将方法分发到具体的调用
            doBeforeInvoke(params, methodType);
        }
        return params;
    }

    @SuppressWarnings("unchecked")
    protected final void doBeforeInvoke(Object[] params, SmsMethodType methodType) {
        Objects.requireNonNull(methodType);
        switch (methodType) {
            case SEND_MESSAGE:
                beforeSendMessage((String)params[0], (String)params[1]);
                break;
            case SEND_MESSAGE_WITH_TEMPLATE:
                beforeSendMessageWithTemplate((String)params[0], (LinkedHashMap<String, String>)params[1]);
                break;
            case SEND_MESSAGE_WITH_CUSTOM_TEMPLATE:
                beforeSendMessageWithCustomTemplate((String)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2]);
                break;
            case MASS_TEXTING:
                beforeMassTexting((List<String>)params[0], (String)params[1]);
                break;
            case MASS_TEXTING_WITH_TEMPLATE:
                beforeMassTextingWithTemplate((List<String>)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2]);
                break;
            case SEND_MESSAGE_ASYNC:
                beforeSendMessageAsync((String)params[0], (String)params[1], (CallBack)params[2]);
                break;
            case SEND_MESSAGE_ASYNC_NO_CALLBACK:
                beforeSendMessageAsyncNoCallback((String)params[0], (String)params[1]);
                break;
            case SEND_MESSAGE_ASYNC_WITH_TEMPLATE:
                beforeSendMessageAsyncWithTemplate((String)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2], (CallBack)params[3]);
                break;
            case SEND_MESSAGE_ASYNC_WITH_TEMPLATE_NO_CALLBACK:
                beforeSendMessageAsyncWithTemplateNoCallback((String)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2]);
                break;
            case DELAYED_MESSAGE:
                beforeDelayedMessage((String)params[0], (String)params[1], (Long)params[2]);
                break;
            case DELAYED_MESSAGE_WITH_TEMPLATE:
                beforeDelayedMessageWithTemplate((String)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2], (Long)params[3]);
                break;
            case DELAY_MASS_TEXTING:
                beforeDelayMassTexting((List<String>)params[0], (String)params[1], (Long)params[2]);
                break;
            case DELAY_MASS_TEXTING_WITH_TEMPLATE:
                beforeDelayMassTextingWithTemplate((List<String>)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2], (Long)params[3]);
                break;
            default: // do nothing
        }
    }

    /**
     * 后置拦截，在方法执行后，无论是否发生异常都会调用
     *
     * @param methodType 方法类型，若不是{@link SmsMethodType}则可能为{@code null}
     * @param method 调用方法
     * @param params 调用参数
     * @param result 返回值
     * @param ex 调用过程捕获的异常，可能为{@code null}
     * @return 返回值，不为{@code null}时将覆盖原有的返回值
     * @implNote 若重写此方法，则务必调用{@link ##doAfterCompletion}方法实现调用分发
     */
    @Override
    public Object afterCompletion(SmsMethodType methodType, Method method, Object target,Object[] params, Object result, Exception ex) {
        if (Objects.nonNull(methodType)) {
            // 将方法分发到具体调用...
            doAfterCompletion(params, result, ex, methodType);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private void doAfterCompletion(Object[] params, Object result, Exception ex, SmsMethodType methodType) {
        Objects.requireNonNull(methodType);
        switch (methodType) {
            case SEND_MESSAGE:
                afterSendMessage((String)params[0], (String)params[1], result, ex);
                break;
            case SEND_MESSAGE_WITH_TEMPLATE:
                afterSendMessageWithTemplate((String)params[0], (LinkedHashMap<String, String>)params[1], result, ex);
                break;
            case SEND_MESSAGE_WITH_CUSTOM_TEMPLATE:
                afterSendMessageWithCustomTemplate((String)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2], result, ex);
                break;
            case MASS_TEXTING:
                afterMassTexting((List<String>)params[0], (String)params[1], result, ex);
                break;
            case MASS_TEXTING_WITH_TEMPLATE:
                afterMassTextingWithTemplate((List<String>)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2], result, ex);
                break;
            case SEND_MESSAGE_ASYNC:
                afterSendMessageAsync((String)params[0], (String)params[1], (CallBack)params[2], result, ex);
                break;
            case SEND_MESSAGE_ASYNC_NO_CALLBACK:
                afterSendMessageAsyncNoCallback((String)params[0], (String)params[1], result, ex);
                break;
            case SEND_MESSAGE_ASYNC_WITH_TEMPLATE:
                afterSendMessageAsyncWithTemplate((String)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2], (CallBack)params[3], result, ex);
                break;
            case SEND_MESSAGE_ASYNC_WITH_TEMPLATE_NO_CALLBACK:
                afterSendMessageAsyncWithTemplateNoCallback((String)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2], result, ex);
                break;
            case DELAYED_MESSAGE:
                afterDelayedMessage((String)params[0], (String)params[1], (Long)params[2], result, ex);
                break;
            case DELAYED_MESSAGE_WITH_TEMPLATE:
                afterDelayedMessageWithTemplate((String)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2], (Long)params[3], result, ex);
                break;
            case DELAY_MASS_TEXTING:
                afterDelayMassTexting((List<String>)params[0], (String)params[1], (Long)params[2], result, ex);
                break;
            case DELAY_MASS_TEXTING_WITH_TEMPLATE:
                afterDelayMassTextingWithTemplate((List<String>)params[0], (String)params[1], (LinkedHashMap<String, String>)params[2], (Long)params[3], result, ex);
                break;
            default: // do nothing
        }
    }

    // region ===== before =====

    /**
     * {@link org.dromara.sms4j.api.SmsBlend#sendMessage(String, String)}
     *
     * @param phone 接收短信的手机号
     * @param message 内容
     */
    protected void beforeSendMessage(String phone, String message) {
        // do nothing
    }

    /**
     * {@link org.dromara.sms4j.api.SmsBlend#sendMessage(String, LinkedHashMap)}
     *
     * @param phone 接收短信的手机号
     * @param messages 模板内容
     */
    protected void beforeSendMessageWithTemplate(String phone, LinkedHashMap<String, String> messages) {
        // do nothing
    }

    /**
     * {@link org.dromara.sms4j.api.SmsBlend#sendMessage(String, String, LinkedHashMap)}
     *
     * @param phone 接收短信的手机号
     * @param templateId 模板ID
     * @param messages 内容
     */
    protected void beforeSendMessageWithCustomTemplate(
        String phone, String templateId, LinkedHashMap<String, String> messages) {
        // do nothing
    }

    /**
     * {@link org.dromara.sms4j.api.SmsBlend#massTexting(List, String)}
     *
     * @param phones 接收短信的手机号
     * @param message 内容
     */
    protected void beforeMassTexting(List<String> phones, String message) {
        // do nothing
    }

    /**
     * {@link org.dromara.sms4j.api.SmsBlend#massTexting(List, String, LinkedHashMap)}
     *
     * @param phones 接收短信的手机号
     * @param templateId 模板ID
     * @param messages 内容
     */
    protected void beforeMassTextingWithTemplate(
        List<String> phones, String templateId, LinkedHashMap<String, String> messages) {
        // do nothing
    }

    /**
     * 前置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessageAsync(String, String, CallBack)}
     *
     * @param phone    接收短信的手机号
     * @param message  内容
     * @param callBack 回调
     */
    protected void beforeSendMessageAsync(String phone, String message, CallBack callBack) {
        // do nothing
    }

    /**
     * 前置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessageAsync(String, String)}
     *
     * @param phone   接收短信的手机号
     * @param message 内容
     */
    protected void beforeSendMessageAsyncNoCallback(String phone, String message) {
        // do nothing
    }

    /**
     * 前置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessageAsync(String, String, LinkedHashMap, CallBack)}
     *
     * @param phone    接收短信的手机号
     * @param message  内容
     * @param template 模板
     * @param callBack 回调
     */
    protected void beforeSendMessageAsyncWithTemplate(String phone, String message, LinkedHashMap<String, String> template, CallBack callBack) {
        // do nothing
    }

    /**
     * 前置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessageAsync(String, String, LinkedHashMap)}
     *
     * @param phone    接收短信的手机号
     * @param message  内容
     * @param template 模板
     */
    protected void beforeSendMessageAsyncWithTemplateNoCallback(String phone, String message, LinkedHashMap<String, String> template) {
        // do nothing
    }

    /**
     * 前置处理 {@link org.dromara.sms4j.api.SmsBlend#delayedMessage(String, String, Long)}
     *
     * @param phone   接收短信的手机号
     * @param message 内容
     * @param delay   延迟时间
     */
    protected void beforeDelayedMessage(String phone, String message, Long delay) {
        // do nothing
    }

    /**
     * 前置处理 {@link org.dromara.sms4j.api.SmsBlend#delayedMessage(String, String, LinkedHashMap, Long)}
     *
     * @param phone    接收短信的手机号
     * @param message  内容
     * @param template 模板
     * @param delay    延迟时间
     */
    protected void beforeDelayedMessageWithTemplate(String phone, String message, LinkedHashMap<String, String> template, Long delay) {
        // do nothing
    }

    /**
     * 前置处理 {@link org.dromara.sms4j.api.SmsBlend#delayMassTexting(List, String, Long)}
     *
     * @param phones  接收短信的手机号
     * @param message 内容
     * @param delay   延迟时间
     */
    protected void beforeDelayMassTexting(List<String> phones, String message, Long delay) {
        // do nothing
    }

    /**
     * 前置处理 {@link org.dromara.sms4j.api.SmsBlend#delayMassTexting(List, String, LinkedHashMap, Long)}
     *
     * @param phones   接收短信的手机号
     * @param message  内容
     * @param template 模板
     * @param delay    延迟时间
     */
    protected void beforeDelayMassTextingWithTemplate(List<String> phones, String message, LinkedHashMap<String, String> template, Long delay) {
        // do nothing
    }

    // endregion

    // region ===== after =====

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessage(String, String)}
     *
     * @param phone 接收短信的手机号
     * @param message 内容
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterSendMessage(String phone, String message, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessage(String, LinkedHashMap)}
     *
     * @param phone 接收短信的手机号
     * @param messages 模板内容
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterSendMessageWithTemplate(String phone, LinkedHashMap<String, String> messages, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessage(String, String, LinkedHashMap)}
     *
     * @param phone 接收短信的手机号
     * @param templateId 模板ID
     * @param messages 内容
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterSendMessageWithCustomTemplate(String phone, String templateId, LinkedHashMap<String, String> messages, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#massTexting(List, String)}
     *
     * @param phones 接收短信的手机号
     * @param message 内容
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterMassTexting(List<String> phones, String message, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#massTexting(List, String, LinkedHashMap)}
     *
     * @param phones 接收短信的手机号
     * @param templateId 模板ID
     * @param messages 内容
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterMassTextingWithTemplate(List<String> phones, String templateId, LinkedHashMap<String, String> messages, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessageAsync(String, String, CallBack)}
     *
     * @param phone 接收短信的手机号
     * @param message 内容
     * @param callBack 回调
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterSendMessageAsync(String phone, String message, CallBack callBack, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessageAsync(String, String)}
     *
     * @param phone 接收短信的手机号
     * @param message 内容
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterSendMessageAsyncNoCallback(String phone, String message, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessageAsync(String, String, LinkedHashMap, CallBack)}
     *
     * @param phone 接收短信的手机号
     * @param message 内容
     * @param template 模板
     * @param callBack 回调
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterSendMessageAsyncWithTemplate(String phone, String message, LinkedHashMap<String, String> template, CallBack callBack, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#sendMessageAsync(String, String, LinkedHashMap)}
     *
     * @param phone 接收短信的手机号
     * @param message 内容
     * @param template 模板
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterSendMessageAsyncWithTemplateNoCallback(String phone, String message, LinkedHashMap<String, String> template, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#delayedMessage(String, String, Long)}
     *
     * @param phone 接收短信的手机号
     * @param message 内容
     * @param delay 延迟时间
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterDelayedMessage(String phone, String message, Long delay, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#delayedMessage(String, String, LinkedHashMap, Long)}
     *
     * @param phone 接收短信的手机号
     * @param message 内容
     * @param template 模板
     * @param delay 延迟时间
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterDelayedMessageWithTemplate(String phone, String message, LinkedHashMap<String, String> template, Long delay, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#delayMassTexting(List, String, Long)}
     *
     * @param phones 接收短信的手机号
     * @param message 内容
     * @param delay 延迟时间
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterDelayMassTexting(List<String> phones, String message, Long delay, Object result, Exception ex) {
        // do nothing
    }

    /**
     * 后置处理 {@link org.dromara.sms4j.api.SmsBlend#delayMassTexting(List, String, LinkedHashMap, Long)}
     *
     * @param phones 接收短信的手机号
     * @param message 内容
     * @param template 模板
     * @param delay 延迟时间
     * @param result 方法返回值
     * @param ex 异常信息
     */
    protected void afterDelayMassTextingWithTemplate(List<String> phones, String message, LinkedHashMap<String, String> template, Long delay, Object result, Exception ex) {
        // do nothing
    }

    // endregion
}
