package com.zzz.platform.config;


import com.zzz.platform.common.domain.SystemEnvironment;
import com.zzz.platform.common.enumeration.SystemEnvironmentEnum;
import com.zzz.platform.utils.EnumUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@Configuration
public class SystemEnvironmentConfig implements Condition {
    @Value("${spring.profiles.active}")
    private String systemEnvironment;

    @Value("${project.name}")
    private String projectName;

    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        return isDevOrTest(conditionContext);
    }

    /**
     * dev or test
     */
    private boolean isDevOrTest(ConditionContext conditionContext) {
        String property = conditionContext.getEnvironment().getProperty("spring.profiles.active");
        return StringUtils.isNotBlank(property) && (SystemEnvironmentEnum.TEST.equalsValue(property) || SystemEnvironmentEnum.DEV.equalsValue(property));
    }

    @Bean("systemEnvironment")
    public SystemEnvironment initEnvironment() {
        SystemEnvironmentEnum currentEnvironment = EnumUtil.getEnumByValue(systemEnvironment, SystemEnvironmentEnum.class);
        if (StringUtils.isBlank(projectName)) {
            throw new ExceptionInInitializerError("Can not get the project name, please fill the application.yml file：project.name");
        }
        return new SystemEnvironment(currentEnvironment == SystemEnvironmentEnum.PROD, projectName, currentEnvironment);
    }
}
