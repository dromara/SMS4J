package org.dromara.sms4j.api.universal;

/**
 * RedisUtil
 * <p> redis工具接口，用户可自主实现以更换redis的来源
 *@since 2.2.0
 * @author :Wind
 * 2023/6/6  22:21
 **/
public interface RedisUtil {

    /**
     *  setOrTime
     * <p>设置带有过期时间的key
     * @param key redis的key
     * @param value redis 的value
     * @param time 过期时间（秒级单位）
     * @author :Wind
    */
    public boolean setOrTime(String key, Object value, Long time);

    /**
     *  getByKey
     * <p>根据key获取redis中缓存的数据
     * @param key redis的key
     * @author :Wind
    */
    public Object getByKey(String key);
}
