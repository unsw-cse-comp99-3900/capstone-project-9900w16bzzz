import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";
import FormInput from "./FormInput";
import SelectInput from "./FormSelector";

function InvoiceForm({ goToStep, invoice }) {
    const [formData, setFormData] = useState({});
    const [expandedSection, setExpandedSection] = useState(null);
    const [isFormValid, setIsFormValid] = useState(false);
    const [selectedRule, setSelectedRule] = useState(null);

    useEffect(() => {
        setIsFormValid(validateForm(formData));
    }, [formData]);
    
    useEffect(() => {
        if (invoice && invoice.invoiceJsonVO) {
            setFormData(invoice.invoiceJsonVO);
        }
    }, [invoice]);

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
    
const validateForm = (data) => {
    const isValid = (obj) => {
      for (let key in obj) {
        if (obj.hasOwnProperty(key)) {
          if (obj[key] === null || obj[key] === undefined || obj[key] === '') {
            return false;
          }
          if (typeof obj[key] === 'object') {
            if (!isValid(obj[key])) {
              return false;
            }
          }
        }
      }
      return true;
    };
  
    return isValid(data);
    };
    
    const findEmptyFields = (data, prefix = '') => {
        let emptyFields = [];
        for (let key in data) {
          if (data.hasOwnProperty(key)) {
            const fullKey = prefix ? `${prefix}.${key}` : key;
            if (data[key] === null || data[key] === undefined || data[key] === '') {
              emptyFields.push(fullKey);
            } else if (typeof data[key] === 'object') {
              emptyFields = [...emptyFields, ...findEmptyFields(data[key], fullKey)];
            }
          }
        }
        return emptyFields;
      };
      
      

const handleInputChange = (field, value) => {
    setFormData(prevData => {
      const newData = {...prevData};
      const keys = field.split('.');
      let current = newData;
      for (let i = 0; i < keys.length - 1; i++) {
        const key = keys[i];
        if (key.includes('[')) {
          const [arrayName, index] = key.split('[');
          if (!current[arrayName]) current[arrayName] = [];
          const arrayIndex = parseInt(index);
          if (!current[arrayName][arrayIndex]) current[arrayName][arrayIndex] = {};
          current = current[arrayName][arrayIndex];
        } else {
          if (!(key in current)) current[key] = {};
          current = current[key];
        }
      }
      current[keys[keys.length - 1]] = value;
      return newData;
    });
  };
    
  const renderField = (key, value, prefix = '') => {
    if (key === 'invoiceLine' && Array.isArray(value)) {
        return value.map((item, index) => (
            <InvoiceLineWrapper key={`invoiceLine-${index}`}>
              <InvoiceLineHeader>Invoice Line ID: {item.id}</InvoiceLineHeader>
              <InvoiceLineFieldGrid>
                {Object.entries(item).map(([subKey, subValue]) => 
                  renderField(subKey, subValue, `${key}[${index}]`)
                )}
              </InvoiceLineFieldGrid>
            </InvoiceLineWrapper>
          ));
    } else if (typeof value === 'object' && value !== null && !Array.isArray(value)) {
      return Object.entries(value).map(([subKey, subValue]) => 
        renderField(subKey, subValue, prefix ? `${prefix}.${key}` : key)
      );
    } else {
        const fullKey = prefix ? `${prefix}.${key}` : key;
        if (optionMappings[key]) {
            return (
                <FieldWrapper key={fullKey}>
                    <SelectInput 
                        placeholder={key}
                        value={value || ''}
                        onChange={(newValue) => handleInputChange(fullKey, newValue)}
                        options={optionMappings[key]}
                    />
                </FieldWrapper>
            );
        } else {
            return (
                <FieldWrapper key={fullKey}>
                    <FormInput 
                        type="text" 
                        placeholder={key}
                        value={value || ''}
                        onChange={(newValue) => handleInputChange(fullKey, newValue)}
                    />
                </FieldWrapper>
            );
        }
    }
  };


  const renderSection = (title, data) => (
    <Section key={title}>
        <SectionHeader onClick={() => toggleSection(title)}>
            {title}
            {expandedSection === title ? ' ▼' : ' ▶'}
        </SectionHeader>
        {expandedSection === title && (
            <SectionContent>
                <FieldGrid>
                    {Object.entries(data).map(([key, value]) => renderField(key, value))}
                </FieldGrid>
            </SectionContent>
        )}
    </Section>
);

    const sections = [
        { title: "Invoice Details", fields: ["invoiceId", "invoiceDate", "dueDate", "typeCode", "currencyCode"] },
        { title: "Buyer", fields: ["buyer"] },
        { title: "Seller", fields: ["seller"] },
        { title: "Financial Details", fields: ["subTotal", "invoiceTotal", "taxTotal"] },
        { title: "allowance", fields: ["allowance"] },
        { title: "Invoice Lines", fields: ["invoiceLine"] },
        { title: "Payment", fields: ["payment"] },
        { title: "Delivery Address", fields: ["deliveryAddress"] }
    ];
    const optionMappings = {
        schemeId: [
            { value: 'GLN', label: 'Global Location Number' },
            { value: '0060', label: 'Data Universal Numbering System' },
            { value: 'VATDE', label: 'German VAT Number' },
            { value: 'ABN', label: 'Australian Business Number' },
            { value: 'EORI', label: 'Economic Operators Registration and Identification' },
            { value: 'LEI', label: 'Legal Entity Identifier' },
            { value: 'SWIFT', label: 'SWIFT Code' },
            { value: 'GSRN', label: 'Global Service Relation Number' },
            { value: 'NACE', label: 'European Standard Industry Classification Code' },
            { value: 'NIF', label: 'Spanish Tax Identification Number' }
        ],
        type: [
            { value: '10', label: 'Standard rate' },
            { value: '0', label: 'Zero rate' },
            { value: '0', label: 'Exempt rate' },
            { value: '0', label: 'Reduce rate' }
        ],
        code: [
            { value: '10', label: 'Cash' },
            { value: '20', label: 'Cheque' },
            { value: '30', label: 'Credit Transfer' },
            { value: '42', label: 'Credit card' },
            { value: '48', label: 'Debit Card' }
        ],
        reason: [
            { value: 'SAA', label: 'Shipping and Handling' },
            { value: 'AA', label: 'Advertising Allowance' },
            { value: 'AB', label: 'Special Allowance' },
            { value: 'AC', label: 'Discount' },
            { value: 'AD', label: 'Bouns' },
            { value: 'AE', label: 'Coupon' },
            { value: 'AF', label: 'Freight' },
            { value: 'AG', label: 'Insurance' },
            { value: 'AH', label: 'Other' },
            { value: 'AI', label: 'Packaging' },
            { value: 'AJ', label: 'Quantity Discount' },
            { value: 'AK', label: 'Rebate' },
            { value: 'AL', label: 'Returns' },
            { value: 'AM', label: 'Trade Discount' },
            { value: 'AN', label: 'Volume Discount' }
        ]
    };

    const uploadEditedInvoice = async () => {
        if (!selectedRule) {
            alert('You have to select validation rule!');
            return;
        }

        try {
            const invoiceId = invoice.invoiceId;
            const rules = "rules=AUNZ_PEPPOL_1_0_10&rules=AUNZ_PEPPOL_SB_1_0_10&rules=AUNZ_UBL_1_0_10";
            let endpoint = `${process.env.REACT_APP_SERVER_URL}/invoice/validate?invoiceId=${invoiceId}&${rules}`;
            let token = localStorage.getItem("token");
            const response = await fetch(`${endpoint}`, {
              method: 'POST',
              headers: {
                  'Content-Type': 'application/json',
                  'x-access-token': `${token}`
              },
              body: JSON.stringify(formData),
            });

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            const data = await response.json();
            if (data.ok) {
                console.log(data);
            }
            if (!data.ok) {
              throw new Error('Server response was not ok');
            }
            
          } catch (error) {
            console.error('Error processing file:', error);
            alert('An error occurred while processing the file. Please try again.');
          }

    }

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
                    {sections.map(section => 
                        renderSection(section.title, 
                            Object.fromEntries(
                                Object.entries(formData)
                                    .filter(([key]) => section.fields.includes(key))
                            )
                        )
                    )}
                    <ValidationWrapper>
                      <ButtonWrapper>
                        <SubmitButton 
                          className="header-btn" 
                          onClick={() => {
                            if (isFormValid) {
                              console.log(formData);
                              console.log('Selected Rule:', selectedRule);
                                uploadEditedInvoice();
                            } else {
                              const emptyFields = findEmptyFields(formData);
                              alert(`Please fill in the following fields: ${emptyFields.join(', ')}`);
                            }
                          }} 
                          isValid={isFormValid}
                        >
                          Validate
                        </SubmitButton>
                      </ButtonWrapper>
                      <SelectWrapper>
                        <SelectInput
                          placeholder={'Validation Rule'}
                          value={selectedRule || ''}
                          onChange={(value) => setSelectedRule(value)}
                          options={[
                            { value: 'default', label: 'Default Rule    ' },
                            { value: 'strict', label: 'Strict Rule    ' },
                            { value: 'loose', label: 'Loose Rule    ' }
                          ]}
                        />
                      </SelectWrapper>
                    </ValidationWrapper>
                </ScrollableContent>
            </Content>
            <ArrowButton disabled style={{ opacity: 0, cursor: "not-allowed", "pointerEvents": "none" }}>
                <ArrowIcon style={{ transform: 'scaleX(-1)' }} />
            </ArrowButton>
        </MainContainer>
    );
}

const ValidationWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin: 15px 0;
  position: relative;
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: center;
`;

const SelectWrapper = styled.div`
  position: absolute;
  right: 0;
`;

const FieldGrid = styled.div`
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 20px;
    width: 100%;

    @media (min-width: 1000px) {
        grid-template-columns: 1fr 1fr;
    }
`;

const InvoiceLineWrapper = styled.div`
margin-bottom: 20px;
padding: 10px;
border: 1px solid rgba(255, 255, 255, 0.3);
border-radius: 5px;
background-color: rgba(255, 255, 255, 0.05);
`;

const InvoiceLineFieldGrid = styled(FieldGrid)`
  margin-top: 15px;
`;

const InvoiceLineHeader = styled.h4`
margin-bottom: 20px;
color: white;
font-size: 18px;
font-weight: bold;
`;

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
        border: 2px solid rgba(0, 0, 0, 0.5); 
    }

    &::-webkit-scrollbar-thumb:hover {
        background: rgba(255, 255, 255, 0.5);
    }
`;

const SectionContent = styled.div`
    padding: 20px 10px 10px 10px;
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

const SubmitButton = styled.button`
  display: inline-block;
  margin: 15px 0;
  padding: 10px 20px;
  background-color: #6414FF;
  color: white;
  text-decoration: none;
  border-radius: 2rem;
  border: none;
  cursor: pointer;
  transition: all 0.3s ease;
  opacity: ${props => props.isValid ? 1 : 0.6};
  
  &:hover {
    background-color: #5000CC;
  }
`;

export default InvoiceForm;