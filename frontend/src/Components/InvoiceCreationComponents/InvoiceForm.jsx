import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";
import FormInput from "./FormInput";
import SelectInput from "./FormSelector";
import CheckboxInput from "./CheckboxInput";

function InvoiceForm({ goToStep, invoice, setValidationResult }) {
    const [formData, setFormData] = useState({});
    const [expandedSection, setExpandedSection] = useState(null);
    const [isFormValid, setIsFormValid] = useState(false);
    const [selectedRules, setSelectedRules] = useState([]);
    
    const [isUploading, setIsUploading] = useState(false);

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

  const handleRuleChange = (rule) => {
    setSelectedRules(prevRules => 
        prevRules.includes(rule)
            ? prevRules.filter(r => r !== rule)
            : [...prevRules, rule]
    );
  };

  const generateRulesString = () => {
    return selectedRules.map(rule => `${rule}`).join(',');
  };

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
    if (field === 'dueDate' || field === 'invoiceDate') {
      setFormData(prevData => ({
        ...prevData,
        [field]: value
      }));
    } else {
      setFormData(prevData => {
        const newData = { ...prevData };
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

        if (keys[keys.length - 1] === 'type' && keys.includes('allowance')) {
          current['reason'] = typeReasonMapping[value];
        } else if (keys[keys.length - 1] === 'reason' && keys.includes('allowance')) {
          current['type'] = Object.keys(typeReasonMapping).find(key => typeReasonMapping[key] === value) || '';
        }
        return newData;
      });
    }
  };
    
  const renderField = (key, value, prefix = '') => {
    const fullKey = prefix ? `${prefix}.${key}` : key;
    if (key === 'dueDate' || key === 'invoiceDate') {
      return (
        <FieldWrapper key={fullKey}>
          <FormInput 
            type="date" 
            placeholder={key}
            value={value || ''}
            onChange={(newValue) => handleInputChange(fullKey, newValue)}
          />
        </FieldWrapper>
      );
    }
    if (key === 'invoiceLine' && Array.isArray(value)) {
      return value.map((item, index) => (
        <InvoiceLineWrapper key={`invoiceLine-${index}`}>
          <InvoiceLineHeader>Invoice Line ID: {item.id}</InvoiceLineHeader>
          <InvoiceLineFieldGrid>
            {Object.entries(item).map(([subKey, subValue]) => {
              if (subKey !== 'allowance') {
                return renderField(subKey, subValue, `${key}[${index}]`);
              }
              return null;
            })}
          </InvoiceLineFieldGrid>
          <AllowanceSection>
            <AllowanceHeader>Allowance</AllowanceHeader>
            <InvoiceLineFieldGrid>
              {Object.entries(item.allowance).map(([subKey, subValue]) =>
                renderField(subKey, subValue, `${key}[${index}].allowance`)
              )}
            </InvoiceLineFieldGrid>
          </AllowanceSection>
        </InvoiceLineWrapper>
      ));
    } else if (typeof value === 'object' && value !== null && !Array.isArray(value)) {
      return Object.entries(value).map(([subKey, subValue]) => 
        renderField(subKey, subValue, prefix ? `${prefix}.${key}` : key)
      );
    } else {
      if (fullKey === 'allowance.type' || fullKey === 'allowance.reason') {
        return (
          <FieldWrapper key={fullKey}>
            <SelectInput 
              placeholder={key}
              value={value || ''}
              onChange={(newValue) => handleInputChange(fullKey, newValue)}
              options={optionMappings[key]}
              onRelatedChange={(relatedKey, relatedValue) => {
                handleInputChange(`allowance.${relatedKey}`, relatedValue);
              }}
              relatedField={fullKey === 'allowance.type' ? 'reason' : 'type'}
              typeReasonMapping={typeReasonMapping}
            />
          </FieldWrapper>
        );
      }
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
  const typeReasonMapping = {
    'SAA': 'Shipping and Handling',
    'AA': 'Advertising Allowance',
    'AB': 'Special Allowance',
    'AC': 'Discount',
    'AD': 'Bouns',
    'AE': 'Coupon',
    'AF': 'Freight',
    'AG': 'Insurance',
    'AH': 'Other',
    'AI': 'Packaging',
    'AJ': 'Quantity Discount',
    'AK': 'Rebate',
    'AL': 'Returns',
    'AM': 'Trade Discount',
    'AN': 'Volume Discount'
  };
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
          { value: 'SAA', label: 'SAA' },
          { value: 'AA', label: 'AA' },
          { value: 'AB', label: 'AB' },
          { value: 'AC', label: 'AC' },
          { value: 'AD', label: 'AD' },
          { value: 'AE', label: 'AE' },
          { value: 'AF', label: 'AF' },
          { value: 'AG', label: 'AG' },
          { value: 'AH', label: 'AH' },
          { value: 'AI', label: 'AI' },
          { value: 'AJ', label: 'AJ' },
          { value: 'AK', label: 'AK' },
          { value: 'AL', label: 'AL' },
          { value: 'AM', label: 'AM' },
          { value: 'AN', label: 'AN' }
        ],
        code: [
            { value: '10', label: 'Cash' },
            { value: '20', label: 'Cheque' },
            { value: '30', label: 'Credit Transfer' },
            { value: '42', label: 'Credit card' },
            { value: '48', label: 'Debit Card' }
        ],
        reason: [
            { value: 'Shipping and Handling', label: 'Shipping and Handling' },
            { value: 'Advertising Allowance', label: 'Advertising Allowance' },
            { value: 'Special Allowance', label: 'Special Allowance' },
            { value: 'Discount', label: 'Discount' },
            { value: 'Bouns', label: 'Bouns' },
            { value: 'Coupon', label: 'Coupon' },
            { value: 'Freight', label: 'Freight' },
            { value: 'Insurance', label: 'Insurance' },
            { value: 'Other', label: 'Other' },
            { value: 'Packaging', label: 'Packaging' },
            { value: 'Quantity Discount', label: 'Quantity Discount' },
            { value: 'Rebate', label: 'Rebate' },
            { value: 'Returns', label: 'Returns' },
            { value: 'Trade Discount', label: 'Trade Discount' },
            { value: 'Volume Discount', label: 'Volume Discount' }
        ]
    };

    const uploadEditedInvoice = async () => {
      if (!selectedRules) {
          alert('You have to select validation rule!');
          return;
      }

      const formattedData = {...formData};
      if (formattedData.dueDate) {
        formattedData.dueDate = new Date(formattedData.dueDate).toISOString().split('T')[0];
      }
      if (formattedData.invoiceDate) {
        formattedData.invoiceDate = new Date(formattedData.invoiceDate).toISOString().split('T')[0];
      }
      if (formattedData.invoiceDate) {
        formattedData.invoiceDate = new Date(formattedData.invoiceDate).toISOString().split('T')[0];
      }


      console.log('formData before sending:', JSON.stringify(formattedData, null, 2));
      setIsUploading(true);
      try {
        const invoiceId = invoice.invoiceId;
        const rules = generateRulesString();
        //const rules = "rules=AUNZ_PEPPOL_1_0_10,AUNZ_PEPPOL_SB_1_0_10AUNZ_UBL_1_0_10";
        let endpoint = `${process.env.REACT_APP_SERVER_URL}/invoice/validate?invoiceId=${invoiceId}&rules=${rules}`;
        let token = localStorage.getItem("token");
        
          const response = await fetch(`${endpoint}`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'x-access-token': `${token}`
            },
            body: JSON.stringify(formattedData),
          });
        if (!response.ok) {
          setIsUploading(false);
              throw new Error('Network response was not ok');
          }
          const data = await response.json();
        if (data.ok) {
          setIsUploading(false);
            console.log(data);
            setValidationResult(data.data);
            goToStep(5);
          }
        if (!data.ok) {
          setIsUploading(false);
            throw new Error('Server response was not ok');
          }
          
      } catch (error) {
        setIsUploading(false);
          console.error('Error processing file:', error);
          alert('An error occurred while processing the file. Please try again.');
        }
    }

  return (
      <>
      {isUploading && <Loading />}
        <MainContainer className="name">
            <ArrowButton onClick={() => goToStep(2)}>
                <ArrowIcon style={{ transform: 'scaleX(-1)' }} />
            </ArrowButton>
            <Content>
                <HeaderContent>
                  <h1><span>Last Step </span>Convert to UBL</h1>
                  <p className="details">To generate a UBL e-invoice, you may need to provide additional information that may not be present on your original invoice.</p>
                  <p className="details">Check the form below, fill out all necessary fields.</p>
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
                      <CheckboxWrapper>
                        <CheckboxInput
                            options={[
                                { value: 'AUNZ_PEPPOL_1_0_10', label: 'Peppol Standard Rule 1.0.10' },
                                { value: 'AUNZ_PEPPOL_SB_1_0_10', label: 'Peppol Small Business Rule 1.0.10' },
                                { value: 'AUNZ_UBL_1_0_10', label: 'UBL Standard Rule 1.0.10' }
                            ]}
                            selectedRules={selectedRules}
                            onChange={handleRuleChange}
                        />
                      </CheckboxWrapper>
                    </ValidationWrapper>
                </ScrollableContent>
            </Content>
            <ArrowButton disabled style={{ opacity: 0, cursor: "not-allowed", "pointerEvents": "none" }}>
                <ArrowIcon style={{ transform: 'scaleX(-1)' }} />
            </ArrowButton>
      </MainContainer>
      </>
    );
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
      <LoadingMessage>Validating... Please wait.</LoadingMessage>
    </LoadingScreen>
  </>
);

const ValidationWrapper = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  margin: 15px 0;
  position: relative;
`;

const AllowanceSection = styled.div`
  margin-top: 20px;
  padding: 10px;
  border: 1px solid rgba(255, 255, 255, 0.3);
  border-radius: 5px;
  background-color: rgba(255, 255, 255, 0.1);
`;

const AllowanceHeader = styled.h5`
  color: white;
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 10px;
`;

const ButtonWrapper = styled.div`
  display: flex;
  justify-content: center;
`;

const CheckboxWrapper = styled.div`
  position: absolute;
  right: 0;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    top: 5px; /* 向下移动的距离，可以根据需要调整 */
  }
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

    @media only screen and (max-width: 430px) and (max-height: 932px) {
    width: 95%;
    height: 90%;
    margin-top: 20%;
    padding: 15px;
    flex-direction: column;
    justify-content: center;
    }

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

    h1 {
    font-size: 64px;
    }

    .details {
      margin: 10px 0;
    }

    @media only screen and (max-width: 430px) and (max-height: 932px) {
    h1 {
      font-size: 30px; /* 调整为适应较小屏幕的字体大小 */
    }

    .details {
      font-size: 14px; /* 调整为适应较小屏幕的字体大小 */
      margin: 5px 0; /* 调整为适应较小屏幕的内边距 */
      }
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

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    height: calc(100% - 150px); /* 调整高度以适应iPhone 14 Pro Max */
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

    @media only screen and (device-width: 430px) and (device-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    position: absolute;
    left: 0px; /* 调整为适应iPhone 14 Pro Max的左侧位置 */
    top: 300px; /* 调整为适应iPhone 14 Pro Max的顶部位置 */
    svg {
      height: 25px; /* 调整为适应iPhone 14 Pro Max的图标大小 */
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

  @media only screen and (device-width: 430px) and (device-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    position: fixed;
    bottom: 1px; /* 距离页面底部的距离，可以根据需要调整 */
    top:820px; 
    left: 50%;
    transform: translateX(-50%);
    width: calc(100% - 250px); /* 使按钮宽度适应屏幕 */
    margin: 0;
    border-radius: 1rem; /* 可选：调整边框圆角以适应底部样式 */
  }
  
`;

export default InvoiceForm;