import React from "react";
import video from "../images/video.mp4";

function Header(){
    return(
        <div id = "main">
            <video autoPlay muted loop id="background-video">
                <source src={video} type="video/mp4" />
                Your browser does not support the video tag.
            </video>
            <div className = "name">
                <h1><span>Eazy</span> Invoice</h1>
                <p className="details">An efficient and secure e-invoice processing platform </p>
                <div className="header-btns">
                    <a href="#start" className="header-btn">START</a>
                </div>
            </div>
        </div>
    )
}
export default Header;