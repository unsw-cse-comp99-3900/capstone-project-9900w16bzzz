import React from "react";

const deleteInvoice = async (invoiceId, setInvoiceData, invoiceData, showPopup) => {
  const token = localStorage.getItem("token");
  try {
    const response = await fetch(
      `${process.env.REACT_APP_SERVER_URL}/invoice/delete`,
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "x-access-token": `${token}`,
        },
        body: JSON.stringify({ invoiceId }),
      }
    );
    const data = await response.json();
    console.log("Delete Response:", data);

    if (data.ok) {
      setInvoiceData(
        invoiceData.filter((invoice) => invoice.invoiceId !== invoiceId)
      );
      showPopup("Delete successful!", "success");
    } else {
      console.error("Failed to delete invoice:", data);
      showPopup("Failed to delete invoice!", "error");
    }
  } catch (error) {
    console.error("Error deleting invoice:", error);
    showPopup("Error deleting invoice!", "error");
  }
};

export default deleteInvoice;
