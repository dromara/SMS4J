package org.dromara.sms4j.api.proxy;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.callback.CallBack;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * {@link SmsBlend}中的主要方法
 *
 * @author huangchengxing
 */
@Getter
public enum SmsMethodType {

    /**
     * {@link SmsBlend#sendMessage(String, String)}
     */
    SEND_MESSAGE("sendMessage", String.class, String.class),

    /**
     * {@link SmsBlend#sendMessage(String, LinkedHashMap)}
     */
    SEND_MESSAGE_WITH_TEMPLATE("sendMessage", String.class, LinkedHashMap.class),

    /**
     * {@link SmsBlend#sendMessage(String, String, LinkedHashMap)}
     */
    SEND_MESSAGE_WITH_CUSTOM_TEMPLATE("sendMessage", String.class, String.class, LinkedHashMap.class),

    /**
     * {@link SmsBlend#massTexting(List, String)}
     */
    MASS_TEXTING("massTexting", List.class, String.class),

    /**
     * {@link SmsBlend#massTexting(List, String, LinkedHashMap)}
     */
    MASS_TEXTING_WITH_TEMPLATE("massTexting", List.class, String.class, LinkedHashMap.class),

    /**
     * {@link SmsBlend#sendMessageAsync(String, String, CallBack)}
     */
    SEND_MESSAGE_ASYNC("sendMessageAsync", String.class, String.class, CallBack.class),

    /**
     * {@link SmsBlend#sendMessageAsync(String, String)}
     */
    SEND_MESSAGE_ASYNC_NO_CALLBACK("sendMessageAsync", String.class, String.class),

    /**
     * {@link SmsBlend#sendMessageAsync(String, String, LinkedHashMap, CallBack)}
     */
    SEND_MESSAGE_ASYNC_WITH_TEMPLATE("sendMessageAsync", String.class, String.class, LinkedHashMap.class, CallBack.class),

    /**
     * {@link SmsBlend#sendMessageAsync(String, String, LinkedHashMap)}
     */
    SEND_MESSAGE_ASYNC_WITH_TEMPLATE_NO_CALLBACK("sendMessageAsync", String.class, String.class, LinkedHashMap.class),

    /**
     * {@link SmsBlend#delayedMessage(String, String, Long)}
     */
    DELAYED_MESSAGE("delayedMessage", String.class, String.class, Long.class),

    /**
     * {@link SmsBlend#delayedMessage(String, String, LinkedHashMap, Long)}
     */
    DELAYED_MESSAGE_WITH_TEMPLATE("delayedMessage", String.class, String.class, LinkedHashMap.class, Long.class),

    /**
     * {@link SmsBlend#delayMassTexting(List, String, Long)}
     */
    DELAY_MASS_TEXTING("delayMassTexting", List.class, String.class, Long.class),

    /**
     * {@link SmsBlend#delayMassTexting(List, String, LinkedHashMap, Long)}
     */
    DELAY_MASS_TEXTING_WITH_TEMPLATE("delayMassTexting", List.class, String.class, LinkedHashMap.class, Long.class),

    /**
     * {@link SmsBlend#joinInBlacklist(String)}
     */
    ADD_BLACK_LIST_ITEM("joinInBlacklist", String.class),

    /**
     * {@link SmsBlend#removeFromBlacklist( String)}
     */
    REMOVE_BLACK_LIST_ITEM("removeFromBlacklist", String.class),

    /**
     * {@link SmsBlend#batchJoinBlacklist(List)}
     */
    ADD_BLACK_LIST_ITEMS("batchJoinBlacklist", List.class),

    /**
     * {@link SmsBlend#batchRemovalFromBlacklist(List)}
     */
    REMOVE_BLACK_LIST_ITEMS("batchRemovalFromBlacklist", List.class),
    /**
     * {@link SmsBlend#getTriggerRecord()}
     */
     GET_TRIGGER_RECORD("getTriggerRecord"),

    /**
     * {@link SmsBlend#clearTriggerRecord()}
     */
    CLEAR_TRIGGER_RECORD("clearTriggerRecord");

    /**
     * 方法
     */
    private final Method method;

    /**
     * 获取方法名称
     *
     * @return 方法名称
     */
    public String getName() {
        return method.getName();
    }

    /**
     * 检查指定方法是否与{@link SmsBlend}中的方法匹配
     *
     * @param target 目标方法
     * @return 是否
     */
    public boolean isMatch(Method target) {
        if (target == method) {
            return true;
        }
        return Objects.nonNull(target)
            && Objects.equals(method.getName(), target.getName())
            && ClassUtil.isAllAssignableFrom(method.getParameterTypes(), target.getParameterTypes())
            && ClassUtil.isAssignable(method.getReturnType(), target.getReturnType());
    }

    /**
     * 获取与目标方法对应的{@link SmsBlend}方法
     *
     * @param targetMethod 目标方法
     * @return {@link SmsMethodType}
     */
    public static SmsMethodType of(Method targetMethod) {
        return Stream.of(values())
            .filter(m -> m.isMatch(targetMethod))
            .findFirst()
            .orElse(null);
    }

    SmsMethodType(String methodName, Class<?>... parameterTypes) {
        this.method = ReflectUtil.getMethod(SmsBlend.class, methodName, parameterTypes);
        Assert.notNull(
            method, "Cannot find best match method from SmsBlend: ({})[{}]",
            methodName, Arrays.asList(parameterTypes)
        );
    }
}
