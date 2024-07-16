package com.zzz.platform;

import com.zzz.platform.service.CacheService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class PlatformApplicationTests {
    @Resource
    private CacheService cacheService;

}
