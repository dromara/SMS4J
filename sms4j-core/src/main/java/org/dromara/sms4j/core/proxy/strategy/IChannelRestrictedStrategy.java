package org.dromara.sms4j.core.proxy.strategy;

import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.proxy.SmsMethodType;
import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsBlendConfigAware;
import org.dromara.sms4j.api.proxy.aware.InterceptorStrategySmsDaoAware;
import org.dromara.sms4j.api.strategy.IInterceptorStrategy;

/**
 * 短信发送渠道级上限拦截策略
 *
 * @author sh1yu
 * @since 2023/12/22 13:03
 */
public interface IChannelRestrictedStrategy extends IInterceptorStrategy, InterceptorStrategySmsDaoAware, InterceptorStrategySmsBlendConfigAware {
     void restricted(SmsMethodType methodType, SmsBlend smsBlend);
     void flushRecord(SmsMethodType methodType, SmsBlend smsBlend);
}