import React, { useState } from "react";
import styled from "styled-components";

const SendInvoice = ({ invoiceId, selectedFileType, fileName }) => {
    const [email, setEmail] = useState("");

    const handleEmailChange = (event) => {
        setEmail(event.target.value);
    };

    const handleSendInvoice = async () => {
        if (!email) {
            alert("Please enter an email address.");
            return;
        }

        if (!selectedFileType) {
            alert("Please select a file type.");
            return;
        }

        const fileTypeMapping = {
            json: 1,
            xml: 2,
            pdf: 3
        };

        const fileType = fileTypeMapping[selectedFileType];
        if (!fileType) {
            alert("Invalid file type selected.");
            return;
        }

        const token = localStorage.getItem('token');
        const requestBody = {
            targetEmail: email,
            subject: `Invoice ${fileName}`,
            text: `Please find attached the invoice in ${selectedFileType} format.`
        };

        console.log("Sending invoice with request body:", requestBody);

        try {
            const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/invoice/send?invoiceId=${invoiceId}&fileType=${fileType}`, {
                method: 'POST',
                headers: {
                    "Content-Type": "application/json",
                    'x-access-token': `${token}`
                },
                body: JSON.stringify(requestBody)
            });

            const data = await response.json();

            if (response.ok && data.ok) {
                alert("Invoice sent successfully!");
            } else {
                console.error("Failed to send invoice:", data);
                alert(`Failed to send invoice. Error: ${data.msg || 'Unknown error'}`);
            }
        } catch (error) {
            console.error("Error sending invoice:", error);
            alert(`Error sending invoice: ${error.message}`);
        }
    };

    return (
        <EmailBox>
            <EmailInput>
                <input 
                    type="text" 
                    placeholder="Email address" 
                    value={email} 
                    onChange={handleEmailChange} 
                />
            </EmailInput>
            <EmailButton onClick={handleSendInvoice}>Send</EmailButton>
        </EmailBox>
    );
};

export default SendInvoice;

const EmailBox = styled.div`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 20px;
    height: 100px;
    margin-left: 30px;
    margin-right: 30px;
    width: 90%;

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin-top: 40px;
    height: auto;
    margin-left: 30px;
    margin-right: 20px;
    }
`;

const EmailInput = styled.div`
    position: relative;

    input {
        padding: 10px;
        width: 280px;
        border: none;
        border-radius: 20px;
        background: rgba(255, 255, 255, 0.2);
        color: #ffffff;
        font-size: 1rem;
        outline: none;
        box-shadow: 0 0 0 0.1rem #9a86d2;

        &::placeholder {
            color: rgba(255, 255, 255, 0.7);
        }
        &:focus {
            display: inline-block;
            box-shadow: 0 0 0 0.2rem #b9abe0;
            backdrop-filter: blur(12rem);
            border-radius: 2rem;
        }
    }

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    input {
      width: 200px;
      font-size: 0.9rem;
        }
    }
`;

const EmailButton = styled.button`
    background-color: #5011cc;
    height: 40px;
    position: relative;
    color: white;
    padding: 10px 20px;
    border-radius: 20px;
    border: 2px solid #6414FF;
    cursor: pointer;
    letter-spacing: 0.1rem;
    font-size: 1rem;
    margin-left: 20px;
    text-transform: uppercase;
    &:hover {
        background-color: transparent;
        transition: all ease 0.5s;
    }
`;
