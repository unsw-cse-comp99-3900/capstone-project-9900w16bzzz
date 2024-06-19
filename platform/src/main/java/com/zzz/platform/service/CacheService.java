package com.zzz.platform.service;

import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

import javax.annotation.Resource;
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
        return Lists.newArrayList(caffeineCacheManager.getCacheNames());
    }

    /**
     * All keys under a particular cache
     *
     */
    public List<String> cacheKey(String cacheName) {
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if (cache == null) {
            return Lists.newArrayList();
        }
        Set<Object> cacheKey = cache.getNativeCache().asMap().keySet();
        return cacheKey.stream().map(e -> e.toString()).collect(Collectors.toList());
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
     * @param cacheName cache key
     * @param value value
     */
    public void saveKey(String cacheName, Object value) {
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            cache.put(cacheName, value);
        }
    }

    public String getValue(String cacheName) {
        CaffeineCache cache = (CaffeineCache) caffeineCacheManager.getCache(cacheName);
        if (cache != null) {
            Object object = cache.get(cacheName);
            if (object != null) {
                return object.toString();
            }
        }
        return "";
    }

}
