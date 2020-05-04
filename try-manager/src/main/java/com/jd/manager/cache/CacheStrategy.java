package com.jd.manager.cache;

public interface CacheStrategy {
    Object readCache(String key);
    boolean writeCache(String key, Object value);
}
