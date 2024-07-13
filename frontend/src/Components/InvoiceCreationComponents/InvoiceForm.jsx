import React, { useState } from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";
import FormInput from "./FormInput";

function InvoiceForm({ goToStep }) {
  const [formData, setFormData] = useState({});
    const [expandedSection, setExpandedSection] = useState(null);

    const toggleSection = (section) => {
        setExpandedSection(expandedSection === section ? null : section);
  };
  /*
  useEffect(() => {
    const fetchData = async () => {
        try {
            const response = await fetch('/api/prefill-data');
            const data = await response.json();
            setFormData(data);
        } catch (error) {
            console.error("Error fetching prefill data:", error);
        }
    };

    fetchData();
}, []);
*/
  const handleInputChange = (field, value) => {
    setFormData(prevData => ({
        ...prevData,
        [field]: value
    }));
  };

    const renderSection = (title, fields) => (
      <Section>
          <SectionHeader onClick={() => toggleSection(title)}>
              {title}
              {expandedSection === title ? ' ▼' : ' ▶'}
          </SectionHeader>
          {expandedSection === title && (
              <SectionContent>
                  <FieldGrid>
                      {fields.map((field, index) => (
                          <FieldWrapper key={index}>
                              <FormInput 
                                  type="text" 
                                  placeholder={field}
                                  value={formData[field] || ''}
                                  onChange={(e) => handleInputChange(field, e.target.value)}
                              />
                          </FieldWrapper>
                      ))}
                  </FieldGrid>
              </SectionContent>
          )}
      </Section>
  );

    return (
        <MainContainer className="name">
            <ArrowButton onClick={() => goToStep(2)}>
                <ArrowIcon style={{ transform: 'scaleX(-1)' }} />
            </ArrowButton>
            <Content>
                <HeaderContent>
                    <h1><span>Last Step </span>Convert to UBL</h1>
                    <p className="details" style={{margin:"10px"}}>To generate a UBL e-invoice, you may need to provide additional information that may not be present on your original invoice.</p>
                    <p className="details" style={{margin:"0"}}>Check the form below, fill out all necessary fields.</p>
                </HeaderContent>
                <ScrollableContent>
                    {renderSection("Supplier Info", [
                        "Supplier", "Address", "City", "Postal code", " Number", "ABN Number",
                        "IBAN supplier", "Payment reference", "Contact person", "Email"
                    ])}
                    
                    {renderSection("Customer Info", [
                        "Customer Name", "Customer Email", "Customer Address"
                    ])}
                    
                    {renderSection("Invoice Header", [
                        "Invoice Number", "Invoice Date", "Due Date"
                    ])}
                    
                    {renderSection("VAT & Financial", [
                        "VAT Rate", "Total Amount", "Currency"
                    ])}

                    <SubmitButton className="header-btn">Submit</SubmitButton>
                </ScrollableContent>
            </Content>
            <ArrowButton disabled style={{ opacity: 0, cursor: "not-allowed", "pointer-events": "none" }}>
                <ArrowIcon style={{ transform: 'scaleX(-1)' }} />
            </ArrowButton>
        </MainContainer>
    );
}

const MainContainer = styled.div`
    width: 80%;
    margin-top: 3%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20px;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 10px;
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    backdrop-filter: blur(2px);
    color: white;
    z-index: 1;
    height: 85%;
`;

const Content = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center;
    width: 80%;
    height: 100%;
    overflow: hidden;
    margin: 0 10px;
`;

const HeaderContent = styled.div`
    width: 100%;
    margin-bottom: 20px;
`;

const ScrollableContent = styled.div`
    width: 100%;
    height: calc(100% - 150px);
    overflow-y: auto;
    margin: 17px 0;
    box-sizing: content-box;

    /* 自定义滚动条样式 */
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
        border: 2px solid rgba(0, 0, 0, 0.5); // 添加边框，创造间距效果
    }

    &::-webkit-scrollbar-thumb:hover {
        background: rgba(255, 255, 255, 0.5);
    }
`;

const SectionContent = styled.div`
    padding: 20px 10px 10px 10px;
`;

const FieldGrid = styled.div`
    display: grid;
    grid-template-columns: 1fr;
    gap: 10px;
    width: 100%;

    @media (min-width: 768px) {
        grid-template-columns: 1fr 1fr;
    }
`;

const FieldWrapper = styled.div`
    width: 100%;
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
        border-radius: 2rem;
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
            fill: ${props => (props.disabled ? 'grey' : 'white')};
        }
        &:hover path {
            fill: ${props => (props.disabled ? 'grey' : '#6414FF')};
        }
    }
`;

const SubmitButton = styled.a`
    display: inline-block;
    margin: 15px 0;
    padding: 10px 20px;
    background-color: #6414FF;
    color: white;
    text-decoration: none;
    border-radius: 5px;
    
    &:hover {
        background-color: #5000CC;
    }
`;

export default InvoiceForm;