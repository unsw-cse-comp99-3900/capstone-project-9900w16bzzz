import React, { useState } from "react";
import video from "../images/video1.mp4";
import styled from "styled-components";
import SignupButton from "./SignupButton";
import { useNavigate } from "react-router-dom";
import { usePopup } from "./PopupWindow/PopupContext";
import CryptoJS from "crypto-js";

/**
 * ResetPassword component for handling password reset functionality.
 *
 * This component provides a form for users to reset their password.
 * It includes validation, password hashing, and interaction with the server.
 */
function ResetPassword() {
  const [oldPassword, setOldPassword] = useState("");
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const { showPopup } = usePopup();
  const navigate = useNavigate();

  /**
   * Validates the new password.
   *
   * @param {string} password - The new password.
   * @returns {string} An error message if validation fails, otherwise an empty string.
   */
  const validatePassword = (password) => {
    if (password.length < 6) {
      return "Password must be at least 6 characters long.";
    }
    return "";
  };

  /**
   * Handles the password reset process.
   *
   * This function validates the passwords, hashes them, and sends a request to the server to update the password.
   * It also handles the response and shows appropriate messages to the user.
   */
  const handleResetPassword = async () => {
    const passwordError = validatePassword(newPassword);
    if (passwordError) {
      showPopup(passwordError, "error");
      return;
    }

    if (newPassword !== confirmPassword) {
      showPopup("The passwords entered twice do not match.", "error");
      return;
    }

    const token = localStorage.getItem("token");
    if (!token) {
      showPopup("You are not logged in.", "error");
      return;
    }

    const hashedOldPassword = CryptoJS.MD5(oldPassword).toString();
    const hashedNewPassword = CryptoJS.MD5(newPassword).toString();

    try {
      const response = await fetch(
        `${process.env.REACT_APP_SERVER_URL}/user/updateUser`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "x-access-token": `${token}`,
          },
          body: JSON.stringify({
            oldPassword: hashedOldPassword,
            newPassword: hashedNewPassword,
            userId: localStorage.getItem("userId"),
          }),
        }
      );

      if (response.ok) {
        const data = await response.json();
        if (data.ok) {
          showPopup("Password reset successful.", "success");
          localStorage.removeItem("token");
          localStorage.removeItem("userId");
          window.dispatchEvent(new Event("localStorageChange"));
          setTimeout(() => {
            navigate("/log-in");
          }, 1000);
        } else {
          showPopup(
            data.msg || "Failed to reset password. Please try again.",
            "error"
          );
        }
      } else {
        showPopup("Failed to reset password. Please try again.", "error");
      }
    } catch (error) {
      console.error("Error:", error);
      showPopup("An error occurred. Please try again later.", "error");
    }
  };

  return (
    <div>
      {/* Background video */}
      <BackgroundVideo autoPlay muted loop id="background-video-signup">
        <source src={video} type="video/mp4" />
        Your browser does not support the video tag.
      </BackgroundVideo>
      <PageContainer>
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
          </InputContainer>
          <ButtonContainer>
            <SignupButton content="Reset" onClick={handleResetPassword} />
          </ButtonContainer>
        </MainContainer>
      </PageContainer>
    </div>
  );
}

// Styled components for the ResetPassword component

const PageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
`;

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
  background: rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(8.5px);
  border-radius: 10px;
  color: #ffffff;
  text-transform: uppercase;
  letter-spacing: 0.4rem;
  z-index: 0;
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

export default ResetPassword;
