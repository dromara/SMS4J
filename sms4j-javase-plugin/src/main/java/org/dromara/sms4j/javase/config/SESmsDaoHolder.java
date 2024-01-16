package org.dromara.sms4j.javase.config;

import org.dromara.sms4j.api.dao.SmsDao;

public class SESmsDaoHolder {

    private static SmsDao smsDao = null;

    public static SmsDao getSmsDao() {
        return smsDao;
    }

    public static void setSmsDao(SmsDao smsDao) {
        SESmsDaoHolder.smsDao = smsDao;
    }
}
