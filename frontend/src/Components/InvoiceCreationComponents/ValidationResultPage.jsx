import React from 'react';
import styled from 'styled-components';


const ValidationResultPage = ({ validationResult }) => {
    const { successful, report } = validationResult;

    return (
      <MainContainer>
        <Content>
          <HeaderContent>
            <h1 style={{ fontSize: '64px' }}><span>Validation </span>Report</h1>
            <Status successful={successful}>
              {successful ? 'Success' : 'Failed'}
            </Status>
          </HeaderContent>
          <ScrollableContent>
            <Section>
              <SectionHeader>Summary</SectionHeader>
              <SectionContent>
                <p>{report.summary}</p>
                <p>File: {report.filename}</p>
                <p>Total Errors: {report.firedAssertionErrorsCount}</p>
              </SectionContent>
            </Section>
            
            {Object.entries(report.reports).map(([reportName, reportData]) => (
              <Section key={reportName}>
                <SectionHeader>{reportName} Validation Results</SectionHeader>
                <SectionContent>
                  <p>Status: {reportData.successful ? 'Successful' : 'Failed'}</p>
                  <p>Summary: {reportData.summary}</p>
                  <p>Errors: {reportData.firedAssertionErrorsCount}</p>
                  {reportData.firedAssertionErrors && reportData.firedAssertionErrors.length > 0 && (
                    <>
                      <SubSectionHeader>Validation Errors</SubSectionHeader>
                      {reportData.firedAssertionErrors.map((error, index) => (
                        <ErrorItem key={index}>
                          <ErrorCode>{error.id}</ErrorCode>
                          <ErrorText>{error.text}</ErrorText>
                          <ErrorLocation>Location: {error.location}</ErrorLocation>
                        </ErrorItem>
                      ))}
                    </>
                  )}
                </SectionContent>
              </Section>
            ))}
          </ScrollableContent>
        </Content>
      </MainContainer>
    );
  };
  
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
  `;
  
export default ValidationResultPage;
