import React from "react";
import styled from "styled-components";

/**
 * LoginInput component for rendering an input field.
 *
 * This component renders a styled input field used in the login form.
 *
 * @param {string} type - The type of the input field (e.g., text, password).
 * @param {string} placeholder - The placeholder text for the input field.
 * @param {string} value - The current value of the input field.
 * @param {function} onChange - The function to call when the input value changes.
 */
export default function LoginInput({ type, placeholder, value, onChange }) {
  return (
    <StyledInput
      type={type}
      placeholder={placeholder}
      value={value}
      onChange={onChange}
    />
  );
}

// Styled component for the input field
const StyledInput = styled.input`
  background: rgba(255, 255, 255, 0.15);
  border-radius: 2rem;
  width: 80%;
  height: 3rem;
  margin-top: 1rem;
  padding: 1rem;
  border: none;
  outline: none;
  color: #3c354e;
  font-size: 1rem;
  font-weight: bold;
  &:focus {
    display: inline-block;
    box-shadow: 0 0 0 0.2rem #b9abe0;
    backdrop-filter: blur(12rem);
    border-radius: 2rem;
  }
  &::placeholder {
    color: #ffffff;
    font-weight: 100;
    font-size: 1rem;
  }
`;
