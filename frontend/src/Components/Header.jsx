import React from "react";
import { useNavigate } from "react-router-dom";
import video from "../images/video.mp4";

function Header() {
  const navigate = useNavigate();

  const handleStart = (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (token) {
      navigate("/my-invoice");
    } else {
      navigate("/sign-up");
    }
  };

  return (
    <div>
      <video autoPlay muted loop id="background-video">
        <source src={video} type="video/mp4" />
        Your browser does not support the video tag.
      </video>
      <div className="name">
        <h1>
          <span>Eazy</span> Invoice
        </h1>
        <p className="details">
          An efficient and secure e-invoice processing platform{" "}
        </p>
        <div className="header-btns">
          <a href="#" onClick={handleStart} className="header-btn">
            START
          </a>
        </div>
      </div>
    </div>
  );
}

export default Header;
