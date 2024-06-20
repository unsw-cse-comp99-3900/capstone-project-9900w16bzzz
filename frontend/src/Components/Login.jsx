import React from "react";
import video from "../images/video1.mp4";
import styled from "styled-components";
import LoginInput from "./LoginInput";
import SignupButton from "./SignupButton";
import { Link } from "react-router-dom";



function Login (){
    return(
        <div id = "main">
            <video autoPlay muted loop id="background-video-signup">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
            </video>
            <Maincontainer>
                <LoginText>Log in</LoginText>
                <InputContainer>    
                    <LoginInput type = "text" placeholder="Email"/>
                    <LoginInput type = "password" placeholder="Password"/>
                </InputContainer>
                <ButtonContainer>
                    <SignupButton content="Log in"/>
                </ButtonContainer>
                <StyledLink>Forget Password?</StyledLink>
            </Maincontainer>
        </div>
    )
}
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
`;
const ButtonContainer = styled.div`
    margin-top: 2rem;
    width: 100%;
    height: 10%;
    display: flex;
    align-items: center;
    justify-content: center;
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