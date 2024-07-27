import React, { createContext, useState, useContext, useCallback } from "react";

const PopupContext = createContext();

export const PopupProvider = ({ children }) => {
  const [popupState, setPopupState] = useState({
    isVisible: false,
    message: "",
    type: "success",
  });

  const showPopup = useCallback((message, type = "success") => {
    setPopupState({ isVisible: true, message, type });
  }, []);

  const hidePopup = useCallback(() => {
    setPopupState((prev) => ({ ...prev, isVisible: false }));
  }, []);

  return (
    <PopupContext.Provider value={{ popupState, showPopup, hidePopup }}>
      {children}
    </PopupContext.Provider>
  );
};

export const usePopup = () => useContext(PopupContext);
