package com.zzz.platform;

import com.zzz.platform.listener.LogVariableListener;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableCaching
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
@MapperScan
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class PlatformApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(PlatformApplication.class);
        // Add a log listener so that log4j2-spring.xml can indirectly read the properties of the configuration file.
        application.addListeners(new LogVariableListener());
        application.run(args);
    }
}
