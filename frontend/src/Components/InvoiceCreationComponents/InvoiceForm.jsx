import React, { useState, useEffect } from "react";
import styled from "styled-components";
import { ReactComponent as ArrowIcon } from "../../images/arrow.svg";
import FormInput from "./FormInput";
import SelectInput from "./FormSelector";
import CheckboxInput from "./CheckboxInput";
import { usePopup } from "../PopupWindow/PopupContext";

function InvoiceForm({ goToStep, invoice, setValidationResult, type = 'creation' }) {
  const isValidation = type === 'validation';
    const { showPopup } = usePopup();
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
          let modifiedData = { ...invoice.invoiceJsonVO };
          console.log(modifiedData);
    
          modifiedData = setDefaultValues(modifiedData);
          
          setFormData(modifiedData);
      }
    }, [invoice]);

    const toggleSection = (section) => {
        setExpandedSection(expandedSection === section ? null : section);
  };

  const setDefaultValues = (data) => {
  
    let newData = JSON.parse(JSON.stringify(data));

    newData.typeCode = "380 commercial invoice";
  
    if (newData.invoiceLine && Array.isArray(newData.invoiceLine)) {
      newData.invoiceLine = newData.invoiceLine.map(line => {
        const amount = parseFloat(line.amount || '0');
        let taxRate = line.taxRate;
        if (taxRate && typeof taxRate === 'string') {
          if (taxRate.endsWith('%')) {
            taxRate = taxRate.slice(0, -1);
          }
          taxRate = parseFloat(taxRate) || "10";
        } else {
          taxRate = "10";
        }
        
        let calculatedTax = amount * taxRate / 100;
        calculatedTax = calculatedTax.toFixed(2);
    
        return {
          ...line,
          unit: line.unit || 'pcs',
          taxRate: taxRate,
          tax: calculatedTax.toString(),
          allowance: {
            ...line.allowance,
            amount: line.allowance?.amount || '0',
            type: line.allowance?.type || 'SAA',
            reason: line.allowance?.reason || 'Shipping and Handling',
            taxPercent: line.allowance?.taxPercent || '10',
            currencyCode: line.allowance?.currencyCode || newData.currencyCode
          }
        };
      });
    }

    newData.buyer.schemeId = newData.buyer.schemeId || '0060';
    if (newData.buyer.address.countryCode) {
      if (newData.buyer.address.countryCode === 'null') {
        newData.buyer.address.countryCode = null;
      }
    }

    if (newData.buyer.address.countryCode) {
      if (newData.seller.address.countryCode === 'null') {
        newData.seller.address.countryCode = null;
      }
    }

    if (newData.buyer.address.countryCode) {
      if (newData.deliveryAddress.countryCode === 'null') {
        newData.deliveryAddress.countryCode = null;
      }
    }

    if (!newData.deliveryAddress) {
      newData.deliveryAddress = {
        address: "address",
        city: "city",
        countryCode: "countryCode",
        postalCode: "postal code",
        street: "st"
      };
    }
    
    newData.seller.schemeId = newData.seller.schemeId || '0060';
    

    newData.allowance = {
      ...newData.allowance,
      amount: newData.allowance.amount || '0',
      type: newData.allowance.type || 'SAA',
      reason: newData.allowance.reason || 'Shipping and Handling',
      taxPercent: newData.allowance.taxPercent || '10',
      currencyCode: newData.allowance.currencyCode || newData.currencyCode
    };

    newData.payment = {
      ...newData.payment,
      code: newData.payment.code || '10',
      accountName: newData.payment.accountName || 'account name',
      accountNumber: newData.payment.accountNumber || 'account number',
      bsbNumber: newData.payment.bsbNumber || 'business number',
      paymentNote: newData.payment.paymentNote || 'additional note'
    };

    return newData;
  };

  const handleRuleChange = (rule) => {
    setSelectedRules(prevRules => 
        prevRules.includes(rule)
            ? prevRules.filter(r => r !== rule)
            : [...prevRules, rule]
    );
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
    if (hiddenFields.includes(key)) {
      return null;
    }
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
          {!(selectedRules.includes('AUNZ_UBL_1_0_10') && selectedRules.length === 1) && (
            <AllowanceSection>
            <AllowanceHeader>Allowance</AllowanceHeader>
            <InvoiceLineFieldGrid>
              {Object.entries(item.allowance).map(([subKey, subValue]) =>
                renderField(subKey, subValue, `${key}[${index}].allowance`)
              )}
            </InvoiceLineFieldGrid>
          </AllowanceSection>
          )}
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


  const renderSection = (title, data) => {
    const isUBLStandardRuleSelected = selectedRules.includes('AUNZ_UBL_1_0_10') && selectedRules.length === 1;
    if ((title === "Payment" || title === "Delivery Address") && isUBLStandardRuleSelected) {
      return null;
    }
  
    return (
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
  };
  const hiddenFields = ['typeCode','schemeId'];
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

  const roundToTwoDecimals = (num) => {
    return Number(parseFloat(num).toFixed(2)).toFixed(2);
  };
  
  const shouldRound = (key, value) => {
    const nonRoundFields = ['id', 'invoiceId', 'schemeId', 'quantity','taxRate','taxPercent','postalCode','phone','address','street'];
    
    if (nonRoundFields.includes(key)) {
      return false;
    }
    return typeof value === 'string' && !isNaN(value);
  };
  
  const roundAllNumbers = (obj) => {
    if (typeof obj !== 'object' || obj === null) {
      return obj;
    }
  
    if (Array.isArray(obj)) {
      return obj.map(roundAllNumbers);
    }
  
    const result = {};
    for (const [key, value] of Object.entries(obj)) {
      if (shouldRound(key, value)) {
        result[key] = roundToTwoDecimals(value);
      } else if (typeof value === 'object' && value !== null) {
        result[key] = roundAllNumbers(value);
      } else {
        result[key] = value;
      }
    }
    return result;
  };
    
  const handleUBLStandardRule = (dataToSend) => {

    const modifiedData = JSON.parse(JSON.stringify(dataToSend));

    // delete illegal fields
    delete modifiedData.payment;
    delete modifiedData.deliveryAddress;

    //delete allowance for all invoice line
    if (Array.isArray(modifiedData.invoiceLine)) {
      modifiedData.invoiceLine = modifiedData.invoiceLine.map(line => {
        const { allowance, ...lineWithoutAllowance } = line;
        return lineWithoutAllowance;
      });
    }

    const roundedData = roundAllNumbers(modifiedData);

    return roundedData;
  }

  const uploadEditedInvoice = async () => {
    if (!selectedRules) {
        showPopup('You have to select validation rule!','error');
        return;
    }
    const formattedData = {...formData};
    if (formattedData.dueDate) {
      formattedData.dueDate = new Date(formattedData.dueDate).toISOString().split('T')[0];
    }
    if (formattedData.invoiceDate) {
      formattedData.invoiceDate = new Date(formattedData.invoiceDate).toISOString().split('T')[0];
    }
    
    const invoiceId = invoice.invoiceId;
    console.log('formData before sending:', JSON.stringify(formattedData, null, 2));
    setIsUploading(true);
    try {
      let endpoint = `${process.env.REACT_APP_SERVER_URL}/invoice/update?invoiceId=${invoiceId}`;
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
        console.log('update invoice successfully.');
      }
      if (!data.ok) {
        setIsUploading(false);
        throw new Error('Server response was not ok');
      }
    } catch (error) {
      setIsUploading(false);
      showPopup(`An error occurred while updating the invoice. Please try again. Error info: ${error}`, 'error');
      return;
    }
    try {
      //const rules = "rules=AUNZ_PEPPOL_1_0_10,AUNZ_PEPPOL_SB_1_0_10,AUNZ_UBL_1_0_10";
      const validationResults = [];
      let token = localStorage.getItem("token");
      
      for (const rule of selectedRules) {
        let dataToSend = formattedData;

        if (rule === 'AUNZ_UBL_1_0_10') {
          dataToSend = handleUBLStandardRule(formattedData);
          console.log("data to validate", dataToSend);
        }

        let endpoint = `${process.env.REACT_APP_SERVER_URL}/invoice/validate?invoiceId=${invoiceId}&rules=${rule}`;
        
        const response = await fetch(endpoint, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'x-access-token': token
          },
          body: JSON.stringify(dataToSend),
        });
        if (!response.ok) {
          throw new Error(`Network response was not ok for rule: ${rule}`);
        }
    
        const data = await response.json();
    
        if (data.ok) {
          console.log(`Validation result for rule ${rule}:`, data);
          validationResults.push({ rule, result: data.data });
        } else {
          throw new Error(`Server response was not ok for rule: ${rule}`);
        }
      }
      setIsUploading(false);
      setValidationResult(validationResults);
      goToStep(5);
        
    } catch (error) {
      setIsUploading(false);
      showPopup(`An error occurred while processing the file. Please try again. Error type: ${error}`,'error');
    }
  }

  const buttonStyle = isValidation 
  ? { opacity: 0, cursor: "not-allowed", pointerEvents: "none" } 
  : {};

  return (
      <>
      {isUploading && <Loading />}
        <MainContainer className="name">
        <ArrowButton
          disabled={isValidation} 
          style={buttonStyle}
          onClick={() => goToStep(2)}
        >
                <ArrowIcon style={{ transform: 'scaleX(-1)' }} />
            </ArrowButton>
            <Content>
                <HeaderContent>
                {isValidation ?
                  <h1><span>Convert </span>to UBL</h1> :
                  <h1><span>Last Step </span>Convert to UBL</h1>
                }
                  <p className="details">Additional information not shown on the invoice may be required.</p>
                  <p className="details">Please select validation rules and check information correctness.</p>
                  <CheckboxWrapper>
                    <CheckboxInput
                          options={[
                            { value: 'AUNZ_UBL_1_0_10', label: 'UBL Standard Rule 1.0.10' },
                            { value: 'AUNZ_PEPPOL_1_0_10', label: 'Peppol Standard Rule 1.0.10' },
                            { value: 'AUNZ_PEPPOL_SB_1_0_10', label: 'Peppol Small Business Rule 1.0.10' }
                        ]}
                        selectedRules={selectedRules}
                        onChange={handleRuleChange}
                    />
                  </CheckboxWrapper>
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
                            if (isFormValid && selectedRules.length > 0) {
                              console.log(selectedRules);
                              console.log(formData);
                                uploadEditedInvoice();
                            } else if (selectedRules.length === 0) {
                              showPopup(`Please select a validation rule!`,'error');
                            }
                            else {
                              const emptyFields = findEmptyFields(formData);
                              showPopup(`Please fill in the following fields: ${emptyFields.join(', ')}`,'error');
                            }
                          }} 
                          isvalid={isFormValid}
                        >
                          Validate
                        </SubmitButton>
                      </ButtonWrapper>
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
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    justify-content: center;
    width: 100%;

    @media (max-width: 768px) {
        flex-direction: column;
        gap: 10px;
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
    height: 90%;

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
            font-size: 30px;
        }

        .details {
            font-size: 14px;
            margin: 5px 0;
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
    left: 0px;
    top: 300px; 
    svg {
      height: 25px; 
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
  opacity: ${props => props.isvalid ? 1 : 0.6};
  
  &:hover {
    background-color: #5000CC;
  }

  @media only screen and (device-width: 430px) and (device-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    position: fixed;
    bottom: 1px; 
    top:820px; 
    left: 50%;
    transform: translateX(-50%);
    width: calc(100% - 250px);
    margin: 0;
    border-radius: 1rem;
  }
  
`;

export default InvoiceForm;