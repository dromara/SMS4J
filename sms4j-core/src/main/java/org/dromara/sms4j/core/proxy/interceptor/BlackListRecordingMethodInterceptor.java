package org.dromara.sms4j.core.proxy.interceptor;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.SmsBlend;
import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.proxy.SmsMethodType;
import org.dromara.sms4j.api.proxy.SmsMethodInterceptor;
import org.dromara.sms4j.api.proxy.aware.SmsDaoAware;
import org.dromara.sms4j.core.proxy.SmsProxyFactory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 黑名单前置拦截执行器
 *
 * @author sh1yu
 * @since 2023/10/27 13:03
 * @see SmsBlend#batchJoinBlacklist
 * @see SmsBlend#batchRemovalFromBlacklist
 * @see SmsBlend#joinInBlacklist
 * @see SmsBlend#removeFromBlacklist
 */
@Slf4j
public class BlackListRecordingMethodInterceptor implements SmsMethodInterceptor, SmsDaoAware {

    private static final String REDIS_KEY_PREFIX = "sms:blacklist:global";
    private static final String CONFIG_PROPERTIES_PREFIX = "sms:blacklist:global";

    private static final String JOIN_IN_BLACKLIST_METHOD = "joinInBlacklist";
    private static final String REMOVE_FROM_BLACKLIST_METHOD = "removeFromBlacklist";
    private static final String BATCH_JOIN_BLACKLIST_METHOD = "batchJoinBlacklist";
    private static final String BATCH_REMOVAL_FROM_BLACKLIST_METHOD = "batchRemovalFromBlacklist";

    @Setter
    private SmsDao smsDao;

    @Override
    public int getOrder(){
        return SmsProxyFactory.BLACK_LIST_RECORDING_METHOD_INTERCEPTOR_ORDER;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object[] beforeInvoke(SmsMethodType methodType, Method method, Object target, Object[] params) {
        // TODO 并发操作是否可以保证安全性？
        // TODO 不同的SmsBlend对象操作的确是同一份黑名单配置是否合理？
        // TODO 是否应该同时支持黑白名单？
        //添加到黑名单
        String methodName = method.getName();
        if (JOIN_IN_BLACKLIST_METHOD.equals(methodName)) {
            String cacheKey = REDIS_KEY_PREFIX;
            List<String> blackList = getBlackList(cacheKey);
            blackList.add((String) params[0]);
            flushBlackList(cacheKey,blackList);
        }
        //从黑名单移除
        else if (REMOVE_FROM_BLACKLIST_METHOD.equals(methodName)) {
            String cacheKey = REDIS_KEY_PREFIX;
            List<String> blackList = getBlackList(cacheKey);
            blackList.remove((String) params[0]);
            flushBlackList(cacheKey,blackList);
        }
        //批量添加到黑名单
        else if (BATCH_JOIN_BLACKLIST_METHOD.equals(methodName)) {
            String cacheKey = REDIS_KEY_PREFIX;
            List<String> blackList = getBlackList(cacheKey);
            blackList.addAll((List<String>) params[0]);
            flushBlackList(cacheKey,blackList);
        }
        //批量从黑名单移除
        else if (BATCH_REMOVAL_FROM_BLACKLIST_METHOD.equals(methodName)) {
            String cacheKey = REDIS_KEY_PREFIX;
            List<String> blackList = getBlackList(cacheKey);
            blackList.removeAll((List<String>) params[0]);
            flushBlackList(cacheKey, blackList);
        }
        return params;
    }

    @SuppressWarnings("unchecked")
    private List<String> getBlackList(String cacheKey) {
        List<String> blackList;
        Object cache = smsDao.get(cacheKey);
        if (null != cache) {
            blackList = (List<String>) cache;
            return blackList;
        }
        blackList = new ArrayList<>();
        smsDao.set(CONFIG_PROPERTIES_PREFIX, blackList);
        return blackList;
    }

    /**
     * 刷新黑名单
     *
     * @param cacheKey cacheKey
     * @param blackList 新的黑名单列表
     */
    private void flushBlackList(String cacheKey, List<String> blackList) {
        smsDao.set(cacheKey, blackList);
    }
}
