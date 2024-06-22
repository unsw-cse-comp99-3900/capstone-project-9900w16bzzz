package com.zzz.platform.api.invoice.service;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzz.platform.api.invoice.domain.ValidateForm;
import com.zzz.platform.api.invoice.domain.ValidateResultVO;
import com.zzz.platform.common.code.InvoiceErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.service.ApiService;
import com.zzz.platform.service.CacheService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/22
 */
@Service
@Slf4j
public class InvoiceValidateService {

    private final static String API_TOKEN_CACHE_KEY = "api_token";
    @Resource
    private ApiService apiService;

    @Resource
    private CacheService cacheService;

    /**
     *
     * @param validateForm invoice validate parameters
     * @return
     */
    public ResponseDTO<ValidateResultVO> invoiceValidation(ValidateForm validateForm) {
        String apiToken = getApiToken();
        if (ObjectUtils.isEmpty(apiToken)) {
            return ResponseDTO.error(InvoiceErrorCode.GET_API_TOKEN_FAILED);
        }
        /* concat params with url */
        String urlWithParams = new StringBuilder(EssApiUrl.VALIDATION_URL.getUrl())
                .append("?rules=").append(validateForm.getRules())
                .append("&customer=").append(validateForm.getCustomer())
                .toString();
        /* check content md5 */
        String content = validateForm.getContent();
        String md5sum = validateForm.getMd5sum();
        if (!md5sum.equals(DigestUtils.md5Hex(content))) {
            return ResponseDTO.error(InvoiceErrorCode.CONTENT_MD5_NOT_EQUAL);
        }
        /* build http body */
        HashMap<String, Object> body = new HashMap<>();
        body.put("filename",validateForm.getFilename());
        body.put("content",content);
        body.put("checksum",md5sum);
        /* build http header */
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<JSONObject> response = null;
        /* send POST request */
        for (int i = 0; i < 2; i++) {
            headers.add(HttpHeaders.AUTHORIZATION, apiToken);
            response = apiService.doPost(urlWithParams, headers, body);
            HttpStatus statusCode = response.getStatusCode();
            if (statusCode.is2xxSuccessful()) {
                JSONObject jsonObject = response.getBody();
                try {
                    ObjectMapper objectMapper = new ObjectMapper();
                    ValidateResultVO validateResultVO = objectMapper.readValue(jsonObject.toString(), ValidateResultVO.class);
                    return ResponseDTO.ok(validateResultVO);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                return ResponseDTO.error(InvoiceErrorCode.API_VALIDATION_REQUEST_FAILED);
            } else if (statusCode.is4xxClientError()) {
                /* API token authenticate failed, try to get a new token, then doPost once */
                log.info("API token invalid, try again...");
                cacheService.removeCache(API_TOKEN_CACHE_KEY);
                apiToken = getApiToken();
            }
        }
        log.error("Request failed, http response is {}", response);
        return ResponseDTO.error(InvoiceErrorCode.API_VALIDATION_REQUEST_FAILED);

    }

    public String getApiToken() {
        // get from cache, otherwise send POST request to api
        String apiToken = cacheService.getValue(API_TOKEN_CACHE_KEY);
        if (!ObjectUtils.isEmpty(apiToken)) {
            return apiToken;
        }
        Map<String, Object> body = Stream.of(AuthTokenHeader.values())
                .collect(Collectors.toMap(AuthTokenHeader::getKey, AuthTokenHeader::getValue));

        ResponseEntity<JSONObject> response = apiService.doPost(EssApiUrl.AUTH_TOKEN_URL.getUrl(), new HttpHeaders(), body);
        HttpStatus statusCode = response.getStatusCode();
        if (statusCode.is2xxSuccessful()) {
            JSONObject jsonObject = response.getBody();
            apiToken = jsonObject.get("access_token").toString();
            apiToken = "Bearer\n" + apiToken;
            cacheService.saveKey("api_token", apiToken);
            return apiToken;
        } else {
            log.error("Getting auth token failed");
            return null;
        }
    }


    @AllArgsConstructor
    @Getter
    enum EssApiUrl {
        AUTH_TOKEN_URL("https://dev-eat.auth.eu-central-1.amazoncognito.com/oauth2/token"),
        VALIDATION_URL("https://services.ebusiness-cloud.com/ess-schematron/v1/api/validate"),
        ;
        private final String url;
    }

    @AllArgsConstructor
    @Getter
    enum AuthTokenHeader {
        GRANT_TYPE("grant_type", "client_credentials"),
        CLIENT_ID("client_id", "7d30bi87iptegbrf2bp37p42gg"),
        CLIENT_SECRET("client_secret", "880tema3rvh3h63j4nquvgoh0lgts11n09bq8597fgrkvvd62su"),
        SCOPE("scope", "eat/read");
        ;
        private final String key;
        private final String value;
    }
}
