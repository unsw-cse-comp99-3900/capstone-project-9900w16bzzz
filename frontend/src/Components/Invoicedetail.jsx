import React from "react";
import video from "../images/video1.mp4";
import styled from "styled-components";
import { BsBoxArrowInLeft } from "react-icons/bs";
import { useNavigate } from "react-router-dom";

function Invoicedetail() {
    const navigate = useNavigate();

    const handleArrowClick = () => {
        navigate("/my-invoice");
    };

    return (
        <div id="main">
            <video autoPlay muted loop id="background-video-signup">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
            </video>
            <Maincontainer>
                <ArrowIcon onClick={handleArrowClick} />
                <InvoiceName>
                    <Title>Invoice 1</Title>
                    <FileTypeDropdown>
                        <select>
                            <option value="pdf">PDF</option>
                            <option value="json">JSON</option>
                            <option value="xml">XML</option>
                        </select>
                    </FileTypeDropdown>
                    <Validate>Validate</Validate>
                </InvoiceName>
                <PreviewBox>

                </PreviewBox>
                <EmailBox>
                    <EmailInput>
                    <input
                                type="text"
                                placeholder="Email address"
                            />
                    </EmailInput>
                    <EmailButton>Send</EmailButton>
                </EmailBox>
            </Maincontainer>
        </div>
    );
}

export default Invoicedetail;

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
`;


const ArrowIcon = styled(BsBoxArrowInLeft)`
    position: relative;
    top: 20px;
    left: 29px;
    font-size: 2rem;
    &:hover {
        cursor: pointer;
    }
`;

const InvoiceName = styled.div`
    display: flex;
    margin-top: 20px;
    flex-direction: row;
    align-items: center;
    height:60px;
    width:100%;
    border-bottom: 3px solid rgba(255, 255, 255, 0.2);
    padding-right: 20px;
`;

const Title = styled.h1`
    color: #ffffff;
    text-transform: uppercase;
    letter-spacing: 0.1rem;
    font-size: 1.5rem;
    padding-left: 80px;
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
    font-weight: bold;
    letter-spacing: 0.1rem;
    font-size:1rem;
    margin-left: 150px;
    text-transform: uppercase;
    &:hover {
        background-color: transparent;
        transition: all ease 0.5s;
    }
`;

const PreviewBox = styled.div`
    position: relative;
    margin-top: 20px;
    margin-left: 30px;
    margin-right: 30px;
    height:240px;
    width:90%;
    background: rgba(0, 0, 0, 0.3);
    border-radius: 20px;
`;

const EmailBox = styled.div`
    display: flex;
    justify-content: flex-end;
    align-items: center;
    margin-top: 20px;
    height: 100px;
    margin-left: 30px;
    margin-right: 30px;
    width: 90%;
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

        &::placeholder {
            color: rgba(255, 255, 255, 0.7);
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
    font-weight: bold;
    letter-spacing: 0.1rem;
    font-size:1rem;
    margin-left: 20px;
    text-transform: uppercase;
    &:hover {
        background-color: transparent;
        transition: all ease 0.5s;
    }
`;