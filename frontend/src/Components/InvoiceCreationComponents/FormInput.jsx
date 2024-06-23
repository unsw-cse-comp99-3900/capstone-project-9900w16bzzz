import React from "react";
import styled from "styled-components";

const FormInput = ({ type, placeholder }) => {
    return <StyledInput type={type} placeholder={placeholder} />;
};

const StyledInput = styled.input`
    background: rgba(255, 255, 255, 0.15);
    border-radius: 2rem;
    width: 80%;
    height: 3rem;
    padding: 1rem;
    border: none;
    margin: 10px;
    color: white;
    font-size: 1rem;
    font-weight: bold;
    &:focus {
        display: inline-block;
        box-shadow: 0 0 0 0.2rem #b9abe0;
        backdrop-filter: blur(12rem);
        border-radius: 2rem;
    }
    &::placeholder{
        color: #ffffff;
        font-weight: 100;
        font-size: 1rem;
    }
`;

export default FormInput;
