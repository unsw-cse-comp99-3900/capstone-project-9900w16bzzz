package com.zzz.platform.listener;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.URLUtil;
import com.zzz.platform.common.enumeration.SystemEnvironmentEnum;
import com.zzz.platform.utils.EnumUtil;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Slf4j
@Component
@Order(value = 1024)
public class WebServerListener implements ApplicationListener<WebServerInitializedEvent> {

    @Override
    public void onApplicationEvent(@NotNull WebServerInitializedEvent webServerInitializedEvent) {
        // show project details
        showProjectMessage(webServerInitializedEvent);
    }

    /**
     * show project details
     */
    private void showProjectMessage(WebServerInitializedEvent webServerInitializedEvent) {
        WebServerApplicationContext context = webServerInitializedEvent.getApplicationContext();
        Environment env = context.getEnvironment();

        // get service information
        String ip = NetUtil.getLocalhost().getHostAddress();
        Integer port = webServerInitializedEvent.getWebServer().getPort();
        String contextPath = env.getProperty("server.servlet.context-path");
        if (contextPath == null) {
            contextPath = "";
        }
        String profile = env.getProperty("spring.profiles.active");
        SystemEnvironmentEnum environmentEnum = EnumUtil.getEnumByValue(profile, SystemEnvironmentEnum.class);
        String projectName = env.getProperty("project.name");
        //splicing service address
        String title = String.format("-------------【%s】 The service has been successfully started （%s started successfully）-------------", projectName, projectName);

        String localhostUrl = URLUtil.normalize(String.format("http://localhost:%d%s", port, contextPath), false, true);
        String externalUrl = URLUtil.normalize(String.format("http://%s:%d%s", ip, port, contextPath), false, true);
        String swaggerUrl = URLUtil.normalize(String.format("http://localhost:%d%s/swagger-ui/index.html", port, contextPath), false, true);
        log.warn("\n{}\n" +
                        "\tCurrent startup environment:\t{} , {}" +
                        "\n\tservice-native address:\t{}" +
                        "\n\tservice extranet address:\t{}" +
                        "\n\tSwagger address:\t{}" +
                        "\n-------------------------------------------------------------------------------------\n",
                title, profile, environmentEnum.getDesc(), localhostUrl, externalUrl, swaggerUrl);
    }
}
