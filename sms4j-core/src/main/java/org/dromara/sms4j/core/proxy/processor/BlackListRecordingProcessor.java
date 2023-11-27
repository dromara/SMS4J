package org.dromara.sms4j.core.proxy.processor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.SmsProcessor;
import org.dromara.sms4j.api.proxy.aware.SmsConfigAware;
import org.dromara.sms4j.api.proxy.aware.SmsDaoAware;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 */
@Slf4j
public class BlackListRecordingProcessor implements SmsProcessor, SmsDaoAware, SmsConfigAware {
    @Setter
    SmsDao smsDao;

    @Setter
    Object smsConfig;


    @Override
    public Object[] preProcessor(Method method, Object source, Object[] param) {
        //添加到黑名单
        if (method.getName().equals("joinInBlacklist")) {
            String cacheKey = getCacheKey();
            ArrayList<String> blackList = getBlackList(cacheKey);
            blackList.add((String) param[0]);
            flushBlackList(cacheKey,blackList);
        }
        //从黑名单移除
        if (method.getName().equals("removeFromBlacklist")) {
            String cacheKey = getCacheKey();
            ArrayList<String> blackList = getBlackList(cacheKey);
            blackList.remove((String) param[0]);
            flushBlackList(cacheKey,blackList);
        }
        //批量添加到黑名单
        if (method.getName().equals("batchJoinBlacklist")) {
            String cacheKey = getCacheKey();
            ArrayList<String> blackList = getBlackList(cacheKey);
            blackList.addAll((List<String>) param[0]);
            flushBlackList(cacheKey,blackList);
        }
        //批量从黑名单移除
        if (method.getName().equals("batchRemovalFromBlacklist")) {
            String cacheKey = getCacheKey();
            ArrayList<String> blackList = getBlackList(cacheKey);
            blackList.removeAll((List<String>) param[0]);
            flushBlackList(cacheKey,blackList);
        }
        return param;
    }

    //构建cachekey
    public String getCacheKey(){
        return "sms:blacklist:global";
    }

    //获取黑名单，没有就新建
    public ArrayList<String> getBlackList(String cacheKey) {
        ArrayList<String> blackList;
        Object cache = smsDao.get(cacheKey);
        if (null != cache) {
            blackList = (ArrayList<String>) cache;
            return blackList;
        }
        blackList = new ArrayList<>();
        smsDao.set("sms:blacklist:global", blackList);
        return blackList;
    }

    //让黑名单生效
    public void flushBlackList(String cacheKey ,ArrayList<String> blackList) {
        smsDao.set(cacheKey, blackList);
    }
}
