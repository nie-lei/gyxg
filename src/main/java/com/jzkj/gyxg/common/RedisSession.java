package com.jzkj.gyxg.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class RedisSession {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RedisTemplate<Object, Map<String, Object>> myRedisTemplateMap;

    @Autowired
    private RedisTemplate<Object, List> myRedisTemplateList;

    private HashOperations getHashOperations() {
        return redisTemplate.opsForHash();
    }

    private ValueOperations getValueOperations() {
        return redisTemplate.opsForValue();
    }

    /**
     * 根据key获取Map
     * @param key
     * @return
     */
    public Map<String, Object> getMapValue(String key) {
        return myRedisTemplateMap.opsForValue().get(key);
    }

    public Map<String, Object> getMapValue(String key, long expire, TimeUnit timeUnit) {
        setMapExpire(key, expire, timeUnit);
        return myRedisTemplateMap.opsForValue().get(key);
    }

    public void setMapValue(String key, Map<String, Object> value) {
        myRedisTemplateMap.opsForValue().set(key, value);
    }

    public void setMapExpire(String key, long expire, TimeUnit timeUnit) {
        myRedisTemplateMap.expire(key, expire, timeUnit);
    }

    public void setMapValue(String key, Map<String, Object> value, long expire, TimeUnit timeUnit) {
        setMapValue(key, value);
        setMapExpire(key, expire, timeUnit);
    }

    /**
     * 根据key获取Map
     * @param key
     * @return
     */
    public List getListValue(String key) {
        return myRedisTemplateList.opsForValue().get(key);
    }
    public List getListValue(String key, long expire, TimeUnit timeUnit) {
        setListExpire(key, expire, timeUnit);
        return myRedisTemplateList.opsForValue().get(key);
    }

    public void setListValue(String key, List value) {
        myRedisTemplateList.opsForValue().set(key, value);
    }

    public void setListExpire(String key, long expire, TimeUnit timeUnit) {
        myRedisTemplateList.expire(key, expire, timeUnit);
    }

    public void setListValue(String key, List value, long expire, TimeUnit timeUnit) {
        setListValue(key, value);
        setListExpire(key, expire, timeUnit);
    }

    /**
     * key-value  获取 value
     *
     * @param key
     * @return
     */
    public Object getValue(String key) {
        return getValueOperations().get(key);
    }

    /**
     * key-value  获取 value 并设置失效时间
     *
     * @param key
     * @param expire
     * @param timeUnit
     * @return
     */
    @SuppressWarnings("unchecked")
    public Object getValue(String key, long expire, TimeUnit timeUnit) {
        Object obj = getValueOperations().get(key);
        redisTemplate.expire(key, expire, timeUnit);
        return obj;
    }

    /**
     * key-value  设置 key value
     *
     * @param key
     * @param value
     */
    @SuppressWarnings("unchecked")
    public void setValue(String key, Object value) {
        getValueOperations().set(key, value);
    }

    /**
     * key-value  设置 key value 并设置失效日期
     *
     * @param key
     * @param value
     * @param expire
     * @param timeUnit
     */
    @SuppressWarnings("unchecked")
    public void setValue(String key, Object value, long expire, TimeUnit timeUnit) {
        getValueOperations().set(key, value);
        redisTemplate.expire(key, expire, timeUnit);
    }

    @SuppressWarnings("unchecked")
    public boolean hasValueKey(String key) {
        Object obj = getValueOperations().get(key);
        return obj != null;
    }


    @SuppressWarnings("unchecked")
    public boolean setIfAbsent(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    /**
     * 同步锁
     *
     * @param key
     * @param seconds 时长
     * @return
     */
    public boolean lock(String key, long seconds) {
        boolean b = redisTemplate.opsForValue().setIfAbsent(key, "1");
        if (b) {
            redisTemplate.expire(key, seconds, TimeUnit.SECONDS);
        }
        return b;
    }

    @SuppressWarnings("unchecked")
    public void unlock(String key, int type) {
        if(type == 1) {
            myRedisTemplateMap.delete(key);
        } else if( type == 2) {
            myRedisTemplateList.delete(key);
        } else {
            redisTemplate.delete(key);
        }
    }




}
