package org.dromara.sms4j.core.proxy.strategy;


import org.dromara.sms4j.api.strategy.IInterceptorStrategy;

import java.util.LinkedHashMap;
import java.util.List;


/**
 * 同步调用方法参数校验前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 * TODO 异步调用和延迟调用的参数是否需要校验？
 */
public interface ISyncMethodParamValidateStrategy extends IInterceptorStrategy {

    void validateMessage(Object messageObj);

    void validatePhone(String phone);

    void validatePhones(List<String> phones);

    void validateMessages(String templateId, LinkedHashMap<String, String> messages);


}
