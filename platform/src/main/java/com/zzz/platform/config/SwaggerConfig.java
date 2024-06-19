package com.zzz.platform.config;

import com.zzz.platform.common.constant.RequestHeaderConst;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Slf4j
@Configuration
@EnableSwagger2
@Conditional(SystemEnvironmentConfig.class)
public class SwaggerConfig {

    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/swagger-ui.html/**",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/doc.html",
    };

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(components())
                .info(new Info()
                        .title("EazyInvoice API Documents")
                        .contact(new Contact().name("ZZZGroup"))
                        .version("v1.X")
                )
                .addSecurityItem(new SecurityRequirement().addList(RequestHeaderConst.TOKEN));
    }

    private Components components() {
        return new Components()
                .addSecuritySchemes(RequestHeaderConst.TOKEN, new SecurityScheme().type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.HEADER).name(RequestHeaderConst.TOKEN));
    }

    @Bean
    public GroupedOpenApi businessApi() {
        return GroupedOpenApi.builder()
                .group("server api")
                .pathsToMatch("/**")
                .build();

    }

}
