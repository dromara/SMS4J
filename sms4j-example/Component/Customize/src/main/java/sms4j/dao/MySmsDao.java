package sms4j.dao;

import org.dromara.sms4j.api.dao.SmsDao;
import org.dromara.sms4j.api.dao.SmsDaoDefaultImpl;
import org.dromara.sms4j.comm.exception.SmsBlendException;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Data;


//最简单的SmsDao实现
public class MySmsDao implements SmsDao {
    {
        System.out.println("加载了自定义实现的SmsDao");
    }
    private static volatile MySmsDao INSTANCE;
    private static final Timer TIMER = new Timer();
    private static final ConcurrentHashMap<String, DataWrapper> DATA_MAP = new ConcurrentHashMap<>();

    /**
     * 缓存时间（单位：毫秒，默认 24 小时）
     */
    private static final long DEFAULT_CACHE_TIME = 24 * 60 * 60 * 1000L;

    /**
     * 定时器执行频率（单位：毫秒，默认 30 秒）
     */
    private static final long TIMER_INTERVAL = 30 * 1000L;

    private MySmsDao() {}

    /**
     * 获取唯一实例
     *
     * @return 唯一实例
     */
    public static MySmsDao getInstance() {
        if (null == INSTANCE) {
            synchronized (MySmsDao.class) {
                if (null == INSTANCE) {
                    INSTANCE = new MySmsDao();
                    // 初始化定时器
                    initTimer();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public void set(String key, Object value, long cacheTime) {
        System.out.println("存入："+key+" - "+value);

        cacheTime = cacheTime * 1000L;
        DataWrapper dataWrapper = DATA_MAP.get(key);
        if (null != dataWrapper) {
            dataWrapper.update(value, cacheTime);
        } else {
            dataWrapper = new DataWrapper(value, cacheTime);
            DATA_MAP.put(key, dataWrapper);
        }
    }

    @Override
    public void set(String key, Object value) {
        this.set(key, value, DEFAULT_CACHE_TIME);
    }

    @Override
    public Object get(String key) {
        System.out.println("获取："+key);

        DataWrapper dataWrapper = DATA_MAP.get(key);
        if (dataWrapper != null && !dataWrapper.isExpired()) {
            Object data = dataWrapper.data;
            System.out.println(data);
            return data;
        }
        return null;
    }

    @Override
    public Object remove(String key) {
        return DATA_MAP.remove(key);
    }

    @Override
    public void clean() {
        DATA_MAP.clear();
    }

    /**
     * 初始化定时器
     */
    private static void initTimer() {
        TIMER.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    clearExpiredData();
                } catch (Exception e) {
                    throw new SmsBlendException(e.getMessage());
                }
            }
        }, TIMER_INTERVAL, TIMER_INTERVAL);
    }

    /**
     * 清除过期数据
     */
    private static void clearExpiredData() {
        List<String> expiredKeyList = new LinkedList<>();
        for (Map.Entry<String, DataWrapper> entry : DATA_MAP.entrySet()) {
            if (entry.getValue().isExpired()) {
                expiredKeyList.add(entry.getKey());
            }
        }
        for (String key : expiredKeyList) {
            DATA_MAP.remove(key);
        }
    }

    /**
     * 数据封装
     */
    @Data
    private static class DataWrapper {

        /**
         * 数据
         */
        private Object data;

        /**
         * 过期时间
         */
        private long expiredTime;

        /**
         * 缓存时间
         */
        private long cacheTime;

        private DataWrapper(Object data, long cacheTime) {
            this.update(data, cacheTime);
        }

        /**
         * 更新数据及缓存时间
         *
         * @param data      数据
         * @param cacheTime 缓存时间
         */
        public void update(Object data, long cacheTime) {
            this.data = data;
            this.cacheTime = cacheTime;
            this.expiredTime = System.currentTimeMillis() + cacheTime;
        }

        /**
         * 数据是否过期
         *
         * @return true：过期，false：未过期
         */
        public boolean isExpired() {
            if (this.expiredTime > 0) {
                return System.currentTimeMillis() > this.expiredTime;
            }
            return true;
        }
    }
}
