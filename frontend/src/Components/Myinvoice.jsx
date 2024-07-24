import React, { useEffect, useState, useCallback } from "react";
import video from "../images/video1.mp4";
import styled from "styled-components";
import { FaSearch } from "react-icons/fa";
import { BiDownload } from "react-icons/bi";
import { useNavigate } from "react-router-dom";
import deleteInvoice from "./Deleteinvoice";
import DownloadInvoice from "./Downloadinvoice";
import { usePopup } from './PopupWindow/PopupContext';

function Myinvoice() {
    const [invoiceData, setInvoiceData] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [openDropdown, setOpenDropdown] = useState(null);
    const [currentPage, setCurrentPage] = useState(1);
    const [totalPages, setTotalPages] = useState(1);
    const [pageSize] = useState(10);
    const navigate = useNavigate();
    const { showPopup } = usePopup();

    const fetchInvoices = useCallback(async (page) => {
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
                    pageNum: page,
                    pageSize: pageSize,
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

            if (data && data.data) {
                setInvoiceData(data.data.list);
                setTotalPages(data.data.pages);
                setCurrentPage(data.data.pageNum);
            } else {
                console.error("Invalid API response structure:", data);
            }
        } catch (error) {
            console.error("Error fetching invoices:", error);
        }
    }, [pageSize]);

    useEffect(() => {
        fetchInvoices(currentPage);
    }, [fetchInvoices, currentPage]);

    const handleInvoiceClick = (invoiceId) => {
        navigate(`/invoice/${invoiceId}`);
    };

    const handleDeleteClick = async (event, invoiceId) => {
        event.stopPropagation();
        await deleteInvoice(invoiceId, setInvoiceData, invoiceData);
        fetchInvoices(currentPage);
    };

    const handleDropdownToggle = (event, invoiceId) => {
        event.stopPropagation();
        setOpenDropdown(openDropdown === invoiceId ? null : invoiceId);
    };

    const handleDownloadClick = async (event, invoiceId, fileType) => {
        event.stopPropagation();
        event.preventDefault();
        try {
            await DownloadInvoice(invoiceId, fileType);
            showPopup('Download successful!', 'success');
        } catch (error) {
            showPopup('Download failed!', 'error');
        }
    };

    const getFileNameWithoutExtension = (fileName) => {
        return fileName.replace(/\.[^/.]+$/, "");
    };

    const filteredInvoices = invoiceData.filter((invoice) =>
        invoice.fileName.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <div id="main">
            <VideoBackground autoPlay muted loop id="background-video-signup">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
            </VideoBackground>
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
                                    <InvoiceNameContainer>
                                        <InvoiceName>{getFileNameWithoutExtension(invoice.fileName)}</InvoiceName>
                                    </InvoiceNameContainer>
                                    {invoice.validationFlag === 1 && (
                                        <ValidationContainer>
                                            <ValidationPass>
                                                <StatusCircleGreen />
                                                Validation Pass
                                            </ValidationPass>
                                        </ValidationContainer>
                                    )}
                                    {invoice.validationFlag === 2 && (
                                        <ValidationContainer>
                                            <ValidationFail>
                                                <StatusCircleRed />
                                                Validation Fail
                                            </ValidationFail>
                                        </ValidationContainer>
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
                        <PaginationControls>
                            <PageButton onClick={() => fetchInvoices(1)} disabled={currentPage === 1}>First</PageButton>
                            <PageButton onClick={() => fetchInvoices(currentPage - 1)} disabled={currentPage === 1}>Prev</PageButton>
                            <span>{currentPage} of {totalPages}</span>
                            <PageButton onClick={() => fetchInvoices(currentPage + 1)} disabled={currentPage === totalPages}>Next</PageButton>
                            <PageButton onClick={() => fetchInvoices(totalPages)} disabled={currentPage === totalPages}>Last</PageButton>
                        </PaginationControls>
                    </Invoicecontainer>
                </Maincontainer>
            </Container>
        </div>
    );
}

export default Myinvoice;

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


const Container = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 20px;

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin-top: 200px; 
    }

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

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    width: 100%;  
    height: 10%; 
    padding: 1px;  
    }

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

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    input {
      width: 140px; 
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

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    left: 110px; 
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

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    width: 93%;
    height: 110%;
    transform: translateX(px); 
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
        cursor: pointer;
    }
`;

const InvoiceNameContainer = styled.div`
  flex: 1;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
`;

const InvoiceName = styled.span`
  color: #ffffff;
  font-size: 1.1rem;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 1.0rem; 
    margin-right: 5px;
    }

`;

const ValidationContainer = styled.div`
  display: flex;
  align-items: center;
  margin-right: 170px;
  width: 130px;

  @media (max-width: 430px) {
    margin-right: 60px;
    width: 60px; 
  }
`;

const ValidationPass = styled.span`
    display: flex;
    align-items: center;
    color: #00ff80;
    font-size: 1rem;

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 0.8rem; 
    margin-right: 30px;
    margin-left: 5px;
        }

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
    color: #ff0000;
    font-size: 1rem;

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 0.8rem;
    margin-right: 30px; 
    margin-left: 5px; 
        }

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

const PaginationControls = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
    padding: 10px 0; 
`;

const PageButton = styled.button`
    background-color: ${props => props.disabled ? 'rgba(100, 20, 255, 0.5)' : 'transparent'};
    color: #ffffff;
    border: 1px solid #6414FF;
    padding: 8px 16px; 
    margin: 0 8px; 
    cursor: ${props => props.disabled ? 'default' : 'pointer'};
    border-radius: 20px;
    font-size: 1rem; 
    min-width: 40px;
    &:hover {
        background-color: ${props => props.disabled ? 'rgba(100, 20, 255, 0.5)' : '#6414FF'};
    }
`;