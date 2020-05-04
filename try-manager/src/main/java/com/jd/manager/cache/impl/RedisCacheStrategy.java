package com.jd.manager.cache.impl;

import com.jd.utils.redis.RedisUtil;
import com.jd.utils.serialize.SerializeUtil;
import redis.clients.jedis.Jedis;

public class RedisCacheStrategy extends DefaultCacheStrategy {

    private static Jedis jedisCache = RedisUtil.getJedis();

    @Override
    public Object readCache(String key) {
        if (null == jedisCache){
            return null;
        }
        byte[] bytes = jedisCache.get(key.getBytes());
        if (null == bytes){
            return null;
        }
        return SerializeUtil.deserialize(bytes);
    }

    @Override
    public boolean writeCache(String key, Object value) {
        if (null == jedisCache || null == value){
            return false;
        }
        jedisCache.set(key.getBytes(), SerializeUtil.serizlize(value));
        return true;
    }
}
