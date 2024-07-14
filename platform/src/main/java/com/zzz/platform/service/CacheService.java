package com.zzz.platform.service;

import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Service
public class CacheService {


    @Resource
    private CaffeineCacheManager caffeineCacheManager;

    /**
     * Get all cache names
     *
     */
    public List<String> cacheNames() {
        return new ArrayList<>(caffeineCacheManager.getCacheNames());
    }

    /**
     * All keys under a particular cache
     *
     */
    public List<String> cacheKeys(String cacheName) {
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if (cache == null) {
            return List.of();  // 使用Java 9及以上版本中的List.of()创建一个空列表
        }
        Set<Object> cacheKey = cache.getNativeCache().asMap().keySet();
        return cacheKey.stream().map(Object::toString).collect(Collectors.toList());
    }

    /**
     * remove cache
     *
     */
    public void removeCache(String cacheName) {
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        }
    }

    /**
     * save key to cache
     * @param cacheName cache name
     * @param key cache key
     * @param value value
     */
    public void saveKey(String cacheName, Object key, Object value) {
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(key, value);
        }
    }

    /**
     * Get value from cache by key
     * @param cacheName cache name
     * @param key cache key
     * @return value as string
     */
    public String getValue(String cacheName, Object key) {
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                Object value = valueWrapper.get();
                if (value != null) {
                    return value.toString();
                }
            }
        }
        return "";
    }
}