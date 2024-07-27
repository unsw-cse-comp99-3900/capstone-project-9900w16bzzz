import React, { useState, useEffect, useCallback } from "react";
import { useParams } from "react-router-dom";
import { motion } from "framer-motion";
import styled from "styled-components";
import video from "../../images/video1.mp4";
import InvoiceForm from ".././InvoiceCreationComponents/InvoiceForm";
import ValidationResultPage from ".././InvoiceCreationComponents/ValidationResultPage";
import ValidationSucceedPage from ".././InvoiceCreationComponents/ValidationSucceedPage";
import { usePopup } from "../PopupWindow/PopupContext";

const ValidationPage = () => {
  const { showPopup } = usePopup();
  const { invoiceId } = useParams();
  const [step, setStep] = useState(0);
  const [invoice, setInvoice] = useState({
    invoiceId: invoiceId,
    invoiceJsonVO: null,
  });
  const [validationResult, setValidationResult] = useState(null);

  const goToStep = (stepNumber) => setStep(stepNumber);
  const transition = {
    duration: 2,
  };

  const loadPage = useCallback(async () => {
    try {
      let endpoint = `${process.env.REACT_APP_SERVER_URL}/invoice/json`;
      let token = localStorage.getItem("token");
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
      }
      if (!data.ok) {
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

  useEffect(() => {
    loadPage();
  }, [loadPage]);

  return (
    <div>
      <MainContainer id="main" style={{ height: "auto" }}>
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
            <InvoiceForm
              goToStep={goToStep}
              invoice={invoice}
              setValidationResult={setValidationResult}
              type="validation"
            />
          </motion.div>
        )}
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
              setStep={setStep}
            />
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

export default ValidationPage;
