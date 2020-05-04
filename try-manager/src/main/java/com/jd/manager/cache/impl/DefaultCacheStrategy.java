package com.jd.manager.cache.impl;

import com.jd.manager.cache.CacheStrategy;

import java.util.HashMap;
import java.util.Map;

public class DefaultCacheStrategy implements CacheStrategy{

    private static Map defaultCache = new HashMap<String, Object>();

    @Override
    public Object readCache(String key) {
        return defaultCache.get(key);
    }

    @Override
    public boolean writeCache(String key, Object value) {
        defaultCache.put(key, value);
        return true;
    }
}
