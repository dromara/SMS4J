package org.dromara.sms4j.core.smsProxy;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;

@Slf4j
public class RestrictedProcess {
    static Long minTimer = 60 * 1000L;
    static Long accTimer = 24 * 60 * 60 * 1000L;
    /**
     * 缓存实例
     */
    @Setter
    private SmsDao smsDao;

    public SmsBlendException process(SmsConfig config, String args) throws Exception {
        Integer accountMax = config.getAccountMax();//每日最大发送量
        Integer minuteMax = config.getMinuteMax();//每分钟最大发送量
        if (SmsUtil.isNotEmpty(accountMax)) {   //是否配置了每日限制
            Integer i = (Integer) smsDao.get(args + "max");
            if (SmsUtil.isEmpty(i)) {
                smsDao.set(args + "max", 1, accTimer);
            } else if (i > accountMax) {
                log.info("The phone:" + args + ",number of short messages reached the maximum today");
                return new SmsBlendException("The phone:" + args + ",number of short messages reached the maximum today");
            } else {
                smsDao.set(args + "max", i + 1, accTimer);
            }
        }
        if (SmsUtil.isNotEmpty(minuteMax)) {  //是否配置了每分钟最大限制
            Integer o = (Integer) smsDao.get(args);
            if (SmsUtil.isNotEmpty(o)) {
                if (o < minuteMax) {
                    smsDao.set(args, o + 1, minTimer);
                } else {
                    log.info("The phone:" + args + " Text messages are sent too often！");
                    return new SmsBlendException("The phone:", args + " Text messages are sent too often！");
                }
            } else {
                smsDao.set(args, 1, minTimer);
            }
        }
        return null;
    }
}
