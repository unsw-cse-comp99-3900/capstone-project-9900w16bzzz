import React from 'react';
import styled from 'styled-components';


const ValidationResultPage = ({ validationResult }) => {
  const overallSuccessful = validationResult.every(result => result.result.successful);

  return (
    <MainContainer>
      <Content>
        <HeaderContent>
          <Title><span>Validation </span>Report</Title>
          <Status successful={overallSuccessful}>
            {overallSuccessful ? 'Success' : 'Failed'}
          </Status>
        </HeaderContent>
        <ScrollableContent>
          {validationResult.map((result, index) => {
            const { rule, result: { successful, report } } = result;
            return (
              <Section key={index}>
                <SectionHeader>{rule} Validation Results</SectionHeader>
                <SectionContent>
                  <p>Status: {successful ? 'Successful' : 'Failed'}</p>
                  <p>Summary: {report.summary}</p>
                  <p>File: {report.filename}</p>
                  <p>Total Errors: {report.firedAssertionErrorsCount}</p>
                  
                  {Object.entries(report.reports).map(([reportName, reportData]) => (
                    <SubSection key={reportName}>
                      <SubSectionHeader>Details</SubSectionHeader>
                      <p>{reportData.summary}</p>
                      <p>Errors number: {reportData.firedAssertionErrorsCount}</p>
                      {reportData.firedAssertionErrors && reportData.firedAssertionErrors.length > 0 && (
                        <>
                          <ErrorHeader>Validation Errors</ErrorHeader>
                          {reportData.firedAssertionErrors.map((error, errorIndex) => (
                            <ErrorItem key={errorIndex}>
                              <ErrorCode>{error.id}</ErrorCode>
                              <ErrorText>{error.text}</ErrorText>
                              <ErrorLocation>Location: {error.location}</ErrorLocation>
                            </ErrorItem>
                          ))}
                        </>
                      )}
                    </SubSection>
                  ))}
                </SectionContent>
              </Section>
            );
          })}
        </ScrollableContent>
      </Content>
    </MainContainer>
  );
};
  const Title = styled.h1`
  font-size: 64px;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 38px;
  }
  `;
  const SubSectionHeader = styled.h4`
  margin-top: 20px;
  margin-bottom: 10px;
  color: #ffffff;
  `;
  const MainContainer = styled.div`
    width: 80%;
    margin: 3% auto;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 10px;
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    backdrop-filter: blur(2px);
    color: white;
    z-index: 1;
    height: 85vh;

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
        width: 400px; 
        height: 90vh; 
        padding: 15px;
        margin-top: 120px; 
        margin-left: 1%; 
    }


  `;
  
  const Content = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    width: 100%;
    height: 100%;
    overflow: hidden;
    margin: 0 10px;
  `;
  
  const HeaderContent = styled.div`
    width: 100%;
    margin-bottom: 20px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  `;
  
const Status = styled.span`
    width:15vh;
    text-align:center;
    padding: 5px 10px;
    border-radius: 4px;
    font-weight: bold;
    color: white;
    background-color: ${props => props.successful ? '#4CAF50' : '#F44336'};

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
        margin-top: 50px; 
        margin-right: 12%;
    }

  `;
  
  const ScrollableContent = styled.div`
    width: 100%;
    height: calc(100% - 150px);
    overflow-y: auto;
    margin: 17px 0;
    box-sizing: content-box;
  
    &::-webkit-scrollbar {
      width: 10px;
    }
  
    &::-webkit-scrollbar-track {
      background: rgba(255, 255, 255, 0.1);
      border-radius: 10px;
    }
  
    &::-webkit-scrollbar-thumb {
      background: rgba(255, 255, 255, 0.3);
      border-radius: 10px;
      border: 2px solid rgba(0, 0, 0, 0.5); 
    }
  
    &::-webkit-scrollbar-thumb:hover {
      background: rgba(255, 255, 255, 0.5);
    }
  `;
  
  
  const Section = styled.div`
    margin-bottom: 10px;
    border: 1px solid rgba(255, 255, 255, 0.2);
    border-radius: 2rem;
  `;
  
  const SectionHeader = styled.div`
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px;
    cursor: pointer;
    background-color: rgba(255, 255, 255, 0.1);
    border-radius: 2rem;
    
    &:hover {
      background-color: rgba(255, 255, 255, 0.2);
    }
  `;
  
  const SectionContent = styled.div`
    padding: 0 30px;
  `;
  
  const ErrorItem = styled.div`
    background-color: rgba(255, 255, 255, 0.05);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 5px;
    padding: 10px;
    margin-bottom: 10px;
  `;
  
  const ErrorCode = styled.div`
    font-weight: bold;
    color: #F44336;
    margin-bottom: 5px;
  `;
  
  const ErrorText = styled.div`
    margin-bottom: 5px;
  `;
  
  const ErrorLocation = styled.div`
    font-size: 0.9em;
    color: rgba(255, 255, 255, 0.7);

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    word-wrap: break-word; 
    word-break: break-all; 
  }

  `;

  const SubSection = styled.div`
  margin-top: 20px;
  padding: 10px;
  background-color: rgba(255, 255, 255, 0.05);
  border-radius: 5px;
`;

const ErrorHeader = styled.h5`
  margin-top: 15px;
  margin-bottom: 10px;
  color: #F44336;
`;
  
export default ValidationResultPage;
