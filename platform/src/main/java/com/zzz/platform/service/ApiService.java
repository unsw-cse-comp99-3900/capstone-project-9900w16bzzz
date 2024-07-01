package com.zzz.platform.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
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

    public ResponseEntity<JSONObject> doPost(String url, HttpHeaders headers, Object body) {
        HttpEntity<Object> entity = new HttpEntity<>(body, headers);

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                entity,
                JSONObject.class
        );
    }

    public ResponseEntity<JSONObject> doPostJson(String url, HttpHeaders headers, Map<String, Object> body) {
        headers.setContentType(MediaType.APPLICATION_JSON);
        return doPost(url,headers, body);
    }

    public ResponseEntity<JSONObject> doPostList(String url, HttpHeaders headers, MultiValueMap<String,Object> body) {
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return doPost(url, headers, body);
    }
}
