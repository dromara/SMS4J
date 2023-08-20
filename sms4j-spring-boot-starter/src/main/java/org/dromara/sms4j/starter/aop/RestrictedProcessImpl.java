package org.dromara.sms4j.starter.aop;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.core.smsProxy.RestrictedProcess;
import org.dromara.sms4j.provider.config.SmsConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.starter.utils.SmsSpringUtil;

import java.util.Objects;

@Slf4j
public class RestrictedProcessImpl extends RestrictedProcess {
    private static final Long minTimer = 60 * 1000L;
    private static final Long accTimer = 24 * 60 * 60 * 1000L;
    private static final String REDIS_KEY = "sms:restricted:";


    @Override
    public SmsBlendException process(SmsConfig config,String args) throws Exception {
        SmsDao smsDao = SmsSpringUtil.getBean(SmsDao.class);
        if (Objects.isNull(smsDao)){
            throw new SmsBlendException("The dao tool could not be found");
        }
        Integer accountMax = config.getAccountMax();//每日最大发送量
        Integer minuteMax = config.getMinuteMax();//每分钟最大发送量
        if (SmsUtil.isNotEmpty(accountMax)) {   //是否配置了每日限制
            Integer i = (Integer) smsDao.get(REDIS_KEY + args + "max");
            if (SmsUtil.isEmpty(i)) {
                smsDao.set(REDIS_KEY + args + "max", 1, accTimer / 1000);
            } else if (i > accountMax) {
                log.info("The phone:" + args + ",number of short messages reached the maximum today");
                return new SmsBlendException("The phone:" + args + ",number of short messages reached the maximum today");
            } else {
                smsDao.set(REDIS_KEY + args + "max", i + 1, accTimer / 1000);
            }
        }
        if (SmsUtil.isNotEmpty(minuteMax)) {  //是否配置了每分钟最大限制
            Integer o = (Integer) smsDao.get(REDIS_KEY + args);
            if (SmsUtil.isNotEmpty(o)) {
                if (o < minuteMax) {
                    smsDao.set(REDIS_KEY + args, o + 1, minTimer / 1000);
                } else {
                    log.info("The phone:" + args + ",number of short messages reached the maximum today");
                    return new SmsBlendException("The phone:", args + " Text messages are sent too often！");
                }
            } else {
                smsDao.set(REDIS_KEY + args, 1, minTimer / 1000);
            }
        }
        return null;
    }
}
