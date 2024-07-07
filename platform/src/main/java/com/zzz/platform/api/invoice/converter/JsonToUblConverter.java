package com.zzz.platform.api.invoice.converter;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.*;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.*;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/3
 */
public class JsonToUblConverter {

    public static InvoiceType convertToUbl(InvoiceJsonVO invoiceJsonVO) {
        InvoiceType invoice = new InvoiceType();
        InvoiceJsonVO.Document document = invoiceJsonVO.getDocuments().get(0);
        InvoiceJsonVO.Document.Fields fields = document.getFields();

        // set invoice ID
        IDType id = new IDType();
        id.setSchemeID(fields.getInvoiceId().getContent());
        invoice.setID(id);

        // set issue date
        IssueDateType issueDate = new IssueDateType();
        issueDate.setValue(LocalDate.parse(fields.getInvoiceDate().getContent(),DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        invoice.setIssueDate(issueDate);

        DueDateType dueDate = new DueDateType();
        dueDate.setValue(LocalDate.parse(fields.getDueDate().getContent(),DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale.ENGLISH)));
        invoice.setDueDate(dueDate);

        // set accounting customer party
        setAccountingCustomerParty(invoice, fields);

        // set accounting supplier name
        setAccountingSupplierParty(invoice, fields);

        // set payable amount type
        setLegalMonetaryTotal(invoice, fields);

        TaxTotalType taxTotal = new TaxTotalType();
        TaxAmountType taxAmount = new TaxAmountType();
        taxAmount.setValue(BigDecimal.valueOf((Double)fields.getTotalTax().getDetails().get(InvoiceJsonVO.Details.AMOUNT.getVal())));
        taxAmount.setCurrencyID(fields.getTotalTax().getDetails().get(InvoiceJsonVO.Details.CODE.getVal()).toString());
        invoice.addTaxTotal(taxTotal);


        int index = 1;
        // set document item
        for (InvoiceJsonVO.Document.Item item : document.getItems()) {
            InvoiceLineType invoiceLine = new InvoiceLineType();
            invoiceLine.setID(String.valueOf(index++));
            // set item description
            ItemType itemType = new ItemType();
            DescriptionType description = new DescriptionType();
            description.setValue(item.getDescription().getContent());
            itemType.setDescription(List.of(description));
            invoiceLine.setItem(itemType);

            // set item price
            PriceType price = new PriceType();
            PriceAmountType priceAmount = new PriceAmountType();
            priceAmount.setValue(BigDecimal.valueOf((Double)item.getAmount().getDetails().get(InvoiceJsonVO.Details.AMOUNT.getVal())));
            String currencyID = item.getAmount().getDetails().get(InvoiceJsonVO.Details.CODE.getVal()).toString();
            priceAmount.setCurrencyID(currencyID);
            price.setPriceAmount(priceAmount);

            // invoiceLine.setLineExtensionAmount(BigDecimal.TEN).setCurrencyID(currencyID);
            invoiceLine.setPrice(price);

            // set item tax
            TaxTotalType taxTotalType = new TaxTotalType();
            TaxAmountType taxAmountType = new TaxAmountType();
            taxAmountType.setValue(BigDecimal.valueOf((Double)item.getTax().getDetails().get(InvoiceJsonVO.Details.AMOUNT.getVal())));
            taxAmountType.setCurrencyID(item.getTax().getDetails().get(InvoiceJsonVO.Details.CODE.getVal()).toString());
            taxTotalType.setTaxAmount(taxAmountType);
            invoiceLine.addTaxTotal(taxTotalType);

            // add invoice item to item
            invoice.addInvoiceLine(invoiceLine);
        }

        return invoice;
    }

    private static void setAccountingCustomerParty(InvoiceType invoice, InvoiceJsonVO.Document.Fields cutomerPartyFields) {
        // set accounting customer name
        PartyType accountingCustomerParty = new PartyType();
        PartyNameType partyName = new PartyNameType();
        NameType customerName = new NameType();
        customerName.setValue(cutomerPartyFields.getCustomerName().getContent());
        partyName.setName(customerName);
        accountingCustomerParty.setPartyName(Collections.singletonList(partyName));
        // Address
        if (ObjectUtils.isNotEmpty(cutomerPartyFields.getCustomerAddress())) {
            AddressType addressType = new AddressType();
            AddressLineType addressLineType = new AddressLineType();
            addressLineType.setLine(cutomerPartyFields.getCustomerAddress().getContent());
            addressType.addAddressLine(addressLineType);
            accountingCustomerParty.setPostalAddress(addressType);
        }
        // construct CustomerPartyType
        CustomerPartyType customerPartyType = new CustomerPartyType();
        customerPartyType.setParty(accountingCustomerParty);

        invoice.setAccountingCustomerParty(customerPartyType);
    }

    private static void setAccountingSupplierParty(InvoiceType invoice, InvoiceJsonVO.Document.Fields supplierPartyFields) {
        PartyType accountingSupplierParty = new PartyType();
        PartyNameType supplierPartyName = new PartyNameType();
        NameType vendorName = new NameType();
        vendorName.setValue(supplierPartyFields.getVendorName().getContent());
        supplierPartyName.setName((vendorName));
        accountingSupplierParty.setPartyName(Collections.singletonList(supplierPartyName));
        // address
        AddressType addressType = new AddressType();
        AddressLineType addressLineType = new AddressLineType();
        addressLineType.setLine(supplierPartyFields.getVendorAddress().getContent());
        addressType.addAddressLine(addressLineType);
        accountingSupplierParty.setPostalAddress(addressType);
        // construct SupplierPartyType
        SupplierPartyType supplierPartyType = new SupplierPartyType();
        supplierPartyType.setParty(accountingSupplierParty);

        invoice.setAccountingSupplierParty(supplierPartyType);
    }

    private static void setLegalMonetaryTotal(InvoiceType invoice, InvoiceJsonVO.Document.Fields legalMonetaryTotalFields) {
        MonetaryTotalType legalMonetaryTotal = new MonetaryTotalType();
        PayableAmountType payableAmount = new PayableAmountType();
        payableAmount.setValue(BigDecimal.valueOf((Double)legalMonetaryTotalFields.getInvoiceTotal().getDetails().get(InvoiceJsonVO.Details.AMOUNT.getVal())));
        payableAmount.setCurrencyID(legalMonetaryTotalFields.getInvoiceTotal().getDetails().get(InvoiceJsonVO.Details.CODE.getVal()).toString());
        legalMonetaryTotal.setPayableAmount(payableAmount);
        //legalMonetaryTotal.setTaxInclusiveAmount()
        invoice.setLegalMonetaryTotal(legalMonetaryTotal);
    }



}
