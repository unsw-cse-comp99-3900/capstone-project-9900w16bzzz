import React from "react";
import styled from "styled-components";

const CheckboxInput = ({ options, selectedRules, onChange }) => {
  return (
    <CheckboxWrapper>
      {options.map((option) => (
        <CheckboxLabel key={option.value}>
          <CheckboxStyled
            type="checkbox"
            checked={selectedRules.includes(option.value)}
            onChange={() => onChange(option.value)}
          />
          <CustomCheckbox checked={selectedRules.includes(option.value)} />
          <LabelText>{option.label}</LabelText>
        </CheckboxLabel>
      ))}
    </CheckboxWrapper>
  );
};

const CheckboxWrapper = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 15px;
`;

const CheckboxLabel = styled.label`
  display: flex;
  align-items: center;
  color: white;
  font-size: 14px;
  cursor: pointer;
  user-select: none;
`;

const CheckboxStyled = styled.input`
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
`;

const CustomCheckbox = styled.span`
  position: relative;
  display: inline-block;
  width: 18px;
  height: 18px;
  background-color: ${(props) => (props.checked ? "#6414FF" : "transparent")};
  border: 2px solid #6414ff;
  border-radius: 4px;
  margin-right: 10px;
  transition: all 0.2s ease-in-out;

  &:after {
    content: "";
    position: absolute;
    display: ${(props) => (props.checked ? "block" : "none")};
    left: 4px;
    top: 0px;
    width: 5px;
    height: 10px;
    border: solid white;
    border-width: 0 2px 2px 0;
    transform: rotate(45deg);
  }
`;

const LabelText = styled.div`
  font-color: white;
  margin-left: 5px;
`;

export default CheckboxInput;
