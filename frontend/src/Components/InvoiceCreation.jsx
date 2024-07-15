import InvoiceForm from "./InvoiceCreationComponents/InvoiceForm";
import React, { useState } from 'react';
import { motion } from 'framer-motion';
import styled from "styled-components";
import video from "../images/video1.mp4";
import GetStarted from "./InvoiceCreationComponents/GetStarted";
import UploadPage from "./InvoiceCreationComponents/UploadPage";
import ChooseConvertOption from "./InvoiceCreationComponents/ChooseConvertOption";
import SucceedPage from "./InvoiceCreationComponents/SuccessedPage";

const InvoiceCreation = () => {
  const [step, setStep] = useState(0);
  const [file, setFile] = useState(null);
  const [invoice, setInvoice] = useState(null);

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
            <ChooseConvertOption goToStep={goToStep} setFile={setFile} file={file} setInvoice={setInvoice} invoice={invoice} />
          </motion.div>
        )}
        {step === 3 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <InvoiceForm  goToStep={goToStep} invoice={invoice} />
          </motion.div>
        )}
        {step === 4 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <SucceedPage goToStep={goToStep} />
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
