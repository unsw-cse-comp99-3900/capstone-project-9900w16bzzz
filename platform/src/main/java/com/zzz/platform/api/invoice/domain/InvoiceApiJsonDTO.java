package com.zzz.platform.api.invoice.domain;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
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
public class InvoiceApiJsonDTO {

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
        private Fields fields;
        private List<Item> items;
        private String content;

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Fields {
            @JsonProperty("AmountDue")
            private Field amountDue;

            @JsonProperty("CustomerAddress")
            private Field customerAddress;

            @JsonProperty("CustomerId")
            private Field customerId;

            @JsonProperty("CustomerName")
            private Field customerName;

            @JsonProperty("DueDate")
            private Field dueDate;

            @JsonProperty("InvoiceDate")
            private Field invoiceDate;

            @JsonProperty("InvoiceId")
            private Field invoiceId;

            @JsonProperty("InvoiceTotal")
            private Field invoiceTotal;

            @JsonProperty("SubTotal")
            private Field subTotal;

            @JsonProperty("RemittanceAddress")
            private Field remittanceAddress;

            @JsonProperty("RemittanceAddressRecipient")
            private Field remittanceAddressRecipient;

            @JsonProperty("ServiceAddress")
            private Field serviceAddress;

            @JsonProperty("VendorAddress")
            private Field vendorAddress;

            @JsonProperty("VendorName")
            private Field vendorName;

            @JsonProperty("PreviousUnpaidBalance")
            private Field previousUnpaidBalance;

            @JsonProperty("ServiceEndDate")
            private Field serviceEndDate;

            @JsonProperty("ServiceStartDate")
            private Field serviceStartDate;

            @JsonProperty("TotalDiscount")
            private Field totalDiscount;

            @JsonProperty("TotalTax")
            private Field totalTax;

            @JsonProperty("PaymentTerm")
            private Field paymentTerm;

            @JsonProperty("ShippingAddress")
            private Field shippingAddress;

            @JsonProperty("ShippingAddressRecipient")
            private Field shippingAddressRecipient;

            @JsonProperty("BillingAddress")
            private Field billingAddress;

            @JsonProperty("BillingAddressRecipient")
            private Field billingAddressRecipient;

            private Map<String, Field> additionalProperties = new HashMap<>();

            @JsonAnySetter
            public void setAdditionalProperty(String name, Field value) {
                this.additionalProperties.put(name, value);
            }
        }



        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Item {
            @JsonProperty("Amount")
            private Field amount;
            @JsonProperty("Description")
            private Field description;
            @JsonProperty("Quantity")
            private Field quantity;
            @JsonProperty("Unit")
            private Field unit;
            @JsonProperty("UnitPrice")
            private Field unitPrice;
            @JsonProperty("Tax")
            private Field tax;
            @JsonProperty("TaxRate")
            private Field taxRate;
            @JsonProperty("page_number")
            private String pageNumber;

            private Map<String, Field> additionalProperties = new HashMap<>();
            @JsonAnySetter
            public void setAdditionalProperty(String name, Field value) {
                this.additionalProperties.put(name, value);
            }
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Field {
            private String content;
            @JsonProperty("field_name")
            private String fieldName;
            private String type;
            @JsonProperty("page_number")
            private String pageNumber;
            private Map<String,Object> details;

        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Table {
        private List<Object> tables;
    }

    @Getter
    @AllArgsConstructor
    public enum Details {
        // currency
        AMOUNT("amount"),
        SYMBOL("symbol"),
        CODE("code"),
        // date
        DAY("day"),
        MONTH("month"),
        YEAR("year"),
        // address
        STREET_ADDRESS("street_address"),
        POSTAL_CODE("postal_code"),
        CITY("city"),
        COUNTRY_REGION("country_region"),
        ;
        private final String val;
    }

}
