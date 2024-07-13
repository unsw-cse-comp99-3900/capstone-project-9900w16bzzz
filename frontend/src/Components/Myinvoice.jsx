import React, { useEffect, useState } from "react";
import video from "../images/video1.mp4";
import styled from "styled-components";
import { FaSearch } from "react-icons/fa";

function Myinvoice(){
    const [invoiceData, setInvoiceData] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");

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
            console.log('API Response:', data); // Log the response for debugging

            if (data && data.data && data.data.list) {
                setInvoiceData(data.data.list);
            } else {
                console.error("Invalid API response structure:", data);
            }
        } catch (error) {
            console.error("Error fetching invoices:", error);
        }
    };

    const filteredInvoices = invoiceData.filter((invoice) =>
        invoice.fileName.toLowerCase().includes(searchTerm.toLowerCase())
    );
    return (
        <div id= "main">
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
                            {invoiceData.map((invoice) => (
                                <InvoiceItem key={invoice.invoiceId}>
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
                                    <DeleteButton>Delete</DeleteButton>
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
    height: 450px;
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
        color: #ffffff;
        font-size: 1rem;
        outline: none;

        &::placeholder {
            color: rgba(255, 255, 255, 0.7);
        }
    }
`;

const SearchIcon = styled(FaSearch)`
    position: absolute;
    top: 10px;
    left: 165px;
    color: rgba(255, 255, 255, 0.7);
    font-size: 1.2rem;
    &:hover{
        cursor: pointer;
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
    &:hover{
        background: rgba(0, 0, 0, 0.3);
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
    margin-right: 200px;
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
    margin-right: 200px;
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
    background-color: transparent;
    padding: 4px 15px;
    font-size: 1rem;
    cursor: pointer;
    outline: none;
    &:hover{
        background: rgba(255, 10, 40, 0.48);
    }
`;


