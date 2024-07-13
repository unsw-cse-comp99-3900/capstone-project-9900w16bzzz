import React,{ useState} from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";

const ChooseConvertOption = ({ goToStep, setFile, file, setInvoice, invoice}) => {
    
  const [selectedAction, setSelectedAction] = useState(null);

  const handleActionChange = (action) => {
    setSelectedAction(action);
  };

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
    setFile(null);

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
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        const data = await response.json();
        
        if (data.ok) {
          setInvoice(data.data);
          console.log(invoice);
        }

        if (!data.ok) {
          throw new Error('Server response was not ok');
        }
        console.log('File processed successfully', data);
      }
    } catch (error) {
      console.error('Error processing file:', error);
      alert('An error occurred while processing the file. Please try again.');
    }
};

  return (
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
      <ArrowButton onClick={() => {
        handleUpload(selectedAction);
        handleNext();
      }} disabled={!selectedAction}>
        <ArrowIcon />
      </ArrowButton>
  </ActionContainer>
  )
}

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