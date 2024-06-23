import React from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";
import FormInput from "./FormInput";

function InvoiceForm({ goToStep }) {
    return (
        <MainContainer className="name">
            <ArrowButton onClick={() => goToStep(2)}>
                <ArrowIcon style={{ transform: 'scaleX(-1)' }} />
            </ArrowButton>
            <Content>
                <h1><span>Step 3 </span> Review</h1>
                <p className="details" style={{margin:"10px"}}>Last step! Please review the invoice information.</p>
                <p className="details" style={{margin:"0"}}>If everything is correct, you can proceed to create the UBL e-invoice.</p>
                <InputContainer>
                    <FormInput type="text" placeholder="Invoice Number" />
                    <FormInput type="text" placeholder="Customer Name" />
                    <FormInput type="email" placeholder="Customer Email" />
                    <FormInput type="text" placeholder="Item Description" />
                    <FormInput type="number" placeholder="Item Quantity" />
                    <FormInput type="number" placeholder="Item Price" />
                </InputContainer>
                <a className="header-btn" style={{ letterSpacing: "0rem", margin: "15px"}}>Submit</a>
            </Content>
            <ArrowButton disabled style={{ opacity: 0, cursor: "not-allowed", "pointer-events": "none" }}>
                <ArrowIcon style={{ transform: 'scaleX(-1)' }} />
            </ArrowButton>
            
        </MainContainer>
    
    );
}


const InputContainer = styled.div`
  display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    height: 35%;
    width: 100%;
    margin: 10px 0px;
`;

const MainContainer = styled.div`
    width: 80%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 10px;
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    backdrop-filter: blur(2px);
    color: white;
    z-index: 1;
`;



const ArrowButton = styled.button`
  background-color: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  svg {
    width: auto;
    height: 50px;
    path {
      fill: ${props => (props.disabled ? 'grey' : 'white')};
    }
    &:hover path {
      fill: ${props => (props.disabled ? 'grey' : '#6414FF')};
    }
  }
`;

const Content = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  width: 90%;

  input {
    margin-top: 20px;
  }
`;

export default InvoiceForm;
