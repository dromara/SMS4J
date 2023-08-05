package org.dromara.sms4j.api.smsProxy;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.universal.SmsRestrictedUtil;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>类名: TimeExpiredPoolCache
 * <p>说明：  一个自实现的内部缓存，可用于无法使用redis的场景
 *
 * @author :Wind
 * 2023/3/25  18:26
 **/
@Slf4j
public class TimeExpiredPoolCache implements SmsRestrictedUtil {

    private TimeExpiredPoolCache poolCache = TimeExpiredPoolCache.getInstance();
    /**
     * 过期时间（默认 24 小时）
     */
    private static final long DEFAULT_CACHED_MILLIS = 24 * 60 * 60 * 1000L;
    /**
     * 定时清理（默认 1 分钟）
     */
    private static final long TIMER_MILLIS = 30 * 1000L;
    /**
     * 对象池
     */
    private static ConcurrentHashMap<String, DataWrapper<?>> DATA_POOL = null;
    /**
     * 对象单例
     */
    private static TimeExpiredPoolCache INSTANCE = null;
    /**
     * 定时器定时清理过期缓存
     */
    private static final Timer TIMER = new Timer();

    private TimeExpiredPoolCache() {
    }

    private static synchronized void syncInit() {
        if (INSTANCE == null) {
            INSTANCE = new TimeExpiredPoolCache();
            DATA_POOL = new ConcurrentHashMap<>();
            initTimer();
        }
    }

    public static TimeExpiredPoolCache getInstance() {
        if (INSTANCE == null) {
            syncInit();
        }
        return INSTANCE;
    }

    private static void initTimer() {
        TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    clearExpiredCaches();
                } catch (Exception e) {
                    log.error(e.getMessage());
                    throw new SmsBlendException(e.getMessage());
                }
            }
        }, TIMER_MILLIS, TIMER_MILLIS);
    }

    /**
     * 清除过期的缓存
     */
    private static void clearExpiredCaches() {
        List<String> expiredKeyList = new LinkedList<>();
        for (Entry<String, DataWrapper<?>> entry : DATA_POOL.entrySet()) {
            if (entry.getValue().isExpired()) {
                expiredKeyList.add(entry.getKey());
            }
        }
        for (String key : expiredKeyList) {
            DATA_POOL.remove(key);
        }
    }

    /**
     * 缓存数据
     *
     * @param key          key值
     * @param data         缓存数据
     * @param cachedMillis 过期时间
     * @param dataRenewer  刷新数据
     */
    @SuppressWarnings("unchecked")
    public <T> T put(String key, T data, long cachedMillis, DataRenewer<T> dataRenewer) throws Exception {
        DataWrapper<T> dataWrapper = (DataWrapper<T>) DATA_POOL.get(key);
        if (data == null && dataRenewer != null) {
            data = dataRenewer.renewData();
        }
        //当重新获取数据为空，直接返回不做put
        if (data == null) {
            return null;
        }
        if (dataWrapper != null) {
            //更新
            dataWrapper.update(data, cachedMillis);
        } else {
            dataWrapper = new DataWrapper<>(data, cachedMillis);
            DATA_POOL.put(key, dataWrapper);
        }
        return data;
    }

    /**
     * 直接设置缓存值和时间
     */
    @SuppressWarnings("unchecked")
    public <T> T put(String key, T data, long cachedMillis) throws Exception {
        DataWrapper<T> dataWrapper = (DataWrapper<T>) DATA_POOL.get(key);
        if (dataWrapper != null) {
            //更新
            dataWrapper.update(data, cachedMillis);
        } else {
            dataWrapper = new DataWrapper<T>(data, cachedMillis);
            DATA_POOL.put(key, dataWrapper);
        }
        return data;
    }

    /**
     * 默认构造时间的缓存数据
     */
    @Deprecated
    public <T> T put(String key, T data, DataRenewer<T> dataRenewer) throws Exception {
        return put(key, data, DEFAULT_CACHED_MILLIS, dataRenewer);
    }

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, long cachedMillis, DataRenewer<T> dataRenewer) throws Exception {
        DataWrapper<T> dataWrapper = (DataWrapper<T>) DATA_POOL.get(key);
        if (dataWrapper != null && !dataWrapper.isExpired()) {
            return dataWrapper.data;
        }
        return put(key, null, cachedMillis, dataRenewer);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        DataWrapper<T> dataWrapper = (DataWrapper<T>) DATA_POOL.get(key);
        if (dataWrapper != null && !dataWrapper.isExpired()) {
            return dataWrapper.data;
        }
        return null;
    }

    @Override
    public void clean() throws RuntimeException {
        DATA_POOL.clear();
    }

    /**
     * 删除指定key的value
     */
    public void remove(String key) {
        DATA_POOL.remove(key);
    }

    @Override
    public boolean setOrTime(String key, Object value, Long time) {
        try {
            poolCache.put(key, value, time);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public boolean set(String key, Object value) {
        try {
            poolCache.put(key, value, DEFAULT_CACHED_MILLIS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public Object getByKey(String key) {
        return null;
    }

    /**
     * 数据构造
     */
    public interface DataRenewer<T> {
        public T renewData();
    }

    /**
     * 数据封装
     */
    private static class DataWrapper<T> {
        /**
         * 数据
         */
        private T data;
        /**
         * 到期时间
         */
        private long expiredTime;
        /**
         * 缓存时间
         */
        private long cachedMillis;

        private DataWrapper(T data, long cachedMillis) {
            this.update(data, cachedMillis);
        }

        public void update(T data, long cachedMillis) {
            this.data = data;
            this.cachedMillis = cachedMillis;
            this.updateExpiredTime();
        }

        public void updateExpiredTime() {
            this.expiredTime = System.currentTimeMillis() + cachedMillis;
        }

        /**
         * 数据是否过期
         */
        public boolean isExpired() {
            if (this.expiredTime > 0) {
                return System.currentTimeMillis() > this.expiredTime;
            }
            return true;
        }
    }
}
