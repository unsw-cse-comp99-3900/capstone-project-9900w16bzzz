import React, { useEffect, useState, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import video from "../images/video1.mp4";
import styled from "styled-components";
import { BsBoxArrowInLeft } from "react-icons/bs";
import Preview from "./Preview";
import SendInvoice from "./Sendinvoice";

function Invoicedetail() {
    const { invoiceId } = useParams();
    const [invoice, setInvoice] = useState(null);
    const [selectedFileType, setSelectedFileType] = useState(null);
    const navigate = useNavigate();

    const getFileNameWithoutExtension = (fileName) => {
        return fileName.replace(/\.[^/.]+$/, "");
    };

    const fetchInvoiceDetail = useCallback(async () => {
        const userId = localStorage.getItem('userId');
        const token = localStorage.getItem('token');
        try {
            const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/invoice/list`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    'x-access-token': `${token}`
                },
                body: JSON.stringify({
                    pageNum: 1,
                    pageSize: 10,
                    searchCount: true,
                    sortItemList: [
                        {
                            isAsc: true,
                            column: "string"
                        }
                    ],
                    userId: userId
                })
            });
            const data = await response.json();
            console.log("Fetched invoice details:", data);

            if (data && data.data && data.data.list) {
                const foundInvoice = data.data.list.find(inv => inv.invoiceId.toString() === invoiceId);
                setInvoice(foundInvoice);
                if (foundInvoice.pdfFlag === 1) {
                    setSelectedFileType("pdf");
                } else if (foundInvoice.jsonFlag === 1) {
                    setSelectedFileType("json");
                } else if (foundInvoice.xmlFlag === 1) {
                    setSelectedFileType("xml");
                }
            } else {
                console.error("Invalid API response structure:", data);
            }
        } catch (error) {
            console.error("Error fetching invoice details:", error);
        }
    }, [invoiceId]);

    useEffect(() => {
        fetchInvoiceDetail();
    }, [fetchInvoiceDetail]);

    const handleArrowClick = () => {
        navigate("/my-invoice");
    };

    const handleFileTypeChange = (event) => {
        setSelectedFileType(event.target.value);
    };

    return (
        <div id="main">
            <VideoBackground autoPlay muted loop id="background-video-signup">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
            </VideoBackground>
            <Maincontainer>
                <ArrowIcon onClick={handleArrowClick} />
                {invoice ? (
                    <>
                        <InvoiceName>
                            <Title>{getFileNameWithoutExtension(invoice.fileName)}</Title>
                            <FileTypeDropdown>
                                <select onChange={handleFileTypeChange} value={selectedFileType}>
                                    {invoice.pdfFlag === 1 && <option value="pdf">PDF</option>}
                                    {invoice.jsonFlag === 1 && <option value="json">JSON</option>}
                                    {invoice.xmlFlag === 1 && <option value="xml">XML</option>}
                                </select>
                            </FileTypeDropdown>
                            <Validate>Validate</Validate>
                        </InvoiceName>
                        <Preview
                            selectedFileType={selectedFileType}
                            invoiceId={invoiceId}
                        />
                        <SendInvoice 
                            invoiceId={invoiceId}
                            selectedFileType={selectedFileType}
                            fileName={getFileNameWithoutExtension(invoice.fileName)}
                        />
                    </>
                ) : (
                    <LoadingMessage>Loading invoice details...</LoadingMessage>
                )}
            </Maincontainer>
        </div>
    );
}

export default Invoicedetail;

const VideoBackground = styled.video`
  position: fixed;
  top:  0%;
  left: 0%;
  min-width: 10%;
  min-height: 130%;
  width: 90%;
  height: 100%;
  z-index: -1;
  transform: translate(-0%, -0%);
  object-fit: cover;
`;


const Maincontainer = styled.div`
    display: flex;
    position: relative;
    justify-content: flex-start;
    align-items: flex-start;
    flex-direction: column;
    height: 500px;
    width: 650px;
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    backdrop-filter: invert(20%);
    border-radius: 10px;
    color: #ffffff;
    z-index: 1;

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    width: 90%;
    height: auto;
    padding: 20px;
    margin-top: 300px;
    }
`;

const ArrowIcon = styled(BsBoxArrowInLeft)`
    position: relative;
    top: 20px;
    left: 29px;
    font-size: 2rem;
    &:hover {
        cursor: pointer;
    }

    media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    top: 10px;
    left: 10px;
    font-size: 1.5rem;
    }
`;

const InvoiceName = styled.div`
    display: flex;
    margin-top: 20px;
    flex-direction: row;
    align-items: center;
    justify-content: flex-start;
    height: 60px;
    width: 100%;
    border-bottom: 3px solid rgba(255, 255, 255, 0.2);
    padding-right: 20px;

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin-top: 20px;
    padding-right: 10px;
    }

`;

const Title = styled.h1`
    color: #ffffff;
    letter-spacing: 0.1rem;
    font-size: 1.5rem;
    padding-left: 80px;
    margin-right: 20px;

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 1.2rem;
    padding-left: 17px;
    margin-right: 17px;
    }

`;

const FileTypeDropdown = styled.div`
    position: relative;
    margin-left: 20px;
    z-index: 1;
    select {
        background: rgba(0, 0, 0, 0.3);
        border: none;
        padding: 10px;
        border-radius: 15px;
        color: #ffffff;
        font-size: 1.1rem;
        &:focus {
            outline: none;
        }
    }

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin-left:20px; 
    }
    
`;

const Validate = styled.button`
    background-color: #5011cc;
    position: relative;
    color: white;
    height: 38px;
    padding: 10px 20px;
    border: 2px solid #6414FF;
    border-radius: 20px;
    cursor: pointer;
    letter-spacing: 0.1rem;
    font-size: 1rem;
    text-transform: uppercase;
    margin-left: auto;
    &:hover {
        background-color: transparent;
        transition: all ease 0.5s;
    }
`;

const LoadingMessage = styled.div`
    color: #ffffff;
    font-size: 1.5rem;
    text-align: center;
    margin-top: 50px;
`;
