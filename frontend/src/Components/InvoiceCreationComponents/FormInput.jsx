import React from "react";
import styled from "styled-components";

const FormInput = ({ type, placeholder, value, onChange }) => {
    return (
        <InputWrapper>
            <StyledInput 
                type={type} 
                placeholder={placeholder}
                value={value}
                onChange={onChange}
                id={placeholder}
            />
            <StyledLabel htmlFor={placeholder} className={value && 'filled'}>
                {placeholder}
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

    &:focus + label, &:not(:placeholder-shown) + label {
        transform: translateY(-2.5rem) translateX(-1.2rem) scale(0.75);
        color: #ffffff;
        background-color: rgba(0, 0, 0, 0);
        padding: 0 0.5rem;
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
        color: #b9abe0;
        background-color: rgba(0, 0, 0, 0.8);
        padding: 0 0.5rem;
    }
`;

export default FormInput;