package org.dromara.sms4j.core.proxy.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.proxy.Restricted;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;

/**
 * 短信发送时间级上限拦截器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class SpanMaxRestrictedMethodInterceptor extends AcctMaxRestrictedMethodInterceptor implements Restricted  {
    @Override
    public int getOrder() {
        return SmsProxyFactory.SPAN_RESTRICTED_METHOD_INTERCEPTOR;
    }

}
