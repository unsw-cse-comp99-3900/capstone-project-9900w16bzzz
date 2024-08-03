import React, { useState } from "react";
import { motion } from "framer-motion";
import styled from "styled-components";
import video from "../images/video1.mp4";
import InvoiceForm from "./InvoiceCreationComponents/InvoiceForm";
import GetStarted from "./InvoiceCreationComponents/GetStarted";
import UploadPage from "./InvoiceCreationComponents/UploadPage";
import ChooseConvertOption from "./InvoiceCreationComponents/ChooseConvertOption";
import SucceedPage from "./InvoiceCreationComponents/SucceedPage";
import ValidationResultPage from "./InvoiceCreationComponents/ValidationResultPage";
import ValidationSucceedPage from "./InvoiceCreationComponents/ValidationSucceedPage";

/**
 * Main component for the invoice creation process.
 * Handles different steps of the process and transitions between them.
 */
const InvoiceCreation = () => {
  // State to manage the current step in the invoice creation process
  const [step, setStep] = useState(0);
  // State to manage the uploaded file
  const [file, setFile] = useState(null);
  // State to manage the created invoice
  const [invoice, setInvoice] = useState(null);
  // State to manage the validation result
  const [validationResult, setValidationResult] = useState(null);

  /**
   * Function to navigate to a specific step.
   * @param {number} stepNumber - The step number to navigate to.
   */
  const goToStep = (stepNumber) => setStep(stepNumber);

  // Transition settings for animations
  const transition = {
    duration: 2,
  };

  return (
    <div>
      <MainContainer id="main" style={{ height: "auto" }}>
        {/* Background video */}
        <BackgroundVideo autoPlay muted loop id="background-video">
          <source src={video} type="video/mp4" />
          Your browser does not support the video tag.
        </BackgroundVideo>
        {/* Conditional rendering for each step with animations */}
        {/* Step 1: get started */}
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
        {/* Step 2: upload file*/}
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
        {/* Step 3: choose upload option */}
        {step === 2 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <ChooseConvertOption
              goToStep={goToStep}
              setFile={setFile}
              file={file}
              setInvoice={setInvoice}
              invoice={invoice}
            />
          </motion.div>
        )}
        {/* Step 4: Invoice form */}
        {step === 3 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <InvoiceForm
              goToStep={goToStep}
              invoice={invoice}
              setValidationResult={setValidationResult}
            />
          </motion.div>
        )}
        {/* Step 5: Upload directly succeed page */}
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
        {/* Step 6: Validation succeesfully page */}
        {step === 5 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <ValidationSucceedPage goToStep={goToStep} />
          </motion.div>
        )}
        {/* Step 7: Validation report */}
        {step === 6 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <ValidationResultPage
              validationResult={validationResult}
              invoiceId={invoice?.invoiceId}
            />
          </motion.div>
        )}
      </MainContainer>
    </div>
  );
};

// Styled component for the main container
const MainContainer = styled.div`
  position: relative;
  width: 100%;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  z-index: 1;
`;

// Styled component for the background video
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
