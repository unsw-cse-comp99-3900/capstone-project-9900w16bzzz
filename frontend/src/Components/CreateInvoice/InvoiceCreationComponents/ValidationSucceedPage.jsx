import React from "react";
import { motion } from "framer-motion";
import { Link } from "react-router-dom";
import styled from "styled-components";

/**
 * ValidationSucceedPage component to display a success message after invoice validation.
 * @param {Function} goToStep - Function to navigate to a specific step.
 */
const ValidationSucceedPage = ({ goToStep }) => {
  // Transition settings for the framer-motion animations
  const transition = {
    duration: 2,
  };

  return (
    <motion.div
      initial={{ opacity: 0 }}
      animate={{ opacity: 1 }}
      exit={{ opacity: 0 }}
      transition={transition}
    >
      <MainContainer>
        <LargeWhiteMessage>Invoice validated!</LargeWhiteMessage>
        <Message>
          You can check your invoice and validate status in the{" "}
          <PurpleLink to="/my-invoice">MY INVOICE</PurpleLink> page.
        </Message>
        <Message>OR</Message>
        <StyledButton onClick={() => goToStep(6)}>
          Check Validation Report!
        </StyledButton>
      </MainContainer>
    </motion.div>
  );
};

// Styled components for the UI elements

const MainContainer = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh;
  text-align: center;
  color: white;
  padding: 30px;
`;

const LargeWhiteMessage = styled.h1`
  color: white;
  font-size: 2.8rem;
  margin-bottom: 1rem;
`;

const Message = styled.h1`
  color: white;
  margin-bottom: 2rem;
  letter-spacing: 0.07em;
`;

const PurpleLink = styled(Link)`
  color: #6414ff;
  text-decoration: underline;
  &:hover {
    color: white;
  }
`;

const StyledButton = styled.button`
  background-color: #6414ff;
  text-transform: uppercase;
  letter-spacing: 0.1rem;
  width: 50%;
  max-width: 300px;
  height: 2.8rem;
  border: none;
  background-color: #6414ff;
  border-radius: 60px;
  color: #ffffff;
  font-weight: bold;
  cursor: pointer;
  margin-top: 1rem;
  &:hover {
    background-color: transparent;
    transition: all ease 0.5s;
    color: #ffffff;
    border: 2px solid #6414ff;
  }
`;

export default ValidationSucceedPage;
