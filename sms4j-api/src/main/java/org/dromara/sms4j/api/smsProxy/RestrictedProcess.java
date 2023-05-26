package org.dromara.sms4j.api.smsProxy;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.comm.config.SmsConfig;
import org.dromara.sms4j.comm.exception.SmsBlendException;
import org.dromara.sms4j.comm.utils.SmsUtil;
import org.dromara.sms4j.comm.utils.TimeExpiredPoolCache;

@Slf4j
public class RestrictedProcess {
    static Long minTimer = 60 * 1000L;
    static Long accTimer = 24 * 60 * 60 * 1000L;
   public SmsBlendException process(SmsConfig config, String args) throws Exception{
      TimeExpiredPoolCache instance = TimeExpiredPoolCache.getInstance();//缓存实例
      Integer accountMax = config.getAccountMax();//每日最大发送量
      Integer minuteMax = config.getMinuteMax();//每分钟最大发送量
      if (SmsUtil.isNotEmpty(accountMax)) {   //是否配置了每日限制
         Integer i = instance.get(args + "max");
         if (SmsUtil.isEmpty(i)) {
            instance.put(args + "max", 1, accTimer);
         } else if (i > accountMax) {
            log.info("The phone:" + args + ",number of short messages reached the maximum today");
            return new SmsBlendException("The phone:" + args + ",number of short messages reached the maximum today");
         } else {
            instance.put(args + "max", i + 1, accTimer);
         }
      }
      if (SmsUtil.isNotEmpty(minuteMax)) {  //是否配置了每分钟最大限制
         Integer o = instance.get(args);
         if (SmsUtil.isNotEmpty(o)) {
            if (o < minuteMax) {
               instance.put(args, o + 1, minTimer);
            } else {
               log.info("The phone:", args + " Text messages are sent too often！");
               return new SmsBlendException("The phone:", args + " Text messages are sent too often！");
            }
         } else {
            instance.put(args, 1, minTimer);
         }
      }
      return null;
   }
}
