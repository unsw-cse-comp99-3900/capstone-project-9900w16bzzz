import React, {useState} from "react";
import video from "../images/video1.mp4";
import styled from "styled-components";
import SignupButton from "./SignupButton";
import { Link, useNavigate } from "react-router-dom";



function Login (){
    const [loginEmail, setEmail] = useState('');
    const [loginPassword, setPassword] = useState('');
    const [emailError, setEmailError] = useState('');

    const navigate = useNavigate();

    const handleLogin = async () => {
      setEmailError('');
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailPattern.test(loginEmail)) {
        setEmailError('Please enter a valid email address.');
        return;
      }

        console.log("sign in email", loginEmail);
        console.log("sign in password", loginPassword);
      const requestBody = {
        loginName: loginEmail,
        loginPwd: loginPassword,
      };

      try {
        const response = await fetch(`http://localhost:9900/login`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestBody),
        });

        if (response.ok) {
          const data = await response.json();
          console.log('Login successful:', data);
          if(data.ok) {
            localStorage.setItem("token",data.data.token);
            await getUserInformation();
            navigate('/');
          }
        } else {
          console.error('Login failed:', response.statusText);
        }
      } catch (error) {
        console.error('Error:', error);
      }


    };

    const getUserInformation = async () => {
        const token = localStorage.getItem("token");
        try {
            const response = await fetch(`http://localhost:9900/getLoginName`, {
              method: 'GET',
              headers: {
                'Content-Type': 'application/json',
                'x-access-token': `${token}`
              },
            });
    
            if (response.ok) {
              const data = await response.json();
              if(data.ok){
                localStorage.setItem("username", data.data.userName);
                localStorage.setItem("userId",data.data.userId);
                window.dispatchEvent(new Event('localStorageChange'));
              }
              console.log('Get user info successful:', data);
            } else {
              console.error('Login failed:', response.statusText);
            }
          } catch (error) {
            console.error('Error:', error);
          }
    }

    return(
        <div id = "main">
            <video autoPlay muted loop id="background-video-signup">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
            </video>
            <Maincontainer>
                <LoginText>Log in</LoginText>
                <InputContainer>    
                    <StyledInput  type = "text" placeholder="Email" value={loginEmail} onChange={(e) => setEmail(e.target.value)}/>
                    {emailError && <ErrorText>{emailError}</ErrorText>}
                    <StyledInput  type = "password" placeholder="Password" value={loginPassword} onChange={(e) => setPassword(e.target.value)}/>
                </InputContainer>
                <ButtonContainer onClick={handleLogin}>
                    <SignupButton content="Log in"/>
                </ButtonContainer>
                <StyledLink>Forget Password?</StyledLink>
            </Maincontainer>
        </div>
    )
}

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
    &::placeholder{
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
    margin-top: 2rem;
    color: #ffffff;
    font-size: 0.7rem;
    letter-spacing: 0rem;
    text-decoration: none;
    &:hover {
        text-decoration: underline;
    }
`;
export default Login;