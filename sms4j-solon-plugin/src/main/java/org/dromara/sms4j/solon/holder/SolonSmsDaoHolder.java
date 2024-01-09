package org.dromara.sms4j.solon.holder;

import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.dao.SmsDaoDefaultImpl;
import org.dromara.sms4j.comm.utils.SmsUtils;
import org.noear.solon.core.AppContext;

public class SolonSmsDaoHolder{

    private static SmsDao smsDao;

    public SolonSmsDaoHolder(AppContext context) {
        context.getBeanAsync(SmsDao.class, bean -> smsDao = bean);
    }

    public static SmsDao getSmsDao() {
        if (SmsUtils.isEmpty(smsDao)){
            smsDao = SmsDaoDefaultImpl.getInstance();
        }
        return smsDao;
    }
}
