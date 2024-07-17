import React from "react";
import styled from "styled-components";

const CheckboxInput = ({ options, selectedRules, onChange }) => {
    return (
        <CheckboxWrapper>
            {options.map(option => (
                <CheckboxLabel key={option.value}>
                    <CheckboxStyled
                        type="checkbox"
                        checked={selectedRules.includes(option.value)}
                        onChange={() => onChange(option.value)}
                    />
                    {option.label}
                </CheckboxLabel>
            ))}
        </CheckboxWrapper>
    );
};

const CheckboxWrapper = styled.div`
    display: flex;
    flex-direction: column;
    gap: 10px;
`;

const CheckboxLabel = styled.label`
    display: flex;
    align-items: center;
    color: white;
    font-size: 14px;
    cursor: pointer;
`;

const CheckboxStyled = styled.input`
    margin-right: 10px;
    cursor: pointer;
`;

export default CheckboxInput;