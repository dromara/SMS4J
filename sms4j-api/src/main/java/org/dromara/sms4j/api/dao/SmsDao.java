package org.dromara.sms4j.api.dao;

/**
 * DAO 接口
 *
 * @author Wind
 * @author Charles7c
 * @since 2023/8/5 20:03
 */
public interface SmsDao {

    /**
     * 存储
     *
     * @param key       键
     * @param value     值
     * @param cacheTime 缓存时间（单位：秒)
     */
    void set(String key, Object value, long cacheTime);

    /**
     * 存储
     *
     * @param key   键
     * @param value 值
     */
    void set(String key, Object value);

    /**
     * 读取
     *
     * @param key 键
     * @return 值
     */
    Object get(String key);

    /**
     * 清空
     */
    void clean();
}
