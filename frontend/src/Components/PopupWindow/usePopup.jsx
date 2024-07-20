import React, { useEffect, useState } from 'react';
import styled, { keyframes, css } from 'styled-components';
import { usePopup } from './PopupContext';

const slideIn = keyframes`
  from { transform: translateX(100%); }
  to { transform: translateX(0); }
`;

const slideOut = keyframes`
  from { transform: translateX(0); }
  to { transform: translateX(100%); }
`;

const PopupWrapper = styled.div`
  position: fixed;
  top: 9vh;
  right: 5vh;
  z-index: 1000;
  ${props => props.isVisible && !props.isClosing && css`
    animation: ${slideIn} 0.3s ease-in-out;
  `}
  ${props => props.isClosing && css`
    animation: ${slideOut} 0.3s ease-in-out forwards;
  `}
  ${props => !props.isVisible && !props.isClosing && css`
    display: none;
  `}
`;

const PopupContent = styled.div`
  background-color: ${props => props.type === 'error' ? '#FF4136' : '#2ECC40'};
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

const MessageText = styled.p`
  font-size: 16px;
  margin: 0;
`;

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

const GlobalPopup = () => {
  const { popupState, hidePopup } = usePopup();
  const [isClosing, setIsClosing] = useState(false);

  useEffect(() => {
    let timer;
    if (popupState.isVisible && !isClosing) {
      timer = setTimeout(() => {
        setIsClosing(true);
      }, 3000); 
    }
    return () => clearTimeout(timer);
  }, [popupState.isVisible, isClosing]);

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