import React,{ useState} from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";

const UploadPage = ({ goToStep }) => {
    
    const [file, setFile] = useState(null);

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };
    return (
        <Container className="name">
            <ArrowBackButton onClick={() => goToStep(0)}>
                <ArrowIcon />
            </ArrowBackButton>
            <Content>
                <h1><span>Step 1 </span> Upload File</h1>
                <p className="details">Please upload your invoice file. Accepted formats are PDF or JSON.</p>
                <FileInputWrapper>
                    <FileInputLabel htmlFor="file-upload"  selected={!!file}>
                        {file ? `Uploaded: ${file.name}` : 'Choose File'}
                    </FileInputLabel>
                    <FileInput id="file-upload" type="file" accept=".pdf, .json" onChange={handleFileChange} />
                </FileInputWrapper>
            </Content>
            <ArrowButton onClick={() => goToStep(2)} disabled={!file}>
                <ArrowIcon />
            </ArrowButton>
        </Container>
    )
}



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
`;

const FileInputWrapper = styled.div`
  margin-top: 20px;
  position: relative;
  display: inline-block;
`;

const FileInputLabel = styled.label`
  background-color: ${props => (props.selected ? '#6414FF' : 'transparent')};
  color: white;
  padding: 10px 20px;
  border: 2px solid #6414FF;
  border-radius: 100px;
  cursor: pointer;
  &:hover {
    background-color: #5011cc;
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
      fill: ${props => (props.disabled ? 'grey' : 'white')};
    }
    &:hover path {
      fill: ${props => (props.disabled ? 'grey' : '#6414FF')};
    }
  }

`;

const ArrowBackButton = styled(ArrowButton)`
  svg {
    transform: scaleX(-1);
  }
`;

export default UploadPage;