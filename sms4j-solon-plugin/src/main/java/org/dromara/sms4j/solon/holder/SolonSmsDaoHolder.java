package org.dromara.sms4j.solon.holder;

import org.dromara.sms4j.api.dao.SmsDao;
import org.noear.solon.Solon;

public class SolonSmsDaoHolder {
    public static SmsDao getSmsDao() {
        return Solon.context().getBean(SmsDao.class);
    }
}
