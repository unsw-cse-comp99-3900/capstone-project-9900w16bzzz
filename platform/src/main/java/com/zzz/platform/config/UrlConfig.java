package com.zzz.platform.config;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.hutool.core.util.StrUtil;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zzz.platform.common.annoation.NoNeedLogin;
import com.zzz.platform.common.domain.RequestUrlVO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/21
 */
@Configuration
@Slf4j
public class UrlConfig {

    @Resource
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    /**
     * Get the request path for each method
     */
    @Bean
    public Map<Method, Set<String>> methodUrlMap() {
        Map<Method, Set<String>> methodUrlMap = Maps.newHashMap();
        // Getting the url with class and method correspondences
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : map.entrySet()) {
            RequestMappingInfo requestMappingInfo = entry.getKey();
            if(requestMappingInfo.getPatternsCondition() == null){
                continue;
            }

            Set<String> urls = requestMappingInfo.getPatternsCondition().getPatterns();
            if (CollectionUtils.isEmpty(urls)) {
                continue;
            }
            HandlerMethod handlerMethod = entry.getValue();
            methodUrlMap.put(handlerMethod.getMethod(), urls);
        }
        return methodUrlMap;
    }

    /**
     * Methods that require url permission checking
     *
     * @param methodUrlMap
     * @return
     */
    @Bean
    public List<RequestUrlVO> authUrl(Map<Method, Set<String>> methodUrlMap) {
        List<RequestUrlVO> authUrlList = Lists.newArrayList();
        for (Map.Entry<Method, Set<String>> entry : methodUrlMap.entrySet()) {
            Method method = entry.getKey();
            // ignore authentication
            SaIgnore ignore = method.getAnnotation(SaIgnore.class);
            if (null != ignore) {
                continue;
            }
            NoNeedLogin noNeedLogin = method.getAnnotation(NoNeedLogin.class);
            if (null != noNeedLogin) {
                continue;
            }
            Set<String> urlSet = entry.getValue();
            List<RequestUrlVO> requestUrlList = this.buildRequestUrl(method, urlSet);
            authUrlList.addAll(requestUrlList);
        }
        return authUrlList;
    }

    private List<RequestUrlVO> buildRequestUrl(Method method, Set<String> urlSet) {
        List<RequestUrlVO> requestUrlList = Lists.newArrayList();
        if (CollectionUtils.isEmpty(urlSet)) {
            return requestUrlList;
        }
        //The name of the method corresponding to the url
        String className = method.getDeclaringClass().getName();
        String methodName = method.getName();
        List<String> list = StrUtil.split(className, ".");
        String controllerName = list.get(list.size() - 1);
        String name = controllerName + "." + methodName;
        //swagger comment
        String methodComment = null;
        Operation apiOperation = method.getAnnotation(Operation.class);
        if (apiOperation != null) {
            methodComment = apiOperation.summary();
        }
        for (String url : urlSet) {
            RequestUrlVO requestUrlVO = new RequestUrlVO();
            requestUrlVO.setUrl(url);
            requestUrlVO.setName(name);
            requestUrlVO.setComment(methodComment);
            requestUrlList.add(requestUrlVO);
        }
        return requestUrlList;
    }


    /**
     * Get information about urls that can be accessed anonymously without logging in.
     *
     * @return
     */
    @Bean
    public List<String> noNeedLoginUrlList(Map<Method, Set<String>> methodUrlMap) {
        List<String> noNeedLoginUrlList = Lists.newArrayList();
        for (Map.Entry<Method, Set<String>> entry : methodUrlMap.entrySet()) {
            Method method = entry.getKey();
            NoNeedLogin noNeedLogin = method.getAnnotation(NoNeedLogin.class);
            if (null == noNeedLogin) {
                continue;
            }
            noNeedLoginUrlList.addAll(entry.getValue());
        }
        log.info("URLs that do not require login：{}", noNeedLoginUrlList);
        return noNeedLoginUrlList;
    }
}
