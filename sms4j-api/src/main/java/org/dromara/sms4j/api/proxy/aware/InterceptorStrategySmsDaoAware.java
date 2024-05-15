package org.dromara.sms4j.api.proxy.aware;

import org.dromara.sms4j.api.dao.SmsDao;
/**
 * 给InterceptorStrate使用的缓存感知接口
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
public interface InterceptorStrategySmsDaoAware {
    void setSmsDao(SmsDao smsDao);
}
