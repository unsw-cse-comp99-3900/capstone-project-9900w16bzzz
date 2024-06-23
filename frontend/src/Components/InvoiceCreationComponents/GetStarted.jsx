import React from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";

const  GetStarted = ({ goToStep }) => {
    return (
        <div className = "name">
            <h1><span>Get</span> Started</h1>
            <p className="details">Follow the several steps to create your e-invoice. </p>
            <ArrowButton onClick={() => goToStep(1)}>
                <ArrowIcon />
            </ArrowButton>
        </div>
    )
}

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
      fill: #6414FF;
    }
  }

`;

export default GetStarted;