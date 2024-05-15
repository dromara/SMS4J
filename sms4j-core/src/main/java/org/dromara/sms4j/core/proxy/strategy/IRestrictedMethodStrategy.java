package org.dromara.sms4j.core.proxy.strategy;

import org.dromara.sms4j.api.strategy.IInterceptorStrategy;

import java.util.List;


/**
 * 短信发送基础上限前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface IRestrictedMethodStrategy extends IInterceptorStrategy {

    void restricted(List<String> phones);

    void flushRecord(List<String> phones);
}
