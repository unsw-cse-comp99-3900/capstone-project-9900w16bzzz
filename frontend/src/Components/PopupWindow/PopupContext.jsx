import React, { createContext, useState, useContext, useCallback } from "react";
import { useCookies } from 'react-cookie';

// Create a context for the Popup
const PopupContext = createContext();

/**
 * Provider component to manage and provide the popup state to its children.
 */
export const PopupProvider = ({ children }) => {
  const [cookies, setCookie, removeCookie] = useCookies(['x-access-token']);

  // State to manage the visibility, message, and type of the popup
  const [popupState, setPopupState] = useState({
    isVisible: false,
    message: "",
    type: "success",
  });

  /**
   * Function to show the popup with a specific message and type.
   * @param {string} message - The message to display in the popup.
   * @param {string} [type="success"] - The type of the popup, default is "success",only "error" and "success" two states.
   */
  const showPopup = useCallback((message, type = "success") => {
    setPopupState({ isVisible: true, message, type });

    const errorPattern = /Error:\s*(.*)/;
    const match = message.match(errorPattern);
    
    if (match && (
      match[1] === "Long time without operating system, need to log in again" || 
      match[1] === "You are not logged in or your login is not working, please log in again!"
    )) {
      localStorage.removeItem("username");
      localStorage.removeItem("token");
      removeCookie('x-access-token', { path: '/' });
      window.location.href = '/';
    }
  }, [removeCookie]);

  /**
   * Function to hide the popup.
   */
  const hidePopup = useCallback(() => {
    setPopupState((prev) => ({ ...prev, isVisible: false }));
  }, []);

  return (
    <PopupContext.Provider value={{ popupState, showPopup, hidePopup }}>
      {children}
    </PopupContext.Provider>
  );
};

/**
 * Custom hook to use the popup context.
 * @returns {object} The popup context value.
 */
export const usePopup = () => useContext(PopupContext);