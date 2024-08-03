import React from "react";
import styled from "styled-components";

/**
 * Modal component for displaying alert messages.
 *
 * This component renders a modal with a message and a close button.
 * The modal can be closed by clicking the backdrop or the close button.
 *
 * @param {string} message - The alert message to be displayed.
 * @param {function} onClose - The function to call when the modal is closed.
 */
const Modal = ({ message, onClose }) => {
  return (
    <Backdrop onClick={onClose}>
      <ModalContent onClick={(e) => e.stopPropagation()}>
        <CloseIcon onClick={onClose}>&times;</CloseIcon>
        <Message>{message}</Message>
      </ModalContent>
    </Backdrop>
  );
};

export default Modal;

// Styled component for the backdrop
const Backdrop = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
`;

// Styled component for the modal content
const ModalContent = styled.div`
  background: black;
  padding: 30px 20px;
  border-radius: 8px;
  text-align: center;
  width: 300px;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
`;

// Styled component for the message
const Message = styled.p`
  margin: 0;
  font-size: 1.2rem;
  color: white;
`;

// Styled component for the close icon
const CloseIcon = styled.span`
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 24px;
  color: white;
  cursor: pointer;
  &:hover {
    color: #6414ff;
  }
`;

