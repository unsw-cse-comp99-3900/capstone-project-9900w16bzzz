import React, { useState } from "react";
import video from "../../images/video1.mp4";
import styled from "styled-components";
import SignupButton from "./SignupButton";
import { Link, useNavigate } from "react-router-dom";
import { usePopup } from "../PopupWindow/PopupContext";
import CryptoJS from "crypto-js";

/**
 * Login component for user authentication.
 *
 * This component provides a login form for users to enter their email and password,
 * validates the input, and sends a login request to the server. On successful login,
 * it fetches user information and navigates to the home page.
 */
function Login() {
  const [loginEmail, setEmail] = useState("");
  const [loginPassword, setPassword] = useState("");
  const [emailError, setEmailError] = useState("");
  const [passwordError, setPasswordError] = useState("");

  const { showPopup } = usePopup();
  const navigate = useNavigate();

  /**
   * Handles the login process.
   *
   * This function validates the email and password, sends a login request to the server,
   * and handles the response. On successful login, it stores the authentication token,
   * fetches user information, and navigates to the home page.
   */
  const handleLogin = async () => {
    setEmailError("");
    setPasswordError("");
    
    // Email validation pattern
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailPattern.test(loginEmail)) {
      setEmailError("Please enter a valid email address.");
      return;
    }

    if (!loginPassword) {
      setPasswordError("Please enter a password.");
      return;
    }

    // Hash the password using MD5
    const hashedPassword = CryptoJS.MD5(loginPassword).toString();

    const requestBody = {
      loginName: loginEmail,
      loginPwd: hashedPassword,
    };

    try {
      const response = await fetch(
        `${process.env.REACT_APP_SERVER_URL}/login`,
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
          // Store the authentication token in local storage
          localStorage.setItem("token", data.data.token);
          showPopup("Login successful!", "success");
          await getUserInformation();
          navigate("/");
        } else {
          showPopup(`Error: ${data.msg}`, "error");
        }
      } else {
        console.error("Login failed:", response.statusText);
      }
    } catch (error) {
      console.error("Error:", error);
    }
  };

  /**
   * Fetches user information after login.
   *
   * This function sends a request to the server to get the logged-in user's information,
   * stores it in local storage, and dispatches an event to update the application state.
   */
  const getUserInformation = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(
        `${process.env.REACT_APP_SERVER_URL}/getLoginName`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
            "x-access-token": `${token}`,
          },
        }
      );

      if (response.ok) {
        const data = await response.json();
        if (data.ok) {
          localStorage.setItem("username", data.data.userName);
          localStorage.setItem("userId", data.data.userId);
          window.dispatchEvent(new Event("localStorageChange"));
        }
      } else {
        console.error("Login failed:", response.statusText);
      }
    } catch (error) {
      console.error("Error:", error);
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
          <LoginText>Log in</LoginText>
          <InputContainer>
            <StyledInput
              type="text"
              placeholder="Email"
              value={loginEmail}
              onChange={(e) => setEmail(e.target.value)}
            />
            {emailError && <ErrorText>{emailError}</ErrorText>}
            <StyledInput
              type="password"
              placeholder="Password"
              value={loginPassword}
              onChange={(e) => setPassword(e.target.value)}
            />
            {passwordError && <ErrorText>{passwordError}</ErrorText>}
          </InputContainer>
          <ButtonContainer>
            <SignupButton content="Log in" onClick={handleLogin} />
          </ButtonContainer>
          <StyledLink to="/sign-up">Don't have an account? Sign up</StyledLink>
        </Maincontainer>
      </PageContainer>
    </div>
  );
}

// Styled components for the Login component

const PageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
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

const Maincontainer = styled.div`
  display: flex;
  position: relative;
  justify-content: flex-start;
  align-items: center;
  flex-direction: column;
  height: 350px;
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

const LoginText = styled.div`
  margin: 2rem 0 1rem 0;
  font-size: 1.4rem;
  font-weight: bold;
`;

const InputContainer = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: center;
  height: 35%;
  width: 100%;
  margin: 10px 0px;
`;

const ButtonContainer = styled.div`
  margin-top: 2rem;
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
  margin-top: 1rem;
  color: #ffffff;
  font-size: 0.6rem;
  letter-spacing: 0rem;
  text-decoration: none;
  &:hover {
    text-decoration: underline;
  }
`;

export default Login;
