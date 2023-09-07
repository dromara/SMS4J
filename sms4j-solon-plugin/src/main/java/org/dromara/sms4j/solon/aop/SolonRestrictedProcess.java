package org.dromara.sms4j.solon.aop;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.RestrictedProcess;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.provider.factory.BeanFactory;
import org.noear.solon.core.AppContext;

@Slf4j
public class SolonRestrictedProcess implements RestrictedProcess {

    private static final Long minTimer = 60 * 1000L;
    private static final Long accTimer = 24 * 60 * 60 * 1000L;
    private static final String REDIS_KEY = "sms:restricted:";
    private SmsDao smsDao;

    public SolonRestrictedProcess(AppContext context) {
        context.getBeanAsync(SmsDao.class, bean -> smsDao = bean);
    }

    @Override
    public SmsBlendException process(String phone) {
        SmsConfig config = BeanFactory.getSmsConfig();
        Integer accountMax = config.getAccountMax(); // 每日最大发送量
        Integer minuteMax = config.getMinuteMax(); // 每分钟最大发送量

        if (SmsUtil.isNotEmpty(accountMax)) {   // 是否配置了每日限制
            Integer i = (Integer) smsDao.get(REDIS_KEY + phone + "max");
            if (SmsUtil.isEmpty(i)) {
                smsDao.set(REDIS_KEY + phone + "max", 1, accTimer / 1000);
            } else if (i >= accountMax) {
                log.info("The phone:" + phone + ",number of short messages reached the maximum today");
                return new SmsBlendException("The phone:" + phone + ",number of short messages reached the maximum today");
            } else {
                smsDao.set(REDIS_KEY + phone + "max", i + 1, accTimer / 1000);
            }
        }

        if (SmsUtil.isNotEmpty(minuteMax)) {  // 是否配置了每分钟最大限制
            Integer o = (Integer) smsDao.get(REDIS_KEY + phone);
            if (SmsUtil.isNotEmpty(o)) {
                if (o < minuteMax) {
                    smsDao.set(REDIS_KEY + phone, o + 1, minTimer / 1000);
                } else {
                    log.info("The phone:" + phone + ",number of short messages reached the maximum today");
                    return new SmsBlendException("The phone:", phone + " Text messages are sent too often！");
                }
            } else {
                smsDao.set(REDIS_KEY + phone, 1, minTimer / 1000);
            }
        }

        return null;
    }
}
