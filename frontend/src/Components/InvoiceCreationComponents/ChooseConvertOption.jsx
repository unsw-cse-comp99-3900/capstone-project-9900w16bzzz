import React,{ useState, useEffect} from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";

const ChooseConvertOption = ({ goToStep, setFile, file, setInvoice, invoice}) => {
    
  const [selectedAction, setSelectedAction] = useState(null);
  const [isUploading, setIsUploading] = useState(false);

  const handleActionChange = (action) => {
    setSelectedAction(action);
  };

  useEffect(() => {
    console.log("Updated invoice:", invoice);
  }, [invoice]);

  const handleNext = () => {
    if (selectedAction === 'save') {
      goToStep(4);
    } else if (selectedAction === 'convert') {
      goToStep(3);
    }
  };

  const handleUpload = async (action) => {
    console.log(file);

    if (!file) {
        alert('No file to upload!');
        return;
    }
    setIsUploading(true);

    try {
      const formData = new FormData();
      formData.append('file', file);
      const userId = localStorage.getItem("userId");
      let endpoint = `${process.env.REACT_APP_SERVER_URL}/invoice/upload`;
      let token = localStorage.getItem("token");
      if (selectedAction === 'save' || selectedAction === 'convert') {
        const response = await fetch(`${endpoint}?userId=${userId}`, {
          method: 'POST',
          headers: {
              'x-access-token': `${token}`
          },
          body: formData,
        });
        
        setFile(null);
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        
        if (data.ok) {
          setInvoice(data.data);
        }
        setIsUploading(false);
        if (!data.ok) {
          throw new Error('Server response was not ok');
        }
        console.log('File processed successfully', data);
        return true;
      }
    } catch (error) {
      console.error('Error processing file:', error);
      alert('An error occurred while processing the file. Please try again.');
      setIsUploading(false);
      return false;
    }
};

  return (
  <>
    {isUploading && <Loading />}
    <ActionContainer className="name">
      <ArrowButton onClick={() => goToStep(1)}>
          <ArrowIcon style={{ transform: 'scaleX(-1)' }} />
      </ArrowButton>
      <div>
        <h1><span>Step 2 </span> Select save format </h1>
        <p className="details">Upload the original file <b>OR</b> convert it to standard UBL e-Invoice.</p>
        <ActionOptions>
          <ActionOption selected={selectedAction === 'save'} onClick={() => handleActionChange('save')}>
            <OptionLabel>Save Directly</OptionLabel>
          </ActionOption>
          <ActionOption selected={selectedAction === 'convert'} onClick={() => handleActionChange('convert')}>
            <OptionLabel>Convert to UBL</OptionLabel>
          </ActionOption>
        </ActionOptions>
      </div>
      <ArrowButton onClick={async () => {
          const uploadSuccess = await handleUpload(selectedAction);
          if (uploadSuccess) {
            handleNext();
          }
        }} 
        disabled={!selectedAction || isUploading}>
        <ArrowIcon />
      </ArrowButton>
      </ActionContainer>
    </>
    )
}

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
`;

const ActionOptions = styled.div`
  display: flex;
  justify-content: space-around;
  width: 100%;
  margin-bottom: 20px;
`;

const ActionOption = styled.div`
  width:200px;
  background-color: ${props => (props.selected ? '#6414FF' : 'transparent')};
  border: 2px solid #6414FF;
  border-radius: 100px;
  padding: 20px;
  cursor: pointer;
  transition: background-color 0.3s;
  &:hover {
    background-color: #5011cc;
  }
`;

const OptionLabel = styled.div`
  color: white;
  font-size: 16px;
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
      fill: ${props => (props.disabled ? 'grey' : 'white')};
    }
    &:hover path {
      fill: ${props => (props.disabled ? 'grey' : '#6414FF')};
    }
  }
`;

export default ChooseConvertOption;