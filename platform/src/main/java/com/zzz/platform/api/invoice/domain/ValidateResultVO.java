package com.zzz.platform.api.invoice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidateResultVO {

    private String customer;
    private boolean successful;
    private String message;
    private Report report;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Report {
        private boolean successful;
        private String summary;
        private String filename;
        private Map<String, ReportDetail> reports;
        private int firedAssertionErrorsCount;
        private List<String> allAssertionErrorCodes;
        private int firedSuccessfulReportsCount;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ReportDetail {
        private String rules;
        private boolean successful;
        private String summary;
        private List<AssertionError> firedAssertionErrors;
        private List<String> firedSuccessfulReports;
        private int firedSuccessfulReportsCount;
        private int firedAssertionErrorsCount;
        private List<String> firedAssertionErrorCodes;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssertionError {
        private String id;
        private String text;
        private String location;
        private String test;
        private String flag;
    }

}
