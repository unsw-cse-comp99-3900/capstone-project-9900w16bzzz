import React from "react";
import styled from "styled-components";

/**
 * CheckboxInput component to render a list of selected rules.
 * @param {Array} options - The list of options for the rules.
 * @param {Array} selectedRules - The list of currently selected rules.
 * @param {Function} onChange - Function to handle changes to the selected rules.
 */
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

/**
 * Wrapper for the list of checkboxes.
 */
const CheckboxWrapper = styled.div`
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-top: 15px;
`;

/**
 * Label for each checkbox to contain the input and custom styling.
 */
const CheckboxLabel = styled.label`
  display: flex;
  align-items: center;
  color: white;
  font-size: 14px;
  cursor: pointer;
  user-select: none;
`;

/**
 * Hidden native checkbox input to be styled by CustomCheckbox.
 */
const CheckboxStyled = styled.input`
  position: absolute;
  opacity: 0;
  cursor: pointer;
  height: 0;
  width: 0;
`;

/**
 * Custom styled checkbox to replace the native checkbox appearance.
 */
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

/**
 * Text label for the checkbox.
 */
const LabelText = styled.div`
  font-color: white;
  margin-left: 5px;
`;

export default CheckboxInput;
