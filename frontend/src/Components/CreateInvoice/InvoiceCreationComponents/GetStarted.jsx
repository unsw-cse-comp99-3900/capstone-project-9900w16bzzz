import React from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../../images/arrow.svg";

/**
 * GetStarted component to display the initial "Get Started" screen.
 * @param {Function} goToStep - Function to navigate to a specific step.
 */
const GetStarted = ({ goToStep }) => {
  return (
    <div className="name">
      {/* Main title */}
      <h1>
        <span>Get</span> Started
      </h1>
      {/* Description text */}
      <p className="details">
        Follow the several steps to create your e-invoice.
      </p>
      {/* Button to proceed to the next step */}
      <ArrowButton data-cy="arrow-button" onClick={() => goToStep(1)}>
        <ArrowIcon />
      </ArrowButton>
    </div>
  );
};

/**
 * Styled component for the arrow button.
 */
const ArrowButton = styled.button`
  background-color: transparent;
  cursor: pointer;
  border: none;
  padding: 0;

  svg {
    width: auto;
    height: 50px;

    path {
      fill: white;
    }

    &:hover path {
      fill: #6414ff;
    }
  }
`;

export default GetStarted;
