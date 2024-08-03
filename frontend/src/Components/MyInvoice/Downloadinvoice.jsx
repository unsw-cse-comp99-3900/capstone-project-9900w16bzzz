import React from "react";

/**
 * Downloads an invoice by ID and file type.
 *
 * This function sends a GET request to the server to download an invoice with the given ID and file type.
 * It handles the file download process, including creating a download link and managing file naming.
 *
 * @param {string} invoiceId - The ID of the invoice to download.
 * @param {number} fileType - The type of file to download (1 for JSON, 2 for XML, 3 for PDF).
 */
const DownloadInvoice = async (invoiceId, fileType) => {
  // Retrieve the authentication token from local storage
  const token = localStorage.getItem("token");

  try {
    // Send a GET request to the server to download the invoice
    const response = await fetch(
      `${process.env.REACT_APP_SERVER_URL}/invoice/download?invoiceId=${invoiceId}&fileType=${fileType}`,
      {
        method: "GET",
        headers: {
          "x-access-token": `${token}`,
        },
      }
    );

    // Check if the request was successful
    if (response.ok) {
      // Create a blob from the response
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);

      // Extract the filename from the Content-Disposition header if available
      const contentDisposition = response.headers.get("Content-Disposition");
      let filename = `${invoiceId}.${
        fileType === 3 ? "pdf" : fileType === 1 ? "json" : "xml"
      }`;

      if (contentDisposition && contentDisposition.includes("attachment")) {
        const filenameMatch = contentDisposition.match(
          /filename\*?=['"]?([^;]*)['"]?/
        );
        if (filenameMatch.length > 1) {
          filename = decodeURIComponent(
            filenameMatch[1].replace(/UTF-8''/, "")
          );
        }
      }

      // Create a download link and trigger the download
      const a = document.createElement("a");
      a.href = url;
      a.download = filename;
      a.click();

      // Revoke the object URL to free up memory
      window.URL.revokeObjectURL(url);
    } else {
      console.error("Failed to download file");
    }
  } catch (error) {
    console.error("Error downloading file:", error);
  }
};

export default DownloadInvoice;
