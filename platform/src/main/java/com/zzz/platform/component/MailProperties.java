package com.zzz.platform.component;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/13
 */
@Data
@Component
@ConfigurationProperties(prefix = "spring.mail")
public class MailProperties {
    private String host;
    private int port;
    private String username;
    private String password;
    private Properties properties = new Properties();
}
