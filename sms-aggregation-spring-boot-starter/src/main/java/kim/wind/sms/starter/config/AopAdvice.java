package kim.wind.sms.starter.config;


import kim.wind.sms.comm.exception.SmsBlendException;
import kim.wind.sms.comm.utils.RedisUtils;
import kim.wind.sms.comm.utils.SmsUtil;
import kim.wind.sms.comm.utils.SpringUtil;
import kim.wind.sms.comm.utils.TimeExpiredPoolCache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Aspect
public class AopAdvice {

    private static final Long minTimer = 60 * 1000L;
    private static final Long accTimer = 24 * 60 * 60 * 1000L;

    private static final String REDIS_KEY = "sms:restricted:";

    @Autowired
    private SmsMainConfig config;


    @Pointcut("@annotation(kim.wind.sms.comm.annotation.Restricted)")
    public void restricted() {
    }

    @Around("restricted()")
    public Object restrictedSendMessage(ProceedingJoinPoint p) throws Throwable {

        String args = "";
        ArrayList<String> argsList = new ArrayList<>();
        try {
            args = (String) p.getArgs()[0];
        } catch (Exception e) {
            for (Object o : (ArrayList<?>) p.getArgs()[0]) {
                argsList.add((String) o);
            }
        }
        SmsBlendException process = redisProcess(args);
        if (process != null) {
            throw process;
        }
        argsList.forEach(f -> {
            SmsBlendException proce = null;
            try {
                proce = redisProcess(f);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (proce != null) {
                throw proce;
            }
        });
        return p.proceed();
    }

    private SmsBlendException process(String args) throws Exception {
        TimeExpiredPoolCache instance = TimeExpiredPoolCache.getInstance();//缓存实例
        Integer accountMax = config.getAccountMax();//每日最大发送量
        Integer minuteMax = config.getMinuteMax();//每分钟最大发送量
        if (SmsUtil.isNotEmpty(accountMax)) {   //是否配置了每日限制
            Integer i = instance.get(args + "max");
            if (SmsUtil.isEmpty(i)) {
                instance.put(args + "max", 1, accTimer);
            } else if (i > accountMax) {
                return new SmsBlendException("accountMax", args + "今日短信已达最大次数");
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
                    return new SmsBlendException("minuteMax", args + "短信发送过于频繁！");
                }
            } else {
                instance.put(args, 1, minTimer);
            }
        }
        return null;
    }

    private SmsBlendException redisProcess(String args) throws Exception{
        RedisUtils redis = SpringUtil.getBean(RedisUtils.class);
        if (redis == null || config.getRedisCache().equals("false")){
           return process(args);
        }
        Integer accountMax = config.getAccountMax();//每日最大发送量
        Integer minuteMax = config.getMinuteMax();//每分钟最大发送量
        if (SmsUtil.isNotEmpty(accountMax)) {   //是否配置了每日限制
            Integer i = (Integer) redis.getByKey(REDIS_KEY+args + "max");
            if (SmsUtil.isEmpty(i)) {
                redis.set(REDIS_KEY+args + "max", 1);
            } else if (i > accountMax) {
                return new SmsBlendException("accountMax", args + "今日短信已达最大次数");
            } else {
                redis.set(REDIS_KEY+args + "max", i + 1);
            }
        }
        if (SmsUtil.isNotEmpty(minuteMax)) {  //是否配置了每分钟最大限制
            Integer o = (Integer) redis.getByKey(REDIS_KEY+args);
            if (SmsUtil.isNotEmpty(o)) {
                if (o < minuteMax) {
                    redis.set(REDIS_KEY+args, o + 1);
                } else {
                    return new SmsBlendException("minuteMax", args + "短信发送过于频繁！");
                }
            } else {
                redis.set(REDIS_KEY+args, 1);
            }
        }
        return null;
    }

}
