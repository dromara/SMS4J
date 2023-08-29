package org.dromara.sms4j.api.proxy;

import org.dromara.sms4j.comm.exception.SmsBlendException;

/**
 * 短信拦截处理接口
 */
public interface RestrictedProcess {

    /**
     * 拦截校验过程
     * @param phone
     * @return
     * @throws Exception
     */
    SmsBlendException process(String phone) throws Exception;

}
