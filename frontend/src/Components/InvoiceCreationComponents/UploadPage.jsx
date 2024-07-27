import React from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";

const UploadPage = ({ goToStep, setFile, file }) => {
  const handleFileChange = (event) => {
    const selectedFile = event.target.files[0];
    if (typeof setFile === "function") {
      setFile(selectedFile);
    } else {
      console.error("setFile is not a function");
    }
  };

  const truncateText = (text, maxLength) => {
    if (text.length > maxLength) {
      return text.substring(0, maxLength) + "...";
    }
    return text;
  };

  const isIphone14ProMax = () => {
    const width = window.innerWidth;
    const height = window.innerHeight;
    const pixelRatio = window.devicePixelRatio;
    return width === 430 && height === 932 && pixelRatio === 3;
  };

  return (
    <Container className="name">
      <ArrowBackButton onClick={() => goToStep(0)}>
        <ArrowIcon />
      </ArrowBackButton>
      <Content>
        <Heading>
          <span>Step 1 </span> Upload File{" "}
        </Heading>
        <p className="details">
          Please upload your invoice file. Accepted formats are PDF or JSON.
        </p>
        <FileInputWrapper>
          <FileInputLabel htmlFor="file-upload" selected={!!file}>
            {file
              ? `Uploaded: ${
                  isIphone14ProMax() ? truncateText(file.name, 10) : file.name
                }`
              : "Choose File"}
          </FileInputLabel>
          <FileInput
            id="file-upload"
            type="file"
            accept=".pdf, .json"
            onChange={handleFileChange}
          />
        </FileInputWrapper>
      </Content>
      <ArrowButton onClick={() => goToStep(2)} disabled={!file}>
        <ArrowIcon />
      </ArrowButton>
    </Container>
  );
};

const Container = styled.div`
  width: 80%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 10px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(2px);
  color: white;

  @media only screen and (device-width: 430px) and (device-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    width: 90%;
    padding: 15px;
  }
`;

const Heading = styled.h1`
  margin: 0;
  font-size: 24px;

  @media only screen and (max-width: 430px) {
    font-size: 30px !important;
  }
`;

const FileInputWrapper = styled.div`
  margin-top: 20px;
  position: relative;
  display: inline-block;
`;

const FileInputLabel = styled.label`
  background-color: ${(props) => (props.selected ? "#6414FF" : "transparent")};
  color: white;
  padding: 10px 20px;
  border: 2px solid #6414ff;
  border-radius: 100px;
  cursor: pointer;
  &:hover {
    background-color: #5011cc;
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 0.75rem; /* Adjust font size for iPhone 14 Pro Max */
    width: 80%; /* Ensure the label takes full width */
    text-align: center; /* Center the text */
  }
`;

const FileInput = styled.input`
  opacity: 0;
  width: 0;
  height: 0;
  position: absolute;
`;

const Content = styled.div`
  text-align: center;

  margin-bottom: 20px;

  h1 {
    margin: 0;
  }

  .details {
    margin-bottom: 20px;
  }

  input {
    margin-top: 20px;
  }
`;

const ArrowButton = styled.button`
  background-color: transparent;
  cursor: pointer;
  border: none;
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
`;

const ArrowBackButton = styled(ArrowButton)`
  svg {
    transform: scaleX(-1);
  }
`;

export default UploadPage;
