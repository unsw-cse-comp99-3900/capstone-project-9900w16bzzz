import React, { useEffect, useState } from "react";
import video from "../images/video1.mp4";
import styled from "styled-components";
import { FaSearch } from "react-icons/fa";
import { BiDownload } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import deleteInvoice from "./Deleteinvoice";
import DownloadInvoice from "./Downloadinvoice";

function Myinvoice() {
    const [invoiceData, setInvoiceData] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [openDropdown, setOpenDropdown] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetchInvoices();
    }, []);

    const fetchInvoices = async () => {
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
            console.log('API Response:', data);

            if (data && data.data && data.data.list) {
                setInvoiceData(data.data.list);
            } else {
                console.error("Invalid API response structure:", data);
            }
        } catch (error) {
            console.error("Error fetching invoices:", error);
        }
    };

    const handleInvoiceClick = (invoiceId) => {
        navigate(`/invoice/${invoiceId}`);
    };

    const handleDeleteClick = (event, invoiceId) => {
        event.stopPropagation();
        deleteInvoice(invoiceId, setInvoiceData, invoiceData);
    };

    const handleDropdownToggle = (event, invoiceId) => {
        event.stopPropagation();
        setOpenDropdown(openDropdown === invoiceId ? null : invoiceId);
    };

    const handleDownloadClick = (event, invoiceId, fileType) => {
        event.stopPropagation();
        event.preventDefault();
        DownloadInvoice(invoiceId, fileType);
    };

    const filteredInvoices = invoiceData.filter((invoice) =>
        invoice.fileName.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div id="main">
            <video autoPlay muted loop id="background-video-signup">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
            </video>
            <Container>
                <Title><Mytext>My </Mytext>Invoice</Title>
                <Maincontainer>
                    <Search>
                        <SearchBox>
                            <SearchIcon />
                            <input
                                type="text"
                                placeholder="Search..."
                                value={searchTerm}
                                onChange={(e) => setSearchTerm(e.target.value)}
                            />
                        </SearchBox>
                    </Search>
                    <Invoicecontainer>
                        <InvoiceList>
                            {filteredInvoices.map((invoice) => (
                                <InvoiceItem key={invoice.invoiceId} onClick={() => handleInvoiceClick(invoice.invoiceId)}>
                                    <InvoiceName>{invoice.fileName}</InvoiceName>
                                    {invoice.validationFlag === 1 && (
                                        <ValidationPass>
                                            <StatusCircleGreen />
                                            Validation Pass
                                        </ValidationPass>
                                    )}
                                    {invoice.validationFlag === 2 && (
                                        <ValidationFail>
                                            <StatusCircleRed />
                                            Validation Fail
                                        </ValidationFail>
                                    )}
                                    <DownloadContainer>
                                        <Download onClick={(event) => handleDropdownToggle(event, invoice.invoiceId)} />
                                        {openDropdown === invoice.invoiceId && (
                                            <DownloadOptions>
                                                {invoice.pdfFlag === 1 && <DownloadOption onClick={(event) => handleDownloadClick(event, invoice.invoiceId, 3)}>PDF</DownloadOption>}
                                                {invoice.jsonFlag === 1 && <DownloadOption onClick={(event) => handleDownloadClick(event, invoice.invoiceId, 1)}>JSON</DownloadOption>}
                                                {invoice.xmlFlag === 1 && <DownloadOption onClick={(event) => handleDownloadClick(event, invoice.invoiceId, 2)}>XML</DownloadOption>}
                                            </DownloadOptions>
                                        )}
                                    </DownloadContainer>
                                    <DeleteButton onClick={(event) => handleDeleteClick(event, invoice.invoiceId)}>Delete</DeleteButton>
                                </InvoiceItem>
                            ))}
                        </InvoiceList>
                    </Invoicecontainer>
                </Maincontainer>
            </Container>
        </div>
    );
}

export default Myinvoice;

const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 20px;
`;

const Mytext = styled.span`
    color: #6414FF;
`;

const Title = styled.h1`
    color: #ffffff;
    text-transform: uppercase;
    letter-spacing: 0.4rem;
    z-index: 1;
    margin-bottom: 20px;
    font-size: 2.5rem;
`;

const Maincontainer = styled.div`
    display: flex;
    position: relative;
    justify-content: flex-start;
    align-items: flex-start;
    flex-direction: column;
    height: 480px;
    width: 800px;
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    backdrop-filter: invert(20%);
    border-radius: 10px;
    color: #ffffff;
    z-index: 1;
`;

const Search = styled.div`
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    width: 100%;
`;

const SearchBox = styled.div`
    position: relative;
    margin: 20px;

    input {
        padding: 10px;
        width: 200px;
        border: none;
        border-radius: 20px;
        background: rgba(255, 255, 255, 0.2);
        box-shadow: 0 0 0 0.1rem #9a86d2;
        color: #ffffff;
        font-size: 1rem;
        outline: none;

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
`;

const SearchIcon = styled(FaSearch)`
    position: absolute;
    top: 10px;
    left: 165px;
    color: rgba(255, 255, 255, 0.7);
    font-size: 1.2rem;
    &:hover {
        cursor: pointer;
    }
`;

const DownloadContainer = styled.div`
    position: relative;
    display: inline-block;
    color: rgba(255, 255, 255, 0.7);
    font-size: 1.2rem;
    margin-right: 20px;
    &:hover {
        cursor: pointer;
    }
    &:hover > div {
        display: block;
    }
`;

const Download = styled(BiDownload)`
    color: rgba(255, 255, 255, 0.7);
    font-size: 1.2rem;
    &:hover {
        cursor: pointer;
    }
`;

const DownloadOptions = styled.div`
    display: block;
    position: absolute;
    background-color: rgba(0, 0, 0, 0.7);
    min-width: 100px;
    box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
    z-index: 1;
    top: 100%;
    left: 50%;
    transform: translateX(-50%);
    border-radius: 5px;
    padding: 5px 0;
`;

const DownloadOption = styled.div`
    color: rgba(255, 255, 255, 0.7);
    padding: 5px 10px;
    text-decoration: none;
    display: block;
    font-size: 1rem;
    &:hover {
        background-color: rgba(255, 255, 255, 0.2);
    }
`;

const Invoicecontainer = styled.div`
    position: relative;
    margin: 0 20px 20px 20px;
    height: 350px;
    width: 760px;
    backdrop-filter: blur(10px);
    overflow: hidden;
    overflow-y: scroll; 
    &::-webkit-scrollbar {
        width: 12px;
    }

    &::-webkit-scrollbar-track {
        background: rgba(255, 255, 255, 0.1); 
        border-radius: 10px;
    }

    &::-webkit-scrollbar-thumb {
        background: rgba(255, 255, 255, 0.15);
        border-radius: 10px;
        border: 3px solid rgba(255, 255, 255, 0.3);  
    }
`;

const InvoiceList = styled.ul`
    margin: 0;
    list-style-type: none;
    padding: 0;
    width: 100%;
`;

const InvoiceItem = styled.li`
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.2);
    &:hover {
        background: rgba(0, 0, 0, 0.3);
        cursor: pointer; /* Added to show pointer on hover */
    }
`;

const InvoiceName = styled.span`
    color: #ffffff;
    font-size: 1.1rem;
    flex: 1;
`;

const ValidationPass = styled.span`
    display: flex;
    align-items: center;
    color: #00ff80;
    font-size: 1rem;
    margin-right: 180px;
`;

const StatusCircleGreen = styled.div`
    width: 6px;
    height: 6px;
    background-color: #00ff00;
    border-radius: 50%;
    margin-right: 10px;
`;

const ValidationFail = styled.span`
    display: flex;
    align-items: center;
    color: #ff3333;
    font-size: 1rem;
    margin-right: 180px;
`;

const StatusCircleRed = styled.div`
    width: 6px;
    height: 6px;
    background-color: #ff0000;
    border-radius: 50%;
    margin-right: 10px;
`;

const DeleteButton = styled.button`
    background-color: transparent;
    border-radius: 20px;
    border: 1px solid #ff1a1a;
    color: rgba(255, 255, 255, 0.7);
    padding: 4px 15px;
    font-size: 1rem;
    cursor: pointer;
    outline: none;
    &:hover {
        background: rgba(255, 10, 40, 0.48);
        transition: all ease 0.5s;
    }
`;
