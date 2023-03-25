package kim.wind.sms.api.callback;

import kim.wind.sms.comm.entity.SmsResponse;

@FunctionalInterface
public interface CallBack {
    void callBack(SmsResponse smsResponse);
}
