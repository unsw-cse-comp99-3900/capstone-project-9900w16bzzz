import React from 'react';
import styled from 'styled-components';

const Modal = ({ message, onClose }) => {
    return (
        <Backdrop onClick={onClose}>
            <ModalContent onClick={e => e.stopPropagation()}>
                <CloseIcon onClick={onClose}>&times;</CloseIcon>
                <Message>{message}</Message>
            </ModalContent>
        </Backdrop>
    );
};

export default Modal;

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

const Message = styled.p`
    margin: 0;
    font-size: 1.2rem;
    color: white;
`;

const CloseIcon = styled.span`
    position: absolute;
    top: 10px;
    right: 10px;
    font-size: 24px;
    color: white;
    cursor: pointer;
    &:hover {
        color: #6414FF;
    }
`;