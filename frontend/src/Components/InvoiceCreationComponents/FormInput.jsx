import React from "react";
import styled from "styled-components";

const FormInput = ({ type, placeholder, value, onChange, isInvalid }) => {
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
    type: "Type",
  };
  const label = placeholderLabels[placeholder] || placeholder;

  return (
    <InputWrapper>
      <StyledInput
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={(e) => onChange(e.target.value)}
        id={placeholder}
        isInvalid={isInvalid}
      />
      <StyledLabel htmlFor={placeholder} className={value && "filled"}>
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

const StyledInput = styled.input`
  &[type="date"]::-webkit-calendar-picker-indicator {
    filter: invert(1);
  }
  background: rgba(255, 255, 255, 0.15);
  border-radius: 2rem;
  width: 100%;
  height: 3rem;
  padding: 1rem;
  border: 2px solid transparent;
  color: white;
  font-size: 1rem;
  font-weight: bold;
  transition: all 0.3s ease;

  &:focus {
    outline: none;
    border-color: #b9abe0;
    box-shadow: 0 0 0 2px rgba(185, 171, 224, 0.3);
  }

  &::placeholder {
    color: transparent;
  }

  &:focus + label,
  &:not(:placeholder-shown) + label {
    transform: translateY(-2.5rem) translateX(-1.2rem) scale(0.75);
    color: #ffffff;
    background-color: rgba(0, 0, 0, 0);
    padding: 0 0.5rem;
  }

  ${(props) =>
    props.isInvalid &&
    `
        border-color: #ff0000;
        box-shadow: 0 0 0 2px rgba(255, 0, 0, 0.3);
      `}
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
    transform: translateY(-2.5rem) translateX(-1.2rem) scale(0.8);
    color: #b9abe0;
    background-color: rgba(0, 0, 0, 0.8);
    padding: 0 0.5rem;
  }
`;

export default FormInput;
