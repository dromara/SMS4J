package org.dromara.sms.api.callback;

import org.dromara.sms.api.entity.SmsResponse;

@FunctionalInterface
public interface CallBack {
    void callBack(SmsResponse smsResponse);
}
