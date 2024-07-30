import React from "react";
import styled from "styled-components";

export default function SignupButton({ content, onClick }) {
  return <StyledButton onClick={onClick}>{content}</StyledButton>;
}

const StyledButton = styled.button`
  background-color: #6414ff;
  text-transform: uppercase;
  letter-spacing: 0.1rem;
  margin-left: 6rem;
  width: 50%;
  height: 2.3rem;
  border: none;
  background-color: #6414ff;
  border-radius: 70px;
  color: #ffffff;
  font-weight: bold;
  cursor: pointer;
  &:hover {
    background-color: transparent;
    transition: all ease 0.5s;
    color: #ffffff;
    border: 2px solid #6414ff;
  }
`;
