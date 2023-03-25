package kim.wind.sms.comm.utils;


import kim.wind.sms.comm.exception.SmsBlendException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>类名: TimeExpiredPoolCache
 * <p>说明：  一个自实现的内部缓存，可用于无法使用redis的场景
 * @author :Wind
 * 2023/3/25  18:26
 **/
public class TimeExpiredPoolCache {
    private static long defaultCachedMillis = 24 * 60 * 60 * 1000L;//过期时间默认24小时
    private static long timerMillis = 30 * 1000L;//定时清理默认1分钟
    /**
     * 对象池
     */
    private static ConcurrentHashMap<String, DataWrapper<?>> dataPool = null;
    /**
     * 对象单例
     */
    private static TimeExpiredPoolCache instance = null;

    private TimeExpiredPoolCache() {
        dataPool = new ConcurrentHashMap<String, DataWrapper<?>>();
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new TimeExpiredPoolCache();
            initTimer();
        }
    }

    public static TimeExpiredPoolCache getInstance() {
        if (instance == null) {
            syncInit();
        }
        return instance;
    }

    /**
     * 定时器定时清理过期缓存
     */
    private static Timer timer = new Timer();

    private static void initTimer() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    clearExpiredCaches();
                } catch (Exception e) {
                    throw new SmsBlendException(e.getMessage());
                }
            }
        }, timerMillis, timerMillis);
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
        DataWrapper<T> dataWrapper = (DataWrapper<T>) dataPool.get(key);
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
            dataWrapper = new DataWrapper<T>(data, cachedMillis);
            dataPool.put(key, dataWrapper);
        }
        return data;
    }

    /**
     * 直接设置缓存值和时间
     */
    @SuppressWarnings("unchecked")
    public <T> T put(String key, T data, long cachedMillis) throws Exception {
        DataWrapper<T> dataWrapper = (DataWrapper<T>) dataPool.get(key);
        if (dataWrapper != null) {
            //更新
            dataWrapper.update(data, cachedMillis);
        } else {
            dataWrapper = new DataWrapper<T>(data, cachedMillis);
            dataPool.put(key, dataWrapper);
        }
        return data;
    }

    /**
     * 默认构造时间的缓存数据
     */
    @Deprecated
    public <T> T put(String key, T data, DataRenewer<T> dataRenewer) throws Exception {
        return put(key, data, defaultCachedMillis, dataRenewer);
    }

    /**
     * 获取缓存
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, long cachedMillis, DataRenewer<T> dataRenewer) throws Exception {
        DataWrapper<T> dataWrapper = (DataWrapper<T>) dataPool.get(key);
        if (dataWrapper != null && !dataWrapper.isExpired()) {
            return dataWrapper.data;
        }
        return put(key, null, cachedMillis, dataRenewer);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        DataWrapper<T> dataWrapper = (DataWrapper<T>) dataPool.get(key);
        if (dataWrapper != null && !dataWrapper.isExpired()) {
            return dataWrapper.data;
        }
        return null;
    }

    /**
     * 清除缓存
     */
    public void clear() {
        dataPool.clear();
    }

    /**
     * 删除指定key的value
     */
    public void remove(String key) {
        dataPool.remove(key);
    }

    /**
     * 数据封装
     */
    private class DataWrapper<T> {
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

    /**
     * 数据构造
     */
    public interface DataRenewer<T> {
        public T renewData();
    }

    /**
     * 清除过期的缓存
     */
    private static void clearExpiredCaches() {
        List<String> expiredKeyList = new LinkedList<String>();

        for (Entry<String, DataWrapper<?>> entry : dataPool.entrySet()) {
            if (entry.getValue().isExpired()) {
                expiredKeyList.add(entry.getKey());
            }
        }
        for (String key : expiredKeyList) {
            dataPool.remove(key);
        }
    }
}
