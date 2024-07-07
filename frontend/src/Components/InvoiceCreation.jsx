import InvoiceForm from "./InvoiceCreationComponents/InvoiceForm";
import React, { useState } from 'react';
import { color, motion } from 'framer-motion';
import styled from "styled-components";
import video from "../images/video1.mp4";
import { Link } from "react-router-dom";
import GetStarted from "./InvoiceCreationComponents/GetStarted";
import UploadPage from "./InvoiceCreationComponents/UploadPage";
import ChooseConvertOption from "./InvoiceCreationComponents/ChooseConvertOption";

const InvoiceCreation = () => {
  const [step, setStep] = useState(0);
  const [file, setFile] = useState(null);
  console.log('setFile type:', typeof setFile);

  const goToStep = (stepNumber) => setStep(stepNumber);
  const transition = {
      duration: 2,
  };

  return (
    <div>
      <MainContainer id="main" style={{height: "auto"}}>
          <BackgroundVideo autoPlay muted loop id="background-video">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
        </BackgroundVideo>
        {step === 0 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <GetStarted goToStep={goToStep} />
          </motion.div>
        )}
        {step === 1 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <UploadPage goToStep={goToStep} setFile={setFile} file={file} />
          </motion.div>
        )}
        {step === 2 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <ChooseConvertOption goToStep={goToStep} file={file} />
          </motion.div>
        )}
        {step === 3 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <InvoiceForm  goToStep={goToStep}/>
          </motion.div>
        )}
        {step === 4 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
              <h1 style={{ color: "white" }}>Invoice created! You can check your invoice in the{" "}
                <Link to="/my-invoice" style={{ color: 'white', textDecoration: 'underline' }}>
                  MY INVOICE
                </Link>{" "}
                page.
              </h1>
            <StyledButton onClick={()=> goToStep(0)}>Create new one!</StyledButton>
          </motion.div>
        )}
      </MainContainer>
    </div>
  );
};



const MainContainer = styled.div`
    position: relative;
    width: 100%;
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    z-index: 1;
`;

const StyledButton = styled.button`
    background-color: #6414FF;
    text-transform: uppercase;
    letter-spacing: 0.1rem;
    margin-left: 6rem;
    width: 50%;
    height: 2.3rem;
    border: none;
    background-color: #6414FF;
    border-radius: 70px;
	color:#ffffff;
    font-weight: bold;
    cursor: pointer;
    &:hover{
        background-color: transparent;
        transition: all ease 0.5s;
        color: #ffffff;
        border: 2px solid #6414FF;
    }
`;

const BackgroundVideo = styled.video`
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    object-fit: cover;
    z-index: -1;
`;


export default InvoiceCreation;
