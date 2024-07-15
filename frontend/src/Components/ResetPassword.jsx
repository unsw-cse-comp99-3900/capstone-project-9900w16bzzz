import React, { useState } from "react";
import styled from "styled-components";
import { useNavigate } from "react-router-dom";
import video from "../images/video1.mp4";
import SignupButton from "./SignupButton";

function ResetPassword() {
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const navigate = useNavigate();

  const validatePassword = (password) => {
    if (password.length < 6) {
      return 'Password must be at least 6 characters long.';
    }
    return '';
  };

  const handleResetPassword = async () => {
    setError('');
    setSuccessMessage('');

    const passwordError = validatePassword(newPassword);
    if (passwordError) {
      setError(passwordError);
      return;
    }

    if (newPassword !== confirmPassword) {
      setError('The passwords entered twice do not match.');
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      setError('You are not logged in.');
      return;
    }

    try {
      const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/user/updateUser`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'x-access-token': `${token}`
        },
        body: JSON.stringify({
          oldPassword,
          newPassword,
          userId: localStorage.getItem("userId")
        }),
      });

      if (response.ok) {
        const data = await response.json();
        if (data.ok) {
          setSuccessMessage('Password reset successful.');
          localStorage.removeItem('token');
          localStorage.removeItem('userId');
          window.dispatchEvent(new Event('localStorageChange'));
          setTimeout(() => {
            navigate('/log-in');
          }, 1000);
        } else {
          setError(data.msg || 'Failed to reset password. Please try again.');
        }
      } else {
        setError('Failed to reset password. Please try again.');
      }
    } catch (error) {
      console.error('Error:', error);
      setError('An error occurred. Please try again later.');
    }
  };

  return (
    <div id="main">
      <BackgroundVideo autoPlay muted loop id="background-video-signup">
        <source src={video} type="video/mp4" />
        Your browser does not support the video tag.
      </BackgroundVideo>
      <MainContainer>
        <ResetText>Reset Password</ResetText>
        <InputContainer>
          <StyledInput 
            type="password" 
            placeholder="Current Password" 
            value={oldPassword} 
            onChange={(e) => setOldPassword(e.target.value)} 
          />
          <StyledInput 
            type="password" 
            placeholder="New Password" 
            value={newPassword} 
            onChange={(e) => setNewPassword(e.target.value)} 
          />
          <StyledInput 
            type="password" 
            placeholder="Confirm New Password" 
            value={confirmPassword} 
            onChange={(e) => setConfirmPassword(e.target.value)} 
          />
          {error && <ErrorText>{error}</ErrorText>}
          {successMessage && <SuccessText>{successMessage}</SuccessText>}
        </InputContainer>
        <ButtonContainer onClick={handleResetPassword}>
          <SignupButton content="Reset" />
        </ButtonContainer>
      </MainContainer>
    </div>
  );
}

const BackgroundVideo = styled.video`
  position: fixed;
  right: 0;
  bottom: 0;
  min-width: 100%;
  min-height: 100%;
  width: auto;
  height: auto;
  z-index: -1;
  object-fit: cover;
`;

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

const MainContainer = styled.div`
  display: flex;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  justify-content: flex-start;
  align-items: center;
  flex-direction: column;
  height: 400px;
  width: 300px;
  background: rgba(255, 255, 255, 0.30);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(8.5px);
  border-radius: 10px;
  color: #ffffff;
  text-transform: uppercase;
  letter-spacing: 0.4rem;
  z-index: 1;
`;

const ResetText = styled.div`
  margin: 2rem 0 1rem 0;
  font-size: 1.2rem;
  font-weight: bold;
  letter-spacing: 0.2rem;
`;

const InputContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 50%;
  width: 100%;
  margin: 10px 0px;
`;

const ButtonContainer = styled.div`
  margin-top: 1.8rem; 
  width: 100%;
  height: 10%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const ErrorText = styled.div`
  color: red;
  margin-top: 0.5rem;
  font-size: 0.7rem;
  letter-spacing: 0rem;
  font-weight: bold;
  text-align: center;
`;

const SuccessText = styled.div`
  color: green;
  margin-top: 0.5rem;
  font-size: 0.7rem;
  letter-spacing: 0rem;
  font-weight: bold;
  text-align: center;
`;


export default ResetPassword;
