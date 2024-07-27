import React from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { usePopup } from "../PopupWindow/PopupContext";

const ProtectedLink = ({ to, children }) => {
  const { showPopup } = usePopup();
  const navigate = useNavigate();

  const handleClick = (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (token) {
      navigate(to);
    } else {
      showPopup("Please log in before access to this page.", "error");
      navigate("/log-in");
    }
  };

  return (
    <RouterLink to={to} onClick={handleClick}>
      {children}
    </RouterLink>
  );
};

export default ProtectedLink;
