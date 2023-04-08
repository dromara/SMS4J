package kim.wind.sms.autoimmit.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.*;
import java.util.concurrent.TimeUnit;


@Slf4j
public class RedisUtils {

    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public void init(RedisConnectionFactory connectionFactory) {
        // 指定相应的序列化方案
        StringRedisSerializer keySerializer = new StringRedisSerializer();
        JdkSerializationRedisSerializer valueSerializer = new JdkSerializationRedisSerializer();
        // 构建StringRedisTemplate
        StringRedisTemplate stringTemplate = new StringRedisTemplate();
        stringTemplate.setConnectionFactory(connectionFactory);
        stringTemplate.afterPropertiesSet();
        // 构建RedisTemplate
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);
        template.afterPropertiesSet();
        this.redisTemplate = template;
    }

    public RedisUtils() {
    }

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 说明：设置redis的key的到期时间
     *
     * @param key  redis的key
     * @param time 到期时间
     * @name: setTimeByKey
     * @author :Wind
     */
    public boolean setTimeByKey(String key, Long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 说明：放入redis
     *
     * @param key   要放入的key
     * @param value 要放入的value
     * @name: set
     * @author :Wind
     */
    public boolean set(String key, Object value) {

        redisTemplate.opsForValue().set(key, value);
        return true;
    }

    /**
     * 说明：放入带过期时间的缓存
     *
     * @param time 到期时间(秒)
     * @name: setOrTime
     * @author :Wind
     */
    public boolean setOrTime(String key, Object value, Long time) {
        try {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <p>说明：将Map中的数据批量放置到redis中
     * <p>
     *
     * @param valueMap 要放入的数据
     * @name: multiSet
     * @author :Wind
     */
    public boolean multiSet(Map valueMap) {
        try {
            redisTemplate.opsForValue().multiSet(valueMap);
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

    /**
     * 说明：获取key对应的值
     *
     * @param key 要查询的key
     * @name: getByKey
     * @author :Wind
     */
    public Object getByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * <p>说明：获取字符串型值
     *
     * @param
     * @name: getKyeString
     * @author :Wind
     */
    public String getKyeString(String key) {
        return (String) getByKey(key);
    }

    /**
     * 说明：判断key是否存在
     *
     * @param key 要判断的key
     * @name: hasKey
     * @author :Wind
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 说明：根据key删除redis缓存可以批量删除
     *
     * @param key 要删除的key
     * @name: deleteKey
     * @author :Wind
     */
    public Boolean deleteKey(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                return redisTemplate.delete(key[0]);
            } else {
                Long delete = redisTemplate.delete(Arrays.asList(key));
                return delete >= 1L;
            }
        }
        return false;
    }

    public Boolean delete(String key) {
        Set<String> keys = redisTemplate.keys(key + "*");
        redisTemplate.delete(keys);
        return true;
    }

    /**
     * 根据key 获取key的过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回-1, 代表为永久有效
     */
    public Long getKeyExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 修改redis中key的名称
     *
     * @param oldKey 旧的key值
     * @param newKey 新的key值
     */
    public void renameKey(String oldKey, String newKey) {
        redisTemplate.rename(oldKey, newKey);
    }

    /**
     * <p>说明：将map对象存入redis
     * <p>
     *
     * @param map 要存入redis中的map
     * @name: setMap
     * @author :Wind
     */
    public <T, M> void MapSetMap(String key, Map<T, M> map) {
        redisTemplate.opsForHash().putAll(key, map);
    }

    /**
     * <p>说明：获取所有hash表中字段
     * <p>
     *
     * @param
     * @name: getMapByKey
     * @author :Wind
     */
    public Set<Object> MapGetHashByKey(String key) {
        return redisTemplate.opsForHash().keys(key);
    }

    /**
     * <p>说明：根据key和fieId获取对应的值
     * <p>
     *
     * @param fieId hash中的fieId也是Map的Key
     * @name: getValueByFieID
     * @author :Wind
     */
    public Object MapGetValueByFieID(String key, String fieId) {
        return redisTemplate.opsForHash().get(key, fieId);
    }

    /**
     * <p>说明：根据key获取所有的键值对
     * <p>
     *
     * @param key redis中的key
     * @name: getMapByKey
     * @author :Wind
     */
    public Map<Object, Object> MapGetMapByKey(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * <p>说明：向key中添加一对新的键值对
     * <p>
     *
     * @param hashKey 键值对的key
     * @param value   键值对的value
     * @name: setNewMapValue
     * @author :Wind
     */
    public void MapSetNewMapValue(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }

    /**
     * <p>说明：根据key和field删除数据
     * <p>
     *
     * @param fields 要删除的fields
     * @return Long 影响的条数
     * @name: hashDelete
     * @author :Wind
     */
    public Long MapHashDelete(String key, Object... fields) {
        return redisTemplate.opsForHash().delete(key, fields);
    }

    /**
     * <p>说明：查看key下存了多少条键值对
     * <p>
     *
     * @param key redis的key
     * @name: getMapValueSize
     * @author :Wind
     */
    public Long MapGetMapValueSize(String key) {
        return redisTemplate.opsForHash().size(key);
    }

    /**
     * 设置值到List中的头部
     *
     * @param key
     * @param value
     * @return
     * @author :Wind
     */
    public Boolean listAddInHead(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 批量设置值到List中的头部
     *
     * @param key    List名字
     * @param values
     * @return
     * @author :Wind
     */
    public Boolean listAddAllInHead(String key, Collection<?> values) {
        try {
            redisTemplate.opsForList().leftPushAll(key, values);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 如果存在List->key, 则设置值到List中的头部
     *
     * @param key   List名字
     * @param value
     * @return
     * @author :Wind
     */
    public Boolean listAddIfPresent(String key, Object value) {
        try {
            redisTemplate.opsForList().leftPushIfPresent(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 设置值到List中的尾部
     *
     * @param key   List名字
     * @param value 值
     * @return
     */
    public Boolean listAddInEnd(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 批量设置值到List中的尾部
     *
     * @param key    List名字
     * @param values 要设置的集合
     * @return
     */
    public Boolean listAddAllInEnd(String key, Collection<?> values) {
        try {
            redisTemplate.opsForList().rightPushAll(key, values);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }

    /**
     * 通过索引去设置List->key中的值
     *
     * @param key   redis的key
     * @param index 索引
     * @param value 值
     * @return
     * @author :Wind
     */
    public Boolean listAddByIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            return false;
        }
    }


    /**
     * 根据索引获取list中的值
     *
     * @param key   list名字
     * @param index
     * @return
     * @author :Wind
     */
    public Object listGetByIndex(String key, long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    /**
     * 根据索引范围获取list中的值
     *
     * @param key   list名字
     * @param start
     * @param end
     * @return
     * @author :Wind
     */
    public List<Object> listGetByRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 移除并获取列表中第一个元素(如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止)
     *
     * @param key list名字
     * @return
     * @author :Wind
     */
    public Object listLeftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 移除并获取列表中最后一个元素(如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止)
     *
     * @param key list名字
     * @return
     * @author :Wind
     */
    public Object listRightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    /**
     * <p>说明：获取列表元素的大小
     * <p>
     *
     * @param
     * @name: listGetSize
     * @author :Wind
     */
    public Long listGetSize(String key) {
        return redisTemplate.opsForList().size(key);
    }

    /**
     * 删除集合中值等于value的元素(
     * index=0, 删除所有值等于value的元素;
     * index>0, 从头部开始删除第一个值等于value的元素;
     * index<0, 从尾部开始删除第一个值等于value的元素)
     *
     * @param key
     * @param index
     * @param value
     * @return
     * @author :Wind
     */
    public Long listRemove(String key, long index, Object value) {
        return redisTemplate.opsForList().remove(key, index, value);
    }

    /**
     * <p>说明：清除所有缓存
     * <p><b>该方法会清理掉redis中所有的缓存,谨慎使用</b>
     * <p>
     *
     * @name: empty
     * @author :Wind
     */
    public void empty() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
