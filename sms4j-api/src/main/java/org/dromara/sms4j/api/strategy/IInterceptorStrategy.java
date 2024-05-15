package org.dromara.sms4j.api.strategy;

/**
 * <p>说明：拦截器执行策略接口
 *
 * @author :sh1yu
 */
public interface IInterceptorStrategy {

    /**
     * 本策略适配与哪个拦截器
     * @return :拦截器的class
     */
    Class<?> aPendingProblemWith();
}
