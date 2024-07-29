package com.zzz.platform.api.invoice.converter;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.zzz.platform.api.invoice.domain.InvoiceJsonVO;
import com.zzz.platform.common.enumeration.BaseEnum;
import com.zzz.platform.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_21.*;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_21.*;
import oasis.names.specification.ubl.schema.xsd.invoice_21.InvoiceType;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author: zuoming yan
 * @version: v1.0.0
 * @date: 2024/7/3
 */
public class JsonToUblConverter {

    public static InvoiceType convertToUbl(InvoiceJsonVO invoiceJsonVO) {
        InvoiceType invoice = new InvoiceType();

        // set invoice ID
        IDType id = new IDType();
        id.setValue(invoiceJsonVO.getInvoiceId());
        invoice.setID(id);
        String customizationId = invoiceJsonVO.getCustomizationId();
        invoice.setCustomizationID(customizationId);
        invoice.setProfileID(invoiceJsonVO.getProfileId());

        // 380 Commercial Invoice Type Code
        invoice.setInvoiceTypeCode("380");
        // set currency code
        invoice.setDocumentCurrencyCode(invoiceJsonVO.getCurrencyCode());
        invoice.setBuyerReference("Commercial details");

        // set issue and due date
        invoice.setIssueDate(invoiceJsonVO.getInvoiceDate());
        invoice.setDueDate(invoiceJsonVO.getDueDate());

        // set accounting customer party
        setAccountingCustomerParty(invoice, invoiceJsonVO.getBuyer());

        // set accounting supplier name
        setAccountingSupplierParty(invoice, invoiceJsonVO.getSeller());

        // set Legal Monetary amount
        setLegalMonetaryTotal(invoice, invoiceJsonVO);

        // set tax total
        setTaxTotal(invoice, invoiceJsonVO);

        // set delivery address
        if (ObjectUtils.isNotEmpty(invoiceJsonVO.getDeliveryAddress())) {
            setDelivery(invoice, invoiceJsonVO.getDeliveryAddress(), invoiceJsonVO.getBuyer());
        }

        // set Payment means
        if (ObjectUtils.isNotEmpty(invoiceJsonVO.getPayment())) {
            setPaymentMeans(invoice, invoiceJsonVO.getPayment());
        }

        // set Allowance charge
        AllowanceChargeType allowanceChargeType = getAllowanceCharge(invoiceJsonVO.getAllowance());
        invoice.addAllowanceCharge(allowanceChargeType);

        // set invoice lines
        setInvoiceLines(invoice, invoiceJsonVO.getInvoiceLine());
        return invoice;
    }

    /**
    <cac:InvoiceLine>
       <cbc:ID>1</cbc:ID>
       <cbc:Note>Texts Giving More Info about the Invoice Line</cbc:Note>
       <cbc:InvoicedQuantity unitCode="E99">10</cbc:InvoicedQuantity>
       <cbc:LineExtensionAmount currencyID= "AUD">299.90</cbc:LineExtensionAmount>

        <cac:Item>
            <cbc:Description>Widgets True and Fair</cbc:Description>
            <cbc:Name>True-Widgets</cbc:Name>
            <cac:ClassifiedTaxCategory>
                <cbc:ID>S</cbc:ID>
                <cbc:Percent>10</cbc:Percent>
                <cac:TaxScheme>
                    <cbc:ID>GST</cbc:ID>
                </cac:TaxScheme>
            </cac:ClassifiedTaxCategory>
        </cac:Item>

       <cac:Price>
           <cbc:PriceAmount currencyID="AUD">29.99</cbc:PriceAmount>
           <cac:AllowanceCharge>
              <cbc:ChargeIndicator>false</cbc:ChargeIndicator>
              <cbc:Amount currencyID="AUD">0.00</cbc:Amount>
              <cbc:BaseAmount currencyID="AUD">29.99</cbc:BaseAmount>
           </cac:AllowanceCharge>
       </cac:Price>
    </cac:InvoiceLine>
    */
    private static void setInvoiceLines(InvoiceType invoice, List<InvoiceJsonVO.Item> items) {
        for (InvoiceJsonVO.Item item : items) {
            String currencyCode = item.getCurrencyCode();
            InvoiceLineType invoiceLine = new InvoiceLineType();
            invoiceLine.setID(item.getId());
            // set line extension amount
            LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType();
            lineExtensionAmountType.setCurrencyID(currencyCode);

            lineExtensionAmountType.setValue(BigDecimal.valueOf(Double.parseDouble((item.getAmount()))));
            invoiceLine.setLineExtensionAmount(lineExtensionAmountType);

            InvoicedQuantityType invoicedQuantityType = new InvoicedQuantityType();
            invoicedQuantityType.setUnitCode("C62");
            invoicedQuantityType.setValue(BigDecimal.valueOf(Double.parseDouble(item.getQuantity())));
            invoiceLine.setInvoicedQuantity(invoicedQuantityType);
            // set item
            ItemType itemType = new ItemType();
            DescriptionType description = new DescriptionType();
            description.setValue(item.getDescription());
            itemType.setDescription(List.of(description));
            String taxRate = item.getTaxRate();
            TaxCategoryType taxCategoryType = getTaxCategoryType(EnumUtil.getEnumByValue(taxRate, TaxType.class));
            itemType.addClassifiedTaxCategory(taxCategoryType);
            itemType.setName(item.getDescription());
            invoiceLine.setItem(itemType);

            PriceType price = new PriceType();
            PriceAmountType priceAmount = new PriceAmountType();
            priceAmount.setValue(BigDecimal.valueOf(Double.parseDouble(item.getUnitPrice())));
            priceAmount.setCurrencyID(currencyCode);
            price.setPriceAmount(priceAmount);

            if (ObjectUtils.isNotEmpty(item.getAllowance())) {
                // set allowance charge
                AllowanceChargeType allowanceChargeType = getAllowanceCharge(item.getAllowance());
                price.addAllowanceCharge(allowanceChargeType);
            }
            invoiceLine.setPrice(price);

            // add invoice item to item
            invoice.addInvoiceLine(invoiceLine);
        }
    }

    /**
    <cac:AccountingCustomerParty>
        <cac:Party>
            <cbc:EndpointID schemeID="0151">91888222000</cbc:EndpointID>
            <cac:PartyIdentification>
                <cbc:ID schemeID="0151">91888222000</cbc:ID>
            </cac:PartyIdentification>
            <cac:PartyName>
                <cbc:Name>Trotters Trading Co Ltd</cbc:Name>
            </cac:PartyName>
            <cac:PostalAddress>
                <...></>
            </cac:PostalAddress>
            <cac:PartyTaxScheme>
                <cbc:CompanyID>91888222000</cbc:CompanyID>
                <cac:TaxScheme>
                    <cbc:ID>GST</cbc:ID>
                </cac:TaxScheme>
            </cac:PartyTaxScheme>
            <cac:PartyLegalEntity>
                <cbc:RegistrationName>Buyer Official Name</cbc:RegistrationName>
                <cbc:CompanyID schemeID="0151">91888222000</cbc:CompanyID>
            </cac:PartyLegalEntity>
            <cac:Contact>
                <...></>
            </cac:Contact>
        </cac:Party>
    </cac:AccountingCustomerParty>
    */
    private static void setAccountingCustomerParty(InvoiceType invoice, InvoiceJsonVO.Person person) {
        PartyType partyType = getPartyType(person);
        // construct CustomerPartyType
        CustomerPartyType customerPartyType = new CustomerPartyType();
        customerPartyType.setParty(partyType);

        invoice.setAccountingCustomerParty(customerPartyType);
    }
    /**
     <cac:AccountingSupplierParty>
        <cac:Party>
            <cbc:EndpointID schemeID="0151">47555222000</cbc:EndpointID>
            <cac:PartyIdentification>
                <cbc:ID>47555222000</cbc:ID>
            </cac:PartyIdentification>
            <cac:PartyName>
                <cbc:Name>Supplier Trading Name Ltd</cbc:Name>
            </cac:PartyName>
            <cac:PostalAddress>
                <...></>
            </cac:PostalAddress>
            <cac:PartyTaxScheme>
                <cbc:CompanyID>47555222000</cbc:CompanyID>
                <cac:TaxScheme>
                    <cbc:ID>GST</cbc:ID>
                </cac:TaxScheme>
            </cac:PartyTaxScheme>

            <cac:Contact>
                <...></>
            </cac:Contact>
        </cac:Party>
    </cac:AccountingSupplierParty>
    */
    private static void setAccountingSupplierParty(InvoiceType invoice, InvoiceJsonVO.Person person) {
        PartyType partyType = getPartyType(person);
        // construct SupplierPartyType
        SupplierPartyType supplierPartyType = new SupplierPartyType();
        supplierPartyType.setParty(partyType);

        invoice.setAccountingSupplierParty(supplierPartyType);
    }

   /**
    <cac:Party>
      <cbc:EndpointID schemeID="0151">123456789</cbc:EndpointID>
      <cac:PartyIdentification>
        <cbc:ID schemeID="0060">123456789</cbc:ID>
      </cac:PartyIdentification>
      <cac:PartyName>
        <cbc:Name>Customer Company Name</cbc:Name>
      </cac:PartyName>
      <cac:PostalAddress>
        <cbc:StreetName>Main St</cbc:StreetName>
        <cbc:CityName>Springfield</cbc:CityName>
        <cbc:PostalZone>12345</cbc:PostalZone>
        <cac:AddressLine>
          <cbc:Line>123 Main St</cbc:Line>
        </cac:AddressLine>
        <cac:Country>
          <cbc:IdentificationCode>US</cbc:IdentificationCode>
        </cac:Country>
      </cac:PostalAddress>
      <cac:PartyLegalEntity>
        <cbc:RegistrationName>Customer Company Name</cbc:RegistrationName>
      </cac:PartyLegalEntity>
      <cac:Contact>
        <cbc:Name>Customer Company Name</cbc:Name>
        <cbc:Telephone>+1234567890</cbc:Telephone>
        <cbc:ElectronicMail>customer@example.com</cbc:ElectronicMail>
      </cac:Contact>
    </cac:Party>
    */
    private static PartyType getPartyType(InvoiceJsonVO.Person person) {
        PartyType partyType = new PartyType();
        PartyNameType supplierPartyName = new PartyNameType();
        supplierPartyName.setName((new NameType(person.getName())));
        partyType.addPartyName(supplierPartyName);

        // endpoint
        String personId = person.getId();
        SchemeID schemeID = EnumUtil.getEnumByValue(person.getSchemeId(), SchemeID.class);
        // endpoint
        EndpointIDType endpointIDType = new EndpointIDType();
        endpointIDType.setSchemeID("0151");
        endpointIDType.setValue(personId);
        partyType.setEndpointID(endpointIDType);
        // id
        PartyIdentificationType partyIdentificationType = new PartyIdentificationType();
        IDType idType = new IDType();
        idType.setSchemeID(schemeID.getValue());
        idType.setValue(personId);
        partyIdentificationType.setID(idType);
        partyType.addPartyIdentification(partyIdentificationType);
        // set contact details
        ContactType contactType = new ContactType();
        contactType.setName(person.getName());
        contactType.setTelephone(person.getPhone());
        contactType.setElectronicMail(person.getMail());
        partyType.setContact(contactType);
        // Address
        AddressType addressType = getAddressType(person.getAddress());
        partyType.setPostalAddress(addressType);
        // party legal entity
        PartyLegalEntityType entityType = new PartyLegalEntityType();
        RegistrationNameType registrationNameType = new RegistrationNameType();
        registrationNameType.setValue(person.getName());
        entityType.setRegistrationName(registrationNameType);
        // company ID
        CompanyIDType companyIDType = new CompanyIDType();
        companyIDType.setSchemeID("0151");
        companyIDType.setValue(personId);
        entityType.setCompanyID(companyIDType);
        partyType.addPartyLegalEntity(entityType);

        return partyType;
    }

    /**
    <cac:PostalAddress>
        <cbc:StreetName>Main street 1</cbc:StreetName>
        <cbc:AdditionalStreetName>Postbox 123</cbc:AdditionalStreetName>
        <cbc:CityName>Harrison</cbc:CityName>
        <cbc:PostalZone>2912</cbc:PostalZone>
        <cac:Country>
            <cbc:IdentificationCode>AU</cbc:IdentificationCode>
        </cac:Country>
    </cac:PostalAddress>
    */
    private static AddressType getAddressType(InvoiceJsonVO.Address address) {
        if (ObjectUtils.isEmpty(address)) {
            return null;
        }
        AddressType addressType = new AddressType();
        // address line
        AddressLineType addressLineType = new AddressLineType();
        addressLineType.setLine(address.getAddress());
        addressType.addAddressLine(addressLineType);
        // set country
        CountryType countryType = new CountryType();
        countryType.setIdentificationCode(address.getCountryCode());
        addressType.setCountry(countryType);
        // set street
        addressType.setStreetName(address.getStreet());
        addressType.setPostalZone(address.getPostalCode());
        addressType.setCityName(address.getCity());
        return addressType;
    }

    /**
    <cac:LegalMonetaryTotal>
        <cbc:LineExtensionAmount currencyID="AUD">1487.40</cbc:LineExtensionAmount>
        <cbc:TaxExclusiveAmount currencyID="AUD">1487.40</cbc:TaxExclusiveAmount>
        <cbc:TaxInclusiveAmount currencyID="AUD">1636.14</cbc:TaxInclusiveAmount>
        <cbc:ChargeTotalAmount currencyID="AUD">0.00</cbc:ChargeTotalAmount>
        <cbc:PrepaidAmount currencyID="AUD">0.00</cbc:PrepaidAmount>
        <cbc:PayableAmount currencyID="AUD">1636.14</cbc:PayableAmount>
    </cac:LegalMonetaryTotal>
    */
    private static void setLegalMonetaryTotal(InvoiceType invoice, InvoiceJsonVO invoiceJsonVO) {
        String currencyCode = invoiceJsonVO.getCurrencyCode();

        MonetaryTotalType legalMonetaryTotal = new MonetaryTotalType();
        // line extension amount type
        LineExtensionAmountType lineExtensionAmountType = new LineExtensionAmountType(BigDecimal.valueOf(Double.parseDouble(invoiceJsonVO.getSubTotal())));
        lineExtensionAmountType.setCurrencyID(currencyCode);
        legalMonetaryTotal.setLineExtensionAmount(lineExtensionAmountType);
        // tax inclusive amount type
        TaxInclusiveAmountType taxInclusiveAmountType = new TaxInclusiveAmountType();
        BigDecimal taxInclusiveAmount = BigDecimal.valueOf(Double.parseDouble(invoiceJsonVO.getInvoiceTotal()));
        taxInclusiveAmountType.setValue(taxInclusiveAmount);
        taxInclusiveAmountType.setCurrencyID(currencyCode);
        legalMonetaryTotal.setTaxInclusiveAmount(taxInclusiveAmountType);

        Double taxTotal = Double.parseDouble(invoiceJsonVO.getTaxTotal());
        // tax exclusive amount type
        TaxExclusiveAmountType taxExclusiveAmountType = new TaxExclusiveAmountType(taxInclusiveAmount.subtract(BigDecimal.valueOf(taxTotal)));
        taxExclusiveAmountType.setCurrencyID(currencyCode);
        legalMonetaryTotal.setTaxExclusiveAmount(taxExclusiveAmountType);
        // allowance amount
        Double allowanceAmount = Double.parseDouble(invoiceJsonVO.getAllowance().getAmount());
        ChargeTotalAmountType chargeTotalAmountType = new ChargeTotalAmountType(BigDecimal.valueOf(allowanceAmount));
        chargeTotalAmountType.setCurrencyID(currencyCode);
        legalMonetaryTotal.setChargeTotalAmount(chargeTotalAmountType);

        PayableAmountType payableAmountType = new PayableAmountType(taxInclusiveAmount);
        payableAmountType.setCurrencyID(currencyCode);
        legalMonetaryTotal.setPayableAmount(payableAmountType);

        invoice.setLegalMonetaryTotal(legalMonetaryTotal);
    }

    /**
    <cac:TaxTotal>
        <cbc:TaxAmount currencyID="AUD">148.74</cbc:TaxAmount>
        <cac:TaxSubtotal>
            <cbc:TaxableAmount currencyID="AUD">1487.40</cbc:TaxableAmount>
            <cbc:TaxAmount currencyID="AUD">148.74</cbc:TaxAmount>
            <cac:TaxCategory>
                <...></...>
            </cac:TaxCategory>
        </cac:TaxSubtotal>
    </cac:TaxTotal>
    */
    private static void setTaxTotal(InvoiceType invoice, InvoiceJsonVO invoiceJsonVO) {
        String currencyCode = invoiceJsonVO.getCurrencyCode();
        TaxTotalType taxTotalType = new TaxTotalType();
        TaxAmountType taxAmount = new TaxAmountType();
        BigDecimal taxTotal = BigDecimal.valueOf(Double.parseDouble(invoiceJsonVO.getTaxTotal()));
        taxAmount.setValue(taxTotal);
        taxAmount.setCurrencyID(currencyCode);
        taxTotalType.setTaxAmount(taxAmount);

        TaxSubtotalType taxSubtotalType = new TaxSubtotalType();
        TaxableAmountType taxableAmountType = new TaxableAmountType(BigDecimal.valueOf(Double.parseDouble(invoiceJsonVO.getSubTotal())));
        taxableAmountType.setCurrencyID(currencyCode);
        taxSubtotalType.setTaxableAmount(taxableAmountType);
        taxSubtotalType.setTaxAmount(taxAmount);

        TaxType taxType = TaxType.STANDARD;
        if (taxTotal.equals(BigDecimal.ZERO)) {
            taxType = TaxType.ZERO;
        }
        taxSubtotalType.setTaxCategory(getTaxCategoryType(taxType));
        taxTotalType.addTaxSubtotal(taxSubtotalType);

        invoice.addTaxTotal(taxTotalType);
    }
    /**
     <cac:Delivery>
        <cbc:ActualDeliveryDate>2019-07-01</cbc:ActualDeliveryDate>
        <cac:DeliveryLocation>
            <cbc:ID schemeID="0151">91888222000</cbc:ID>
            <cac:Address>
                <...></>
            </cac:Address>
        </cac:DeliveryLocation>
        <cac:DeliveryParty>
            <cac:PartyName>
                <...></>
            </cac:PartyName>
        </cac:DeliveryParty>
    </cac:Delivery>
    */
    private static void setDelivery(InvoiceType invoice, InvoiceJsonVO.Address deliveryAddress, InvoiceJsonVO.Person person) {
        DeliveryType deliveryType = new DeliveryType();
        deliveryType.setDeliveryAddress(getAddressType(deliveryAddress));
        deliveryType.setDeliveryParty(getPartyType(person));
        invoice.addDelivery(deliveryType);
    }

    /**
    <cac:PaymentMeans>
        <cbc:PaymentMeansCode name="Credit transfer">30</cbc:PaymentMeansCode>
        <cbc:PaymentID>PaymentReferenceText</cbc:PaymentID>
        <cac:PayeeFinancialAccount>
            <...></>
        </cac:PayeeFinancialAccount>
    </cac:PaymentMeans>
     */
    private static void setPaymentMeans(InvoiceType invoice, InvoiceJsonVO.Payment payment) {
        PaymentMeansType paymentMeansType = new PaymentMeansType();
        // set code
        String code = payment.getCode();
        PaymentMeansCodeType paymentMeansCodeType = new PaymentMeansCodeType();
        paymentMeansCodeType.setValue(code);
        paymentMeansCodeType.setName(EnumUtil.getEnumDescByValue(code, PaymentMeansCode.class));
        paymentMeansType.setPaymentMeansCode(paymentMeansCodeType);
        // set payment id
        PaymentIDType paymentIDType = new PaymentIDType();
        paymentIDType.setValue(payment.getAccountName());
        paymentMeansType.addPaymentID(paymentIDType);
        // payee financial account
        FinancialAccountType financialAccountType = getFinancialAccountType(payment);
        paymentMeansType.setPayeeFinancialAccount(financialAccountType);

        invoice.addPaymentMeans(paymentMeansType);
    }

    /**
    <cac:PayeeFinancialAccount>
        <cbc:ID>AccountNumber</cbc:ID>
        <cbc:Name>AccountName</cbc:Name>
        <cac:FinancialInstitutionBranch>
            <cbc:ID>BSB Number</cbc:ID>
        </cac:FinancialInstitutionBranch>
    </cac:PayeeFinancialAccount>
    */
    private static FinancialAccountType getFinancialAccountType(InvoiceJsonVO.Payment payment) {
        FinancialAccountType financialAccountType = new FinancialAccountType();
        financialAccountType.setID(payment.getAccountNumber());
        financialAccountType.setName(payment.getAccountName());
        PaymentNoteType paymentNoteType = new PaymentNoteType();
        paymentNoteType.setValue(payment.getPaymentNote());
        financialAccountType.addPaymentNote(paymentNoteType);
        BranchType branchType = new BranchType();
        branchType.setID(payment.getBsbNumber());
        financialAccountType.setFinancialInstitutionBranch(branchType);
        return financialAccountType;
    }

    /**
    <cac:AllowanceCharge>
        <cbc:ChargeIndicator>true</cbc:ChargeIndicator>
        <cbc:AllowanceChargeReasonCode>SAA</cbc:AllowanceChargeReasonCode>
        <cbc:AllowanceChargeReason>Shipping and Handling</cbc:AllowanceChargeReason>
        <cbc:Amount currencyID="AUD">0</cbc:Amount>
        <cbc:BaseAmount currencyID="AUD">0</cbc:BaseAmount>
        <cac:TaxCategory>
            <...></>
        </cac:TaxCategory>
    </cac:AllowanceCharge>
    */
    private static AllowanceChargeType getAllowanceCharge(InvoiceJsonVO.Allowance allowance) {
        // allowance charge type and reason
        AllowanceChargeType allowanceChargeType = new AllowanceChargeType();
        AllowanceChargeReasonCode allowanceChargeReasonCode = EnumUtil.getEnumByValue(allowance.getType(), AllowanceChargeReasonCode.class);
        allowanceChargeType.setChargeIndicator(allowanceChargeReasonCode.isCharge());
        allowanceChargeType.setAllowanceChargeReasonCode(allowanceChargeReasonCode.getValue());
        AllowanceChargeReasonType allowanceChargeReasonType = new AllowanceChargeReasonType();
        allowanceChargeReasonType.setValue(allowanceChargeReasonCode.getDesc());
        allowanceChargeType.addAllowanceChargeReason(allowanceChargeReasonType);
        // base amount type
        BaseAmountType baseAmountType = new BaseAmountType();
        baseAmountType.setValue(BigDecimal.valueOf(Double.parseDouble(allowance.getAmount())));
        baseAmountType.setCurrencyID(allowance.getCurrencyCode());
        allowanceChargeType.setBaseAmount(baseAmountType);
        AmountType amountType = new AmountType();
        amountType.setValue(BigDecimal.valueOf(Double.parseDouble(allowance.getAmount())));
        amountType.setCurrencyID(allowance.getCurrencyCode());
        allowanceChargeType.setAmount(amountType);
        // set tax category
        String taxPercent = allowance.getTaxPercent();
        allowanceChargeType.addTaxCategory(getTaxCategoryType(EnumUtil.getEnumByValue(taxPercent, TaxType.class)));

        allowanceChargeType.setMultiplierFactorNumeric(BigDecimal.valueOf(Double.parseDouble("0")));
        return allowanceChargeType;
    }
    /**
    <cac:TaxCategory>
        <cbc:ID>S</cbc:ID>
        <cbc:Percent>10</cbc:Percent>
        <cac:TaxScheme>
            <cbc:ID>GST</cbc:ID>
        </cac:TaxScheme>
    </cac:TaxCategory>
    */
    private static TaxCategoryType getTaxCategoryType(TaxType taxType) {
        TaxCategoryType taxCategoryType = new TaxCategoryType();
        taxCategoryType.setID(taxType.getDesc());
        taxCategoryType.setPercent(BigDecimal.valueOf(Double.parseDouble(taxType.getValue())));
        TaxSchemeType taxSchemeType = new TaxSchemeType();
        taxSchemeType.setID("GST");
        taxCategoryType.setTaxScheme(taxSchemeType);
        return taxCategoryType;
    }

    @AllArgsConstructor
    @Getter
    enum SchemeID implements BaseEnum{
        GLN("GLN", "Global Location Number"),
        DUNS("0060", "Data Universal Numbering System"),
        VATDE("VATDE", "German VAT Number"),
        ABN("ABN", "Australian Business Number"),
        EORI("EORI", "Economic Operators Registration and Identification"),
        LEI("LEI", "Legal Entity Identifier"),
        SWIFT("SWIFT", "SWIFT Code"),
        GSRN("GSRN", "Global Service Relation Number"),
        NACE("NACE", "European Standard Industry Classification Code"),
        NIF("NIF", "Spanish Tax Identification Number")
        ;
        final String value;
        final String desc;
    }

    @AllArgsConstructor
    @Getter
    enum TaxType implements BaseEnum{
        STANDARD("S","10", "Standard rate"),
        ZERO("Z", "0","Zero rate"),
        EXEMPT("E", "0","Exempt rate"),
        REDUCE("R", "0","Reduce rate");
        ;
        final String desc;
        final String value;
        final String others;
    }

    @AllArgsConstructor
    @Getter
    enum PaymentMeansCode implements BaseEnum {
        CASH("10", "Cash"),
        CHEQUE("20", "Cheque"),
        CREDIT_TRANSFER("30", "Credit transfer"),
        CREDIT_CARD("42", "Credit card"),
        DEBIT_CARD("48", "Debit card"),
        ;
        final String value;
        final String desc;
    }

    @AllArgsConstructor
    @Getter
    enum AllowanceChargeReasonCode implements BaseEnum{
        SAA("SAA", "Shipping and Handling", true),
        AA("AA", "Advertising Allowance", false),
        AB("AB", "Special Allowance", false),
        AC("AC", "Discount", false),
        AD("AD", "Bonus", false),
        AE("AE", "Coupon", false),
        AF("AF", "Freight", false),
        AG("AG", "Insurance", true),
        AH("AH", "Other", true),
        AI("AI", "Packaging", true),
        AJ("AJ", "Quantity Discount", false),
        AK("AK", "Rebate", false),
        AL("AL", "Returns", false),
        AM("AM", "Trade Discount", false),
        AN("AN", "Volume Discount", false)
        ;
        final String value;
        final String desc;
        final boolean charge;
    }

}
