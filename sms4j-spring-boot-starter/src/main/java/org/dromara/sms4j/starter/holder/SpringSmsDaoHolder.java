package org.dromara.sms4j.starter.holder;

import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.starter.utils.SmsSpringUtils;

public class SpringSmsDaoHolder {
    public static SmsDao getSmsDao() {
        return SmsSpringUtils.getApplicationContext().getBean(SmsDao.class);
    }
}
