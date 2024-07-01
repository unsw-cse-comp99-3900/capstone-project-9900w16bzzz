package com.zzz.platform.api.invoice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/1
 */
@AllArgsConstructor
@Data
@NoArgsConstructor
public class InvoiceJsonVO {

    @JsonProperty("file_name")
    private String fileName;
    @JsonProperty("document_type")
    private String documentType;
    private List<Page> pages;
    private List<Document> documents;
    private List<Table> tables;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Page {
        @JsonProperty("page_number")
        private int pageNumber;
        @JsonProperty("incomplete_processed")
        private boolean incompleteProcessed;
        private String text;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Document {
        private Map<String, Object> fields;
        private List<Map<String,Object>> items;
        private String content;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Table {
        private List<Object> tables;
    }


}
