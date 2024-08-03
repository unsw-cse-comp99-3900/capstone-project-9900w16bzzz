import React, { useState } from "react";
import { FaEye, FaEyeSlash } from "react-icons/fa6";
import styled from "styled-components";
import Modal from "./Modal";

/**
 * Preview component for displaying and handling file previews.
 *
 * This component provides a preview option for different file types (PDF, JSON, XML).
 * It handles file preview actions and displays a modal for modified PDF files.
 *
 * @param {string} selectedFileType - The selected file type for preview.
 * @param {string} invoiceId - The ID of the invoice.
 * @param {number} validationFlag - The validation status of the invoice (0: not validated, 1: passed, 2: failed).
 */
const Preview = ({ selectedFileType, invoiceId, validationFlag }) => {
  const [showModal, setShowModal] = useState(false);

  /**
   * Handles the preview click event to open the file in a new tab.
   */
  const handlePreviewClick = async () => {
    const token = localStorage.getItem("token");
    const fileTypeMap = {
      pdf: 3,
      json: 1,
      xml: 2,
    };
    const fileType = fileTypeMap[selectedFileType];

    const response = await fetch(
      `${process.env.REACT_APP_SERVER_URL}/invoice/download?invoiceId=${invoiceId}&fileType=${fileType}`,
      {
        method: "GET",
        headers: {
          "x-access-token": `${token}`,
        },
      }
    );
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    window.open(url, "_blank");
  };

  /**
   * Handles the eye slash icon click event to show the modal.
   */
  const handleEyeSlashClick = () => {
    setShowModal(true);
  };

  /**
   * Handles closing the modal.
   */
  const handleCloseModal = () => {
    setShowModal(false);
  };

  return (
    <PreviewBox>
      {validationFlag === 0 || selectedFileType !== "pdf" ? (
        <EyeIcon onClick={handlePreviewClick} />
      ) : (
        <EyeSlashIcon onClick={handleEyeSlashClick} />
      )}
      {showModal && (
        <Modal
          message="The PDF file has been modified. Please view the JSON or XML file instead."
          onClose={handleCloseModal}
        />
      )}
    </PreviewBox>
  );
};

export default Preview;

// Styled components for the Preview component

const PreviewBox = styled.div`
  position: relative;
  margin: 30px auto;
  height: 40vh;
  width: 90%;
  background: rgba(0, 0, 0, 0.3);
  border-radius: 20px;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin-top: 30px;
    margin-left: 1px;
    margin-right: 15px;
    height: 220px;
    width: 100%;
  }
`;

const EyeIcon = styled(FaEye)`
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 2rem;
  &:hover {
    cursor: pointer;
  }
`;

const EyeSlashIcon = styled(FaEyeSlash)`
  position: relative;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 2.2rem;
  &:hover {
    cursor: pointer;
  }
`;
