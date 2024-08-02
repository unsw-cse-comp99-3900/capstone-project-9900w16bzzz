import React, { useState } from "react";
import styled from "styled-components";
import { usePopup } from "./PopupWindow/PopupContext";
import Modal from "./Modal";

const SendInvoice = ({
  invoiceId,
  selectedFileType,
  fileName,
  validationFlag,
}) => {
  const [email, setEmail] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [modalMessage, setModalMessage] = useState("");
  const { showPopup } = usePopup();

  const handleEmailChange = (event) => {
    setEmail(event.target.value);
  };

  const handleSendInvoice = async () => {
    if (!email) {
      showPopup("Please enter an email address.", "error");
      return;
    }

    if (!selectedFileType) {
      showPopup("Please select a file type.", "error");
      return;
    }

    const fileTypeMapping = {
      json: 1,
      xml: 2,
      pdf: 3,
    };

    const fileType = fileTypeMapping[selectedFileType];
    if (!fileType) {
      showPopup("Invalid file type selected.", "error");
      return;
    }


    if (validationFlag === 2) {
      setModalMessage("A file that failed validation cannot be sent.");
      setIsModalOpen(true);
      return;
    }

    if (validationFlag !== 0 && selectedFileType === "pdf") {
      setModalMessage("The PDF file has been modified. Please send the JSON or XML file instead.");
      setIsModalOpen(true);
      return;
    }


    const token = localStorage.getItem("token");
    const requestBody = {
      targetEmail: email,
      subject: `Invoice ${fileName}`,
      text: `Please find attached the invoice in ${selectedFileType} format.`,
    };

    console.log("Sending invoice with request body:", requestBody);

    setIsLoading(true);

    try {
      const response = await fetch(
        `${process.env.REACT_APP_SERVER_URL}/invoice/send?invoiceId=${invoiceId}&fileType=${fileType}`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "x-access-token": `${token}`,
          },
          body: JSON.stringify(requestBody),
        }
      );

      const data = await response.json();
      setIsLoading(false);

      if (response.ok && data.ok) {
        showPopup("Invoice sent successfully!", "success");
      } else {
        console.error("Failed to send invoice:", data);
        showPopup(
          `Failed to send invoice. Error: ${data.msg || "Unknown error"}`,
          "error"
        );
      }
    } catch (error) {
      console.error("Error sending invoice:", error);
      setIsLoading(false);
      showPopup(`Error sending invoice: ${error.message}`, "error");
    }
  };

  return (
    <EmailBox>
      <EmailInput>
        <input
          type="text"
          placeholder="Email address"
          value={email}
          onChange={handleEmailChange}
          disabled={isLoading}
        />
      </EmailInput>
      <EmailButton onClick={handleSendInvoice} disabled={isLoading}>
        Send
      </EmailButton>
      {isLoading && (
        <LoadingOverlay>
          <LoadingMessage>Sending Invoice...</LoadingMessage>
        </LoadingOverlay>
      )}
      {isModalOpen && (
        <Modal
          message={modalMessage}
          onClose={() => setIsModalOpen(false)}
        />
      )}
    </EmailBox>
  );
};

export default SendInvoice;

const EmailBox = styled.div`
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 100px;
  margin-left: 30px;
  margin-right: 30px;
  width: 90%;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin-top: 40px;
    height: auto;
    margin-left: 30px;
    margin-right: 20px;
  }
`;

const EmailInput = styled.div`
  position: relative;

  input {
    padding: 10px;
    width: 22vw;
    border: none;
    border-radius: 20px;
    background: rgba(255, 255, 255, 0.2);
    color: #ffffff;
    font-size: 1rem;
    outline: none;
    box-shadow: 0 0 0 0.1rem #9a86d2;

    &::placeholder {
      color: rgba(255, 255, 255, 0.7);
    }
    &:focus {
      display: inline-block;
      box-shadow: 0 0 0 0.2rem #b9abe0;
      backdrop-filter: blur(12rem);
      border-radius: 2rem;
    }
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    input {
      width: 200px;
      font-size: 0.9rem;
    }
  }
`;

const EmailButton = styled.button`
  background-color: #5011cc;
  height: 40px;
  position: relative;
  color: white;
  padding: 10px 20px;
  border-radius: 20px;
  border: 2px solid #6414ff;
  cursor: pointer;
  letter-spacing: 0.1rem;
  font-size: 1rem;
  margin-left: 20px;
  text-transform: uppercase;
  &:hover {
    background-color: transparent;
    transition: all ease 0.5s;
  }
  &:disabled {
    cursor: not-allowed;
    opacity: 0.5;
  }
`;

const LoadingOverlay = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
`;

const LoadingMessage = styled.div`
  background-color: #5011cc;
  color: white;
  padding: 20px 30px;
  border-radius: 20px;
  font-size: 1.2rem;
  font-weight: bold;
  text-transform: uppercase;
`;
