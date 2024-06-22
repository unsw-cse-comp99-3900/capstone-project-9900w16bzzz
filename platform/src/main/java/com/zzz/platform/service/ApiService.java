package com.zzz.platform.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/22
 */
@Service
public class ApiService {

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<JSONObject> doPost(String url, HttpHeaders headers, Map<String,Object> body) {
        headers.set(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.set(HttpHeaders.CONNECTION, "keep-alive");
        HttpEntity<Map<String,Object>> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                JSONObject.class
        );
    }
}
