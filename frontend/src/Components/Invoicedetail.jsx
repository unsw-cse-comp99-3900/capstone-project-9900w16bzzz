import React from "react";
import video from "../images/video1.mp4";
import styled from "styled-components";
import {BsBoxArrowInLeft} from "react-icons/bs";



function Invoicedetail(){
    return (
        <div id = "main">
            <video autoPlay muted loop id="background-video-signup">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
            </video>
            <Maincontainer>
                <ArrowIcon/>
            </Maincontainer>
        </div>
    )
}

export default Invoicedetail;

const Maincontainer = styled.div`
    display: flex;
    position: relative;
    justify-content: flex-start;
    align-items: flex-start;
    flex-direction: column;
    height: 450px;
    width: 600px;
    box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
    backdrop-filter: invert(20%);
    border-radius: 10px;
    color: #ffffff;
    z-index: 1;
`;

const ArrowIcon = styled(BsBoxArrowInLeft)`
    position: absolute;
    top: 20px;
    left: 29px;
    font-size: 2rem;
    &:hover{
        cursor: pointer;
    }
`;