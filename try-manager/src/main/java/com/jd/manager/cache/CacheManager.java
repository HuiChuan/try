package com.jd.manager.cache;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class CacheManager {
    private CacheStrategy cacheStrategy;

    public Object getObject(String key){
        try {
            return cacheStrategy.readCache(key);
        }catch (Exception e){
            log.error("CacheManager getObject error happens！key=" + key, e);
        }
        return null;
    }

    public boolean setObject(String key, Object value){
        try {
            return cacheStrategy.writeCache(key, value);
        }catch (Exception e){
            log.error("CacheManager setObject error happens！key=" + key, e);
        }
        return false;
    }
}
