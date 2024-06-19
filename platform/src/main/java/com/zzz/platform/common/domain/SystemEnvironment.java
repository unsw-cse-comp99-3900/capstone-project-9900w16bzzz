package com.zzz.platform.common.domain;

import com.zzz.platform.common.enumeration.SystemEnvironmentEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/19
 */
@AllArgsConstructor
@Getter
public class SystemEnvironment {


    /**
     * is prod
     */
    private boolean isProd;

    /**
     * project name
     */
    private String projectName;

    /**
     * current environment
     */
    private SystemEnvironmentEnum currentEnvironment;
}
