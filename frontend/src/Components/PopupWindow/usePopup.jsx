import React, { useEffect, useState } from "react";
import styled, { keyframes, css } from "styled-components";
import { usePopup } from "./PopupContext";

// Keyframes for slide-in animation
const slideIn = keyframes`
  from { transform: translateX(100%); }
  to { transform: translateX(0); }
`;

// Keyframes for slide-out animation
const slideOut = keyframes`
  from { transform: translateX(0); }
  to { transform: translateX(100%); }
`;

// Styled component for the popup wrapper
const PopupWrapper = styled.div`
  position: fixed;
  top: 9vh;
  right: 5vh;
  z-index: 1000;
  ${(props) =>
    props.isVisible &&
    !props.isClosing &&
    css`
      animation: ${slideIn} 0.3s ease-in-out;
    `}
  ${(props) =>
    props.isClosing &&
    css`
      animation: ${slideOut} 0.3s ease-in-out forwards;
    `}
  ${(props) =>
    !props.isVisible &&
    !props.isClosing &&
    css`
      display: none;
    `}
`;

// Styled component for the popup content
const PopupContent = styled.div`
  background-color: ${(props) =>
    props.type === "error" ? "#FF4136" : "#2ECC40"};
  color: white;
  padding: 15px 20px;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
  display: flex;
  align-items: center;
  justify-content: space-between;
  min-width: 250px;
  max-width: 350px;
`;

// Styled component for the message text
const MessageText = styled.p`
  font-size: 16px;
  margin: 0;
`;

// Styled component for the close button
const CloseButton = styled.button`
  background-color: transparent;
  border: none;
  color: white;
  font-size: 20px;
  cursor: pointer;
  padding: 0;
  margin-left: 15px;
  opacity: 0.7;
  transition: opacity 0.2s ease;

  &:hover {
    opacity: 1;
  }
`;

/**
 * Global popup component to display popup messages.
 */
const GlobalPopup = () => {
  const { popupState, hidePopup } = usePopup();
  const [isClosing, setIsClosing] = useState(false);

  // Effect to start the closing animation after a delay when the popup is visible
  useEffect(() => {
    let timer;
    if (popupState.isVisible && !isClosing) {
      timer = setTimeout(() => {
        setIsClosing(true);
      }, 5000);
    }
    return () => clearTimeout(timer);
  }, [popupState.isVisible, isClosing]);

  // Effect to hide the popup after the closing animation completes
  useEffect(() => {
    let timer;
    if (isClosing) {
      timer = setTimeout(() => {
        hidePopup();
        setIsClosing(false);
      }, 300);
    }
    return () => clearTimeout(timer);
  }, [isClosing, hidePopup]);

  /**
   * Handler to manually trigger the closing animation.
   */
  const handleClose = () => {
    setIsClosing(true);
  };

  return (
    <PopupWrapper isVisible={popupState.isVisible} isClosing={isClosing}>
      <PopupContent type={popupState.type}>
        <MessageText>{popupState.message}</MessageText>
        <CloseButton onClick={handleClose}>&times;</CloseButton>
      </PopupContent>
    </PopupWrapper>
  );
};

export default GlobalPopup;