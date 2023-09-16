package org.dromara.sms4j.solon.utils;

import lombok.extern.slf4j.Slf4j;
import org.dromara.sms4j.api.dao.SmsDao;
import org.noear.solon.Solon;
import org.redisson.api.RedissonClient;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SmsRedisDaoImpl implements SmsDao {

    private RedissonClient redisTemplate;

    public SmsRedisDaoImpl() {
        Thread t = new Thread(()->{
            //如果获取到的bean为null则等待后重试，最多重试五次
            for(int i = 0; i < 5 ;i++){
                RedissonClient bean = Solon.context().getBean(RedissonClient.class);
                if (Objects.isNull(bean)){
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    redisTemplate = bean;
                    return;
                }
            }
        });
        t.start();
    }

    public SmsRedisDaoImpl(RedissonClient redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void set(String key, Object value, long cacheTime) {
        redisTemplate.getBucket(key).set(value, cacheTime, TimeUnit.SECONDS);
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.getBucket(key).set(value);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.getBucket(key).get();
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
            valueMap.forEach((key, val) -> redisTemplate.getBucket((String) key).set(val));
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }

    @Override
    public void clean() throws RuntimeException {
        //TODO
    }
}
