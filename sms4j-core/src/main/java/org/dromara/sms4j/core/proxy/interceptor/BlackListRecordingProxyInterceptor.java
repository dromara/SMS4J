package org.dromara.sms4j.core.proxy.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.proxy.SmsMethodType;
import org.dromara.sms4j.core.proxy.strategy.IBlackListManageStrategy;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;


/**
 * 黑名单记录代理拦截器，仅
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class BlackListRecordingProxyInterceptor implements SmsMethodInterceptor<IBlackListManageStrategy> {

    @Override
    public int getOrder() {
        return SmsProxyFactory.BLACK_LIST_RECORDING_INTERCEPTOR_ORDER;
    }

    /**
     * 前置拦截，在方法执行前调用
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
        switch (methodType) {
            case ADD_BLACK_LIST_ITEM:
                getStrategy().addBlackListItem((String) params[0]);
                break;
            case REMOVE_BLACK_LIST_ITEM:
                getStrategy().removeBlackListItem((String) params[0]);
                break;
            case ADD_BLACK_LIST_ITEMS:
                getStrategy().addBlackListItems((List<String>) params[0]);
                break;
            case REMOVE_BLACK_LIST_ITEMS:
                getStrategy().removeBlackListItems((List<String>) params[0]);
                break;
            case GET_TRIGGER_RECORD:
                getStrategy().getTriggerRecord();
                break;
            case CLEAR_TRIGGER_RECORD:
                getStrategy().clearTriggerRecord();
                break;
            default: // do nothing
        }
    }

    public Object afterCompletion(
        SmsMethodType methodType, Method method, Object target,Object[] params, Object result, Exception ex) {
        if (Objects.nonNull(methodType)) {
            switch (methodType) {
                case GET_TRIGGER_RECORD:
                    return getStrategy().getTriggerRecord();
            }
        }
        return result;
    }

}
