package org.dromara.sms4j.api.callback;

import org.dromara.sms4j.api.entity.SmsResponse;

@FunctionalInterface
public interface CallBack {
    void callBack(SmsResponse smsResponse);
}
