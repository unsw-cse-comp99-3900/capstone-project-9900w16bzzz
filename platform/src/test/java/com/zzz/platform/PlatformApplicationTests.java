package com.zzz.platform;

import com.zzz.platform.service.CacheService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class PlatformApplicationTests {
    @Resource
    private CacheService cacheService;

    @Test
    void contextLoads() {
        cacheService.saveKey("test","test","value");

        System.out.println(cacheService.getValue("test","test"));
    }

}
