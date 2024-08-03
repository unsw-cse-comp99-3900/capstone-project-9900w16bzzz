import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";
import { usePopup } from "../../PopupWindow/PopupContext";

/**
 * ChooseConvertOption component to handle the selection of saving or converting an invoice.
 * @param {Function} goToStep - Function to navigate to a specific step.
 * @param {Function} setFile - Function to set the selected file.
 * @param {File} file - The currently selected file.
 * @param {Function} setInvoice - Function to set the invoice data.
 * @param {Object} invoice - The invoice data.
 */
const ChooseConvertOption = ({
  goToStep,
  setFile,
  file,
  setInvoice,
  invoice,
}) => {
  const { showPopup } = usePopup();
  const [selectedAction, setSelectedAction] = useState(null);
  const [isUploading, setIsUploading] = useState(false);

  /**
   * Handler for selecting an action (save or convert).
   * @param {string} action - The selected action.
   */
  const handleActionChange = (action) => {
    setSelectedAction(action);
  };

  /**
   * Effect to log the updated invoice data.
   */
  useEffect(() => {
    console.log("Updated invoice:", invoice);
  }, [invoice]);

  /**
   * Handler for navigating to the next step based on the selected action.
   */
  const handleNext = () => {
    if (selectedAction === "save") {
      goToStep(4);
    } else if (selectedAction === "convert") {
      goToStep(3);
    }
  };

  /**
   * Handler for uploading the selected file.
   * @param {string} action - The selected action.
   * @returns {boolean} - True if the upload was successful, false otherwise.
   */
  const handleUpload = async (action) => {
    console.log(file);

    if (!file) {
      showPopup("No file to upload!", "error");
      return false;
    }
    setIsUploading(true);

    try {
      const formData = new FormData();
      formData.append("file", file);
      const userId = localStorage.getItem("userId");
      let endpoint = `${process.env.REACT_APP_SERVER_URL}/invoice/upload`;
      let token = localStorage.getItem("token");
      if (selectedAction === "save" || selectedAction === "convert") {
        const response = await fetch(`${endpoint}?userId=${userId}`, {
          method: "POST",
          headers: {
            "x-access-token": `${token}`,
          },
          body: formData,
        });

        setFile(null);
        if (!response.ok) {
          throw new Error("Network response was not ok");
        }
        const data = await response.json();

        if (data.ok) {
          setInvoice(data.data);
        }
        setIsUploading(false);
        if (!data.ok) {
          const errorMsg = data.msg;
          const match = errorMsg.match(/"detail":\s*"([^"]*)"/);
          if (match && match[1]) {
            throw new Error(match[1]);
          } else {
            throw new Error(errorMsg);
          }
        }
        console.log("File processed successfully", data);
        return true;
      }
    } catch (error) {
      console.error("Error processing file:", error);
      showPopup(
        `An error occurred while processing the file. Please try again.${error}`,
        "error"
      );
      setIsUploading(false);
      return false;
    }
  };

  return (
    <>
      {isUploading && <Loading />}
      <PageContainer>
        <ActionContainer className="name">
          <ArrowBackButton onClick={() => goToStep(1)}>
            <ArrowIcon />
          </ArrowBackButton>
          <Content>
            <Heading>
              <span>Step 2 </span> Select save format{" "}
            </Heading>
            <p className="details">
              Upload the original file <b>OR</b> convert it to standard UBL
              e-Invoice.
            </p>
            {/* Options to choose save or convert action */}
            <ActionOptions>
              <ActionOption
                selected={selectedAction === "save"}
                onClick={() => handleActionChange("save")}
              >
                <OptionLabel>Save Directly</OptionLabel>
              </ActionOption>
              <ActionOption
                selected={selectedAction === "convert"}
                onClick={() => handleActionChange("convert")}
              >
                <OptionLabel>Convert to UBL</OptionLabel>
              </ActionOption>
            </ActionOptions>
          </Content>
          <ArrowButton
            onClick={async () => {
              const uploadSuccess = await handleUpload(selectedAction);
              if (uploadSuccess) {
                handleNext();
              }
            }}
            disabled={!selectedAction || isUploading}
          >
            <ArrowIcon />
          </ArrowButton>
        </ActionContainer>
      </PageContainer>
    </>
  );
};

/**
 * Styled components for various UI elements.
 */

const BlurredBackground = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(5px);
  z-index: 1000;
`;

const LoadingScreen = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1001;
`;

const LoadingMessage = styled.div`
  color: white;
  font-size: 24px;
  background-color: rgba(100, 20, 255, 0.8);
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
`;

const Loading = () => (
  <>
    <BlurredBackground />
    <LoadingScreen>
      <LoadingMessage>Uploading... Please wait.</LoadingMessage>
    </LoadingScreen>
  </>
);

const PageContainer = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
  width: 100vw;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    height: auto;
    padding: 20px;
  }
`;

const ActionContainer = styled.div`
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 10px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(10px);

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    width: 90%;
    padding: 15px;
  }
`;

const Content = styled.div`
  flex: 1;
  text-align: center;
  margin: 0 20px;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin: 0 5px;
  }
`;

const Heading = styled.h1`
  margin: 0;
  font-size: 24px;

  @media only screen and (max-width: 430px) {
    font-size: 35px !important;
  }
`;

const ActionOptions = styled.div`
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-bottom: 20px;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    flex-direction: column;
    align-items: center;
  }
`;

const ActionOption = styled.div`
  width: 200px;
  background-color: ${(props) => (props.selected ? "#6414FF" : "transparent")};
  border: 2px solid #6414ff;
  border-radius: 100px;
  padding: 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  text-align: center;

  &:hover {
    background-color: #5011cc;
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    padding: 12px;
    width: 90%;
    margin-bottom: 10px;
  }
`;

const OptionLabel = styled.div`
  color: white;
  font-size: 16px;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 14px;
  }
`;

const ArrowButton = styled.button`
  background-color: transparent;
  border: none;
  cursor: pointer;
  padding: 0;
  svg {
    width: auto;
    height: 50px;
    path {
      fill: ${(props) => (props.disabled ? "grey" : "white")};
    }
    &:hover path {
      fill: ${(props) => (props.disabled ? "grey" : "#6414FF")};
    }
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    svg {
      height: 40px;
    }
  }
`;

const ArrowBackButton = styled(ArrowButton)`
  svg {
    transform: scaleX(-1);
  }
`;

export default ChooseConvertOption;
