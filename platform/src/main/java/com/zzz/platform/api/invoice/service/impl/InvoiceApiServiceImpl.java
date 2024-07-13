package com.zzz.platform.api.invoice.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zzz.platform.api.invoice.domain.InvoiceApiJsonDTO;
import com.zzz.platform.api.invoice.domain.ValidateResultVO;
import com.zzz.platform.api.invoice.domain.api.EssInvoiceValidateForm;
import com.zzz.platform.api.invoice.domain.api.UpbrainExtractorForm;
import com.zzz.platform.api.invoice.service.InvoiceApiService;
import com.zzz.platform.common.code.InvoiceErrorCode;
import com.zzz.platform.common.domain.ResponseDTO;
import com.zzz.platform.service.ApiService;
import com.zzz.platform.service.CacheService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/1
 */
@Slf4j
@Service
public class InvoiceApiServiceImpl implements InvoiceApiService {

    private final static String API_TOKEN_CACHE_KEY = "api_token";

    @Resource
    private ApiService apiService;

    @Resource
    private CacheService cacheService;

    @Value("${upbrainsai.token}")
    private String upbrainsaiToken;

    @Resource
    private ObjectMapper objectMapper;

    /**
     * Using Upbrainsai extractors extract invoice info from pdf and convert to json
     * @param upbrainExtractorForm request body
     * @return invoice json object
     */
    @Override
    public ResponseDTO<InvoiceApiJsonDTO> convertPdfToJson(UpbrainExtractorForm upbrainExtractorForm) {
        String url = UpbrainSaiUrl.INVOICE_EXTRACTOR_URL.getUrl();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, upbrainsaiToken);
        ByteArrayResource resource;
        try {
            MultipartFile file = upbrainExtractorForm.getFile();
            resource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            };
        } catch (IOException e) {
            log.error("Reading file failed, {}", e.getMessage());
            return ResponseDTO.error(InvoiceErrorCode.INVOICE_FILE_FORMAT_ERROR);
        }

        MultiValueMap<String,Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource);
        // send post request to upbrainsai
        ResponseEntity<JSONObject> response = apiService.doPostList(url, headers, body);
        if(response.getStatusCode() == HttpStatus.OK && !ObjectUtils.isEmpty(response.getBody())){
            JSONObject jsonObject = JSONObject.parseObject(response.getBody().toString());
            try {
                InvoiceApiJsonDTO invoiceApiJsonDTO = objectMapper.readValue(jsonObject.get("result").toString(), InvoiceApiJsonDTO.class);
                return ResponseDTO.ok(invoiceApiJsonDTO);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        } else {
            log.error("Upbrain Extractor api request failed, response: {}", response);
        }

        return ResponseDTO.error(InvoiceErrorCode.UPBRAINSAI_API_REQUEST_FAILED);
    }

    /**
     *
     * @param essInvoiceValidateForm invoice validate parameters
     * @return validation report
     */
    @Override
    public ResponseDTO<ValidateResultVO> essValidateInvoice(EssInvoiceValidateForm essInvoiceValidateForm) {
        String apiToken = getApiToken();
        if (ObjectUtils.isEmpty(apiToken)) {
            return ResponseDTO.error(InvoiceErrorCode.GET_API_TOKEN_FAILED);
        }
        /* concat params with url */
        String urlWithParams = new StringBuilder(EssApiUrl.VALIDATION_URL.getUrl())
                .append("?rules=").append(essInvoiceValidateForm.getRules())
                .append("&customer=").append(essInvoiceValidateForm.getCustomer())
                .toString();
        /* build http body */
        HashMap<String, Object> body = new HashMap<>();
        body.put("filename",essInvoiceValidateForm.getFileName());
        body.put("content",essInvoiceValidateForm.getContent());
        body.put("checksum",essInvoiceValidateForm.getChecksum());
        /* build http header */
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<JSONObject> response = null;
        /* send POST request */
        for (int i = 0; i < 2; i++) {
            headers.add(HttpHeaders.AUTHORIZATION, apiToken);
            headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            response = apiService.doPostJson(urlWithParams, headers, body);
            HttpStatus statusCode = response.getStatusCode();
            if (statusCode.is2xxSuccessful()) {
                JSONObject jsonObject = response.getBody();
                try {
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

    private String getApiToken() {
        // get from cache, otherwise send POST request to api
        String apiToken = cacheService.getValue(API_TOKEN_CACHE_KEY);
        if (!ObjectUtils.isEmpty(apiToken)) {
            return apiToken;
        }
        String params = Stream.of(AuthTokenHeader.values())
                .map(header -> URLEncoder.encode(header.getKey(), StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(header.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        ResponseEntity<JSONObject> response = apiService.doPostParams(EssApiUrl.AUTH_TOKEN_URL.getUrl(), headers, params);
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

    private String buildRequestParameters() {
        return Stream.of(AuthTokenHeader.values())
                .map(header -> {
                    return URLEncoder.encode(header.getKey(), StandardCharsets.UTF_8) + "=" +
                            URLEncoder.encode(header.getValue(), StandardCharsets.UTF_8);
                })
                .collect(Collectors.joining("&"));
    }

    @AllArgsConstructor
    @Getter
    enum UpbrainSaiUrl {
        INVOICE_EXTRACTOR_URL("https://xtract.upbrainsai.com/api/invoice"),
        ;
        private final String url;
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
