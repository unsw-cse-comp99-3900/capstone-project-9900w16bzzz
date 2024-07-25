import React from "react";
import styled from "styled-components";

const SelectInput = ({ placeholder, value, onChange, options, onRelatedChange, relatedField, typeReasonMapping }) => {
    const handleChange = (e) => {
        const newValue = e.target.value;
        onChange(newValue);
        if (onRelatedChange && relatedField && typeReasonMapping) {
            if (relatedField === 'reason') {
                onRelatedChange(relatedField, typeReasonMapping[newValue]);
            } else if (relatedField === 'type') {
                const relatedValue = Object.keys(typeReasonMapping).find(key => typeReasonMapping[key] === newValue) || '';
                onRelatedChange(relatedField, relatedValue);
            }
        }
    };
    const placeholderLabels = {
        invoiceId: "Invoice Number",
        invoiceDate: "Invoice Date",
        dueDate: "Due Date",
        currencyCode: "Currency",
        amount: "Amount",
        description: "Description",
        quantity: "Quantity",
        unitPrice: "Unit Price",
        tax: "Tax Amount",
        taxRate: "Tax Rate (%)",
        id: "ABN",
        name: "Name",
        countryCode: "Country Code (e.g AU)",
        subTotal: "Sub Total",
        invoiceTotal: "Invoice Total",
        taxTotal: "Tax Total",
        taxPercent: "Tax Percent",
        code: "Method",
        accountName: "Account Name",
        accountNumber: "Account Number",
        bsbNumber: "Business Number",
        paymentNote: "Payment Note",
        postalCode: "Postal Code",
        address: "Address",
        street: "Street",
        city: "City",
        phone: "Phone",
        mail: "Mail",
        type: "Type"
    };
    const label = placeholderLabels[placeholder] || placeholder;

    return (
        <InputWrapper>
            <div>
                <StyledSelect
                    value={value}
                    onChange={handleChange}
                    id={placeholder}
                >
                    <option value="" disabled hidden></option>
                    {options.map(option => (
                        <option key={option.value} value={option.value}>
                            {option.label}
                        </option>
                    ))}
                </StyledSelect>
                <SelectArrow />
            </div>
            <StyledLabel htmlFor={placeholder} className={value && 'filled'}>
                {label}
            </StyledLabel>
        </InputWrapper>
    );
};

const InputWrapper = styled.div`
    position: relative;
    width: 100%;
    margin: 10px 0;
`;

const StyledSelect = styled.select`
    background: rgba(255, 255, 255, 0.15);
    border-radius: 2rem;
    width: 100%;
    height: 3rem;
    padding: 0 1rem;
    padding-right: 3rem;
    border: 2px solid transparent;
    color: white;
    font-size: 1rem;
    font-weight: bold;
    transition: all 0.3s ease;
    appearance: none;
    
    &:focus {
        outline: none;
        border-color: #b9abe0;
        box-shadow: 0 0 0 2px rgba(185, 171, 224, 0.3);
    }

    &:focus + label, &:not(:placeholder-shown) + label {
        transform: translateY(-2.5rem) translateX(-1.2rem) scale(0.75);
        color: #ffffff;
        background-color: rgba(0, 0, 0, 0);
        padding: 0 0.5rem;
    }

    option {
        background-color: #2c2c2c;
        color: white;
        padding: 10px;
    }
`;
const StyledLabel = styled.label`
    position: absolute;
    left: 1rem;
    top: 0.8rem;
    color: #ffffff;
    font-weight: 100;
    font-size: 1rem;
    pointer-events: none;
    transition: 0.3s ease all;
    z-index: 1;

    &.filled {
        transform: translateY(-2.5rem) translateX(-1.2rem)  scale(0.8);
        color: #ffffff;
        background-color: rgba(0, 0, 0, 0);
        padding: 0 0.5rem;
    }
`;

const SelectArrow = styled.div`
    position: absolute;
    right: 1rem;
    top: 50%;
    transform: translateY(-50%);
    width: 0;
    height: 0;
    border-left: 5px solid transparent;
    border-right: 5px solid transparent;
    border-top: 5px solid #ffffff;
    pointer-events: none;
`;

export default SelectInput;