import React,{ useState} from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";

const ChooseConvertOption = ({ goToStep }) => {
    
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
      <ArrowButton onClick={handleNext} disabled={!selectedAction}>
        <ArrowIcon />
      </ArrowButton>
  </ActionContainer>
  )
}

const ActionContainer = styled.div`
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