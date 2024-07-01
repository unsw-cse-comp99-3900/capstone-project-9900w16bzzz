package com.zzz.platform.api.invoice.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/6/22
 */
@Data
@AllArgsConstructor
public class ValidateResultVO {

    private String customer;
    private boolean successful;
    private String message;
    private Report report;

    @Data
    @AllArgsConstructor
    public static class Report {
        private boolean successful;
        private String summary;
        private String filename;
        private Map<String, RuleReport> reports;
        private int firedAssertionErrorsCount;
        private List<String> allAssertionErrorCodes;
        private int firedSuccessfulReportsCount;
    }

    @Data
    @AllArgsConstructor
    public static class RuleReport {
        private String rules;
        private boolean successful;
        private String summary;
        private List<String> firedAssertionErrors;
        private List<String> firedSuccessfulReports;
        private int firedAssertionErrorsCount;
        private int firedSuccessfulReportsCount;
        private List<String> firedAssertionErrorCodes;
    }

}
