import React from "react";
import { Link as RouterLink, useNavigate } from "react-router-dom";
import { usePopup } from "../../PopupWindow/PopupContext";

/**
 * ProtectedLink component to create a protected link that checks for authentication.
 * @param {string} to - The path to navigate to.
 * @param {ReactNode} children - The child components to render inside the link.
 */
const ProtectedLink = ({ to, children }) => {
  const { showPopup } = usePopup();
  const navigate = useNavigate();

  /**
   * Handler for link click event.
   * @param {Event} e - The click event.
   */
  const handleClick = (e) => {
    e.preventDefault();
    const token = localStorage.getItem("token");
    if (token) {
      navigate(to); // Navigate to the target path if authenticated
    } else {
      showPopup("Please log in before access to this page.", "error");
      navigate("/log-in"); // Redirect to login page if not authenticated
    }
  };

  return (
    <RouterLink to={to} onClick={handleClick}>
      {children}
    </RouterLink>
  );
};

export default ProtectedLink;
