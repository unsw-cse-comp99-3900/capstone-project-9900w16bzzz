import React, { useState, useEffect, useCallback } from "react";
import { useParams } from "react-router-dom";
import { motion } from "framer-motion";
import styled from "styled-components";
import video from "../../images/video1.mp4";
import InvoiceForm from "../CreateInvoice/InvoiceCreationComponents/InvoiceForm";
import ValidationResultPage from "../CreateInvoice/InvoiceCreationComponents/ValidationResultPage";
import ValidationSucceedPage from "../CreateInvoice/InvoiceCreationComponents/ValidationSucceedPage";
import { usePopup } from "../PopupWindow/PopupContext";

/**
 * ValidationPage component to handle the validation of a specific invoice.
 */
const ValidationPage = () => {
  const { showPopup } = usePopup();
  const { invoiceId } = useParams(); // Get the invoice ID from the URL parameters
  const [step, setStep] = useState(0); // State to manage the current step
  const [invoice, setInvoice] = useState({
    invoiceId: invoiceId,
    invoiceJsonVO: null,
  }); // State to manage the invoice details
  const [validationResult, setValidationResult] = useState(null); // State to manage the validation result
  const [shouldRefresh, setShouldRefresh] = useState(false); // State to manage the page refresh

  /**
   * Function to navigate to a specific step.
   * @param {number} stepNumber - The step number to navigate to.
   */
  const goToStep = useCallback((stepNumber) => {
    setStep(stepNumber);
    if (stepNumber === 0) {
      setShouldRefresh(true); // Trigger refresh if navigating back to the first step
    }
  }, []);

  // Transition settings for animations
  const transition = {
    duration: 2,
  };

  /**
   * Function to load the invoice details from the server.
   */
  const loadPage = useCallback(async () => {
    try {
      const endpoint = `${process.env.REACT_APP_SERVER_URL}/invoice/json`;
      const token = localStorage.getItem("token");
      const response = await fetch(
        `${endpoint}?invoiceId=${invoice.invoiceId}`,
        {
          method: "GET",
          headers: {
            "x-access-token": `${token}`,
          },
        }
      );

      if (!response.ok) {
        throw new Error("Network response was not ok");
      }
      const data = await response.json();

      if (data.ok) {
        setInvoice((prevInvoice) => ({
          ...prevInvoice,
          invoiceJsonVO: data.data,
        }));
      } else {
        throw new Error("Server response was not ok");
      }
    } catch (error) {
      console.error("Error processing file:", error);
      showPopup(
        "An error occurred while processing the file. Please try again.",
        "error"
      );
    }
  }, [invoice.invoiceId, showPopup]);

  // Effect to load the page when the component mounts
  useEffect(() => {
    loadPage();
  }, [loadPage]);

  // Effect to refresh the page when necessary
  useEffect(() => {
    if (shouldRefresh && step === 0) {
      loadPage();
      setShouldRefresh(false);
    }
  }, [shouldRefresh, step, loadPage]);

  return (
    <div>
      <MainContainer id="main" style={{ height: "auto" }}>
        {/* Background video */}
        <BackgroundVideo autoPlay muted loop id="background-video">
          <source src={video} type="video/mp4" />
          Your browser does not support the video tag.
        </BackgroundVideo>
        {/* Conditional rendering for each step with animations */}
        {/* Step 1: Read invoice JSON file and load invoice form */}
        {step === 0 && (
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
              type="validation"
            />
          </motion.div>
        )}
        {/* Step 2: Validation succeed page */}
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
        {/* Step 3: Validation report */}
        {step === 6 && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            exit={{ opacity: 0 }}
            transition={transition}
          >
            <ValidationResultPage
              validationResult={validationResult}
              invoiceId={invoiceId}
              setStep={goToStep}
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

export default ValidationPage;
