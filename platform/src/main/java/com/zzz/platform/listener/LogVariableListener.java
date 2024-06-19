package com.zzz.platform.listener;

import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Order(value = LoggingApplicationListener.DEFAULT_ORDER - 1)
public class LogVariableListener implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    private static final String LOG_DIRECTORY = "project.log-directory";

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent applicationEvent) {

        ConfigurableEnvironment environment = applicationEvent.getEnvironment();
        String filePath = environment.getProperty(LOG_DIRECTORY);
        if (filePath != null) {
            System.setProperty(LOG_DIRECTORY, filePath);
        }
    }
}
