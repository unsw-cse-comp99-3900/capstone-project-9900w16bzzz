import React, { useState } from "react";
import styled from "styled-components";
import video from "../images/video1.mp4";
import SignupInput from "./SignupInput";
import SignupButton from "./SignupButton";
import { Link, useNavigate } from "react-router-dom";
import { usePopup } from "./PopupWindow/PopupContext";
import CryptoJS from "crypto-js";

/**
 * Signup component for user registration.
 *
 * This component provides a form for users to sign up with email, username, and password.
 * It handles input validation, password hashing, and sending the registration request to the server.
 */
function Signup() {
  const { showPopup } = usePopup();
  const [email, setEmail] = useState("");
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [emailError, setEmailError] = useState("");
  const [usernameError, setUsernameError] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const navigate = useNavigate();

  /**
   * Handles the sign-up process.
   *
   * This function validates the inputs, hashes the password, and sends a request to the server to register the user.
   * It also handles the response and shows appropriate messages to the user.
   */
  const handleSignUp = async () => {
    setEmailError("");
    setUsernameError("");
    setPasswordError("");

    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(email)) {
      setEmailError("Please enter a valid email address.");
      return;
    }

    if (!username) {
      setUsernameError("Please enter a username");
      return;
    }

    if (password.length < 6) {
      setPasswordError("Password must be at least 6 characters long.");
      return;
    }

    if (password !== confirmPassword) {
      setPasswordError("Passwords do not match.");
      return;
    }

    const hashedPassword = CryptoJS.MD5(password).toString();

    const requestBody = {
      loginName: email,
      userName: username,
      loginPwd: hashedPassword,
    };

    try {
      const response = await fetch(
        `${process.env.REACT_APP_SERVER_URL}/signup`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(requestBody),
        }
      );

      if (response.ok) {
        const data = await response.json();
        if (data.ok) {
          showPopup("Sign up successful!", "success");
          navigate("/");
        } else {
          showPopup(`Sign up failed: ${data.msg}`, "error");
          console.error("Sign up failed:", data.msg);
        }
      } else {
        setEmailError("Sign up failed. Please try again.");
      }
    } catch (error) {
      console.error("Error:", error);
      setPasswordError("An error occurred. Please try again.");
    }
  };

  return (
    <div>
      {/* Background video */}
      <video autoPlay muted loop id="background-video-signup">
        <source src={video} type="video/mp4" />
        Your browser does not support the video tag.
      </video>
      <PageContainer>
        <Maincontainer>
          <WelcomeText>Welcome</WelcomeText>
          <InputContainer>
            <SignupInput
              type="text"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
            />
            {emailError && <ErrorText>{emailError}</ErrorText>}
            <SignupInput
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
            {usernameError && <ErrorText>{usernameError}</ErrorText>}
            <SignupInput
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
            <SignupInput
              type="password"
              placeholder="Confirm password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
            />
            {passwordError && <ErrorText>{passwordError}</ErrorText>}
          </InputContainer>
          <ButtonContainer>
            <SignupButton content="Sign up" onClick={handleSignUp} />
          </ButtonContainer>
          <StyledLink to="/log-in">Already have an account? Login</StyledLink>
        </Maincontainer>
      </PageContainer>
    </div>
  );
}

// Styled components for the Signup component

const PageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
`;

const Maincontainer = styled.div`
  display: flex;
  position: relative;
  justify-content: flex-start;
  align-items: center;
  flex-direction: column;
  height: 420px;
  width: 300px;
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: invert(20%);
  border-radius: 10px;
  color: #ffffff;
  text-transform: uppercase;
  letter-spacing: 0.4rem;
  z-index: 1;
`;

const WelcomeText = styled.div`
  margin: 2rem 0 1rem 0;
  font-size: 1.4rem;
  font-weight: bold;
`;

const InputContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  height: 60%;
  width: 100%;
`;

const ButtonContainer = styled.div`
  margin-top: 0.5rem;
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
`;

const StyledLink = styled(Link)`
  margin-top: 0.7rem;
  color: #ffffff;
  font-size: 0.6rem;
  letter-spacing: 0rem;
  text-decoration: none;
  &:hover {
    text-decoration: underline;
  }
`;

export default Signup;
