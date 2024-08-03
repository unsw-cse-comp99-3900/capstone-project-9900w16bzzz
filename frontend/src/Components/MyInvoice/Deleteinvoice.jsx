import React from "react";

/**
 * Deletes an invoice by ID.
 *
 * This function sends a POST request to the server to delete an invoice with the given ID.
 * It updates the invoice data in the state and displays a popup message based on the success or failure of the deletion.
 *
 * @param {string} invoiceId - The ID of the invoice to delete.
 * @param {Function} setInvoiceData - Function to update the invoice data state.
 * @param {Array} invoiceData - Array of current invoice data.
 * @param {Function} showPopup - Function to show a popup message.
 */
const deleteInvoice = async (invoiceId, setInvoiceData, invoiceData, showPopup) => {
  // Retrieve the authentication token from local storage
  const token = localStorage.getItem("token");

  try {
    // Send a POST request to the server to delete the invoice
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

    // Parse the response JSON data
    const data = await response.json();
    console.log("Delete Response:", data);

    // Check if the deletion was successful
    if (data.ok) {
      // Update the invoice data state by filtering out the deleted invoice
      setInvoiceData(
        invoiceData.filter((invoice) => invoice.invoiceId !== invoiceId)
      );
      // Show a success popup message
      showPopup("Delete successful!", "success");
    } else {
      console.error("Failed to delete invoice:", data);
      // Show an error popup message
      showPopup("Failed to delete invoice!", "error");
    }
  } catch (error) {
    console.error("Error deleting invoice:", error);
    // Show an error popup message for exceptions
    showPopup("Error deleting invoice!", "error");
  }
};

export default deleteInvoice;
