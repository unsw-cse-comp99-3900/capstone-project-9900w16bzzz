import React,{useState} from "react";
import styled from "styled-components";
import video from "../images/video1.mp4";
import SignupInput from "./SignupInput";
import SignupButton from "./SignupButton";
import { Link, useNavigate } from "react-router-dom";


function Signup(){

    const [email, setEmail] = useState('');
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleSignUp = async () => {
        console.log(email);
        console.log(username);
        console.log(password);
      const requestBody = {
        loginName: email,
        userName: username,
        loginPwd: password,
      };

      try {
        const response = await fetch(`http://localhost:9900/signup`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
          },
          body: JSON.stringify(requestBody),
        });

        if (response.ok) {
          const data = await response.json();
          console.log('Sign up successful:', data);
          navigate('/');
        } else {
          console.error('Sign up  failed:', response.statusText);
        }
      } catch (error) {
        console.error('Error:', error);
      }
    };

    return(
        <div id = "main">
            <video autoPlay muted loop id="background-video-signup">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
            </video>
            <Maincontainer>
                <WelcomeText>Welcome</WelcomeText>
                <InputContainer>
                    <SignupInput type = "text" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)}/>
                    <SignupInput type = "text" placeholder="Username" value={username} onChange={(e) => setUsername(e.target.value)}/>
                    <SignupInput type = "password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)}/>
                    <SignupInput type = "password" placeholder="Confirm password"/>
                </InputContainer>
                <ButtonContainer  onClick={handleSignUp}>
                    <SignupButton content="Sign up"/>
                </ButtonContainer>
                <StyledLink to="/log-in">Already have an account? Login</StyledLink>
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
    height: 410px;
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