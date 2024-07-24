package com.zzz.platform.api.invoice.converter;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zzz.platform.api.invoice.domain.InvoiceApiJsonDTO;
import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/11
 */
public class InvoiceJsonDtoToVoConverter {

    public static InvoiceJsonVO convert(InvoiceApiJsonDTO invoiceApiJsonDTO) {
        InvoiceApiJsonDTO.Document document = invoiceApiJsonDTO.getDocuments().get(0);
        InvoiceApiJsonDTO.Document.Fields fields = document.getFields();

        InvoiceJsonVO invoiceJsonVO = new InvoiceJsonVO();

        // set invoice id

        invoiceJsonVO.setInvoiceId(getContent(fields.getInvoiceId()));
        // set invoice and due date
        invoiceJsonVO.setInvoiceDate(extractDate(fields.getInvoiceDate()));
        invoiceJsonVO.setDueDate(extractDate(fields.getDueDate()));

        // set default commercial invoice coed
        invoiceJsonVO.setTypeCode("380");

        String countryCode = getDetails(fields.getInvoiceTotal(), InvoiceApiJsonDTO.Details.CODE);
        invoiceJsonVO.setCurrencyCode(countryCode);

        // set buyer
        InvoiceApiJsonDTO.Document.Field customerAddress = null;
        if (ObjectUtils.isNotEmpty(fields.getCustomerAddress())) {
            customerAddress = fields.getCustomerAddress();
        } else if (ObjectUtils.isNotEmpty(fields.getRemittanceAddress())) {
            customerAddress = fields.getRemittanceAddress();
        } else if (ObjectUtils.isNotEmpty(fields.getServiceAddress())) {
            customerAddress = fields.getServiceAddress();
        }
        InvoiceJsonVO.Person buyer = extractPerson(fields.getCustomerName(), customerAddress);
        invoiceJsonVO.setBuyer(buyer);

        // set seller
        InvoiceJsonVO.Person seller = extractPerson(fields.getVendorName(), fields.getVendorAddress());
        seller.setId(getContent(fields.getCustomerId()));
        invoiceJsonVO.setSeller(seller);

        // set invoice total
        InvoiceApiJsonDTO.Document.Field invoiceTotal = fields.getInvoiceTotal();
        invoiceJsonVO.setInvoiceTotal(getDetails(invoiceTotal, InvoiceApiJsonDTO.Details.AMOUNT));

        // set sub-total
        InvoiceApiJsonDTO.Document.Field subTotal = fields.getSubTotal();
        if (ObjectUtils.isNotEmpty(subTotal)) {
            invoiceJsonVO.setSubTotal(getDetails(subTotal, InvoiceApiJsonDTO.Details.AMOUNT));
        } else if (ObjectUtils.isNotEmpty(invoiceTotal)) {
            invoiceJsonVO.setSubTotal(getDetails(invoiceTotal, InvoiceApiJsonDTO.Details.AMOUNT));
        }
        // set total tax
        InvoiceApiJsonDTO.Document.Field totalTax = fields.getTotalTax();
        invoiceJsonVO.setTaxTotal(getDetails(totalTax, InvoiceApiJsonDTO.Details.AMOUNT));

        // set allowance
        InvoiceApiJsonDTO.Document.Field totalDiscount = fields.getTotalDiscount();
        InvoiceJsonVO.Allowance allowance = new InvoiceJsonVO.Allowance();
        allowance.setAmount(getDetails(totalDiscount, InvoiceApiJsonDTO.Details.AMOUNT));
        allowance.setCurrencyCode(getDetails(totalDiscount, InvoiceApiJsonDTO.Details.CODE));
        // reason and taxPercent
        invoiceJsonVO.setAllowance(allowance);

        // set payment details
        /* InvoiceApiJsonDTO.Document.Field paymentTerm = fields.getPaymentTerm();
        if (ObjectUtils.isNotEmpty(paymentTerm)) {
            InvoiceJsonVO.Payment payment = new InvoiceJsonVO.Payment();
            // set Payment details
            invoiceJsonVO.setPayment(payment);
        } */
        InvoiceJsonVO.Payment payment = new InvoiceJsonVO.Payment();
        // set Payment details
        invoiceJsonVO.setPayment(payment);


        // set delivery address
        InvoiceApiJsonDTO.Document.Field shippingAddress = fields.getShippingAddress();
        if (ObjectUtils.isNotEmpty(shippingAddress)) {
            InvoiceJsonVO.Address deliveryAddr = extractAddress(shippingAddress);
            invoiceJsonVO.setDeliveryAddress(deliveryAddr);
        }

        // set items
        setItems(document.getItems(), invoiceJsonVO);

        return invoiceJsonVO;
    }

    private static LocalDate extractDate(InvoiceApiJsonDTO.Document.Field dateDetail) {
        if (ObjectUtils.isEmpty(dateDetail)) {
            return null;
        }
        return LocalDate.of(
                Integer.parseInt(Objects.requireNonNull(getDetails(dateDetail, InvoiceApiJsonDTO.Details.YEAR))),
                Integer.parseInt(Objects.requireNonNull(getDetails(dateDetail, InvoiceApiJsonDTO.Details.MONTH))),
                Integer.parseInt(Objects.requireNonNull(getDetails(dateDetail, InvoiceApiJsonDTO.Details.DAY)))
        );
    }

    private static InvoiceJsonVO.Person extractPerson(InvoiceApiJsonDTO.Document.Field personDetail, InvoiceApiJsonDTO.Document.Field addrDetail) {
        InvoiceJsonVO.Person person = new InvoiceJsonVO.Person();
        person.setName(getContent(personDetail));
        InvoiceJsonVO.Address address = extractAddress(addrDetail);
        person.setAddress(address);
        return person;
    }

    private static InvoiceJsonVO.Address extractAddress(InvoiceApiJsonDTO.Document.Field addressDetail) {
        InvoiceJsonVO.Address address = new InvoiceJsonVO.Address();
        address.setAddress(getContent(addressDetail));
        address.setStreet(getDetails(addressDetail, InvoiceApiJsonDTO.Details.STREET_ADDRESS));
        address.setPostalCode(getDetails(addressDetail, InvoiceApiJsonDTO.Details.POSTAL_CODE));
        address.setCity(getDetails(addressDetail, InvoiceApiJsonDTO.Details.CITY));
        address.setCountryCode(getDetails(addressDetail, InvoiceApiJsonDTO.Details.COUNTRY_REGION));
        return address;
    }

    private static void setItems(List<InvoiceApiJsonDTO.Document.Item> jsonItems, InvoiceJsonVO invoiceJsonVO) {
        ArrayList<InvoiceJsonVO.Item> ublItems = new ArrayList<>();
        int index = 0;
        for (InvoiceApiJsonDTO.Document.Item item : jsonItems) {
            InvoiceJsonVO.Item itemUblVO = new InvoiceJsonVO.Item();
            itemUblVO.setId(String.valueOf(index++));
            itemUblVO.setAmount(getDetails(item.getAmount(), InvoiceApiJsonDTO.Details.AMOUNT));
            itemUblVO.setCurrencyCode(getDetails(item.getAmount(), InvoiceApiJsonDTO.Details.CODE));
            itemUblVO.setDescription(getContent(item.getDescription()));
            itemUblVO.setQuantity(getContent(item.getQuantity()));
            itemUblVO.setUnit(getContent(item.getUnit()));
            itemUblVO.setUnitPrice(getDetails(item.getUnitPrice(), InvoiceApiJsonDTO.Details.AMOUNT));
            itemUblVO.setTax(getContent(item.getTax()));
            itemUblVO.setTaxRate(getContent(item.getTaxRate()));
            // set allowance
            itemUblVO.setAllowance(new InvoiceJsonVO.Allowance());
            ublItems.add(itemUblVO);
        }
        invoiceJsonVO.setInvoiceLine(ublItems);
    }


    private static String getContent(InvoiceApiJsonDTO.Document.Field field) {
        if (ObjectUtils.isNotEmpty(field)) {
            return field.getContent();
        } else {
            return null;
        }
    }

    private static String getDetails(InvoiceApiJsonDTO.Document.Field field, InvoiceApiJsonDTO.Details details) {
        if (ObjectUtils.isEmpty(field)) {
            return null;
        }
        Object object = field.getDetails().get(details.getVal());
        return String.valueOf(object);
    }

}
