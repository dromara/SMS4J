package org.dromara.sms4j.javase.config;

import lombok.Getter;
import org.dromara.sms4j.api.dao.SmsDao;

public class SESmsDaoHolder {

    @Getter
    private static SmsDao smsDao = null;

    public static void setSmsDao(SmsDao smsDao) {
        SESmsDaoHolder.smsDao = smsDao;
    }
}
