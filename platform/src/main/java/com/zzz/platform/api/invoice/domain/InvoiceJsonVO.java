package com.zzz.platform.api.invoice.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/10
 */
@Data
public class InvoiceJsonVO {

    private String invoiceId;

    private LocalDate invoiceDate;

    private LocalDate dueDate;

    @Schema(defaultValue = "380 commercial invoice")
    private String typeCode;

    private String currencyCode;

    private Person buyer;

    private Person seller;

    private String subTotal;

    private String invoiceTotal;

    private String taxTotal;

    private Allowance allowance;

    private List<Item> invoiceLine;

    private Payment payment;

    private Address deliveryAddress;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Person {
        private String id;
        @Schema(allowableValues = {
                "GLN", "0060", "VATDE", "ABN", "EORI",
                "LEI", "SWIFT", "GSRN", "NACE", "NIF"
        })
        private String schemeId;
        private String name;
        private Address address;
        private String phone;
        private String mail;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Address {
        private String address;
        private String street;
        private String postalCode;
        private String city;
        private String countryCode;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Payment {
        /**
         * @see com.zzz.platform.api.invoice.converter.JsonToUblConverter.PaymentMeansCode
         */
        @Schema(allowableValues = {"10", "20", "30", "42", "48"})
        private String code;
        private String accountName;
        private String accountNumber;
        private String bsbNumber;
        private String paymentNote;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Allowance {
        /**
         * @see com.zzz.platform.api.invoice.converter.JsonToUblConverter.AllowanceChargeReasonCode
         */
        @Schema(allowableValues = {
                "SAA", "AA", "AB", "AC", "AD", "AE", "AF",
                "AG", "AH", "AI", "AJ", "AK", "AL", "AM", "AN"
        })
        private String type;
        private String amount;
        private String reason;
        private String taxPercent;
        private String currencyCode;

        public Allowance(String type) {
            this.type = amount;
            this.amount = "10";
            this.reason = "SAA";
            this.taxPercent = "10";
            this.currencyCode = "AUD";
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Item {
        private String id;
        private String amount;
        private String currencyCode;
        private String description;
        private String quantity;
        private String unit;
        private String unitPrice;
        private String tax;
        private String taxRate;
        private Allowance allowance;
    }
}
