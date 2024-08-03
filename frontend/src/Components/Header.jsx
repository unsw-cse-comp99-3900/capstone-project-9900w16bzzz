import React from "react";
import { useNavigate } from "react-router-dom";
import video from "../images/video.mp4";

/**
 * Home component for the landing page.
 *
 * This component renders a video background and a main section with a title, description,
 * and a start button that navigates to different routes based on the user's authentication status.
 */
function Home() {
  const navigate = useNavigate();

  /**
   * Handles the click event for the start button.
   *
   * This function checks if the user is authenticated by checking the token in local storage.
   * If authenticated, it navigates to the 'My Invoice' page; otherwise, it navigates to the 'Sign Up' page.
   *
   * @param {Event} e - The click event.
   */
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
      {/* Background video */}
      <video autoPlay muted loop id="background-video">
        <source src={video} type="video/mp4" />
        Your browser does not support the video tag.
      </video>
      <div className="name">
        <h1>
          <span>Eazy</span> Invoice
        </h1>
        <p className="details">
          An efficient and secure e-invoice processing platform
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

export default Home;
