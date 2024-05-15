package org.dromara.sms4j.core.proxy.strategy;

import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsDaoAware;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;

import java.util.List;

/**
 * 黑名单前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface IListCheckMethodStrategy extends IInterceptorStrategy, InterceptorStrategySmsDaoAware {

    void checkListWithRecord(List<String> phones);
}
