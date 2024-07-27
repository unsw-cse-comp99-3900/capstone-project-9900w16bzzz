import React from "react";

const DownloadInvoice = async (invoiceId, fileType) => {
  const token = localStorage.getItem("token");
  try {
    const response = await fetch(
      `${process.env.REACT_APP_SERVER_URL}/invoice/download?invoiceId=${invoiceId}&fileType=${fileType}`,
      {
        method: "GET",
        headers: {
          "x-access-token": `${token}`,
        },
      }
    );

    if (response.ok) {
      const blob = await response.blob();
      const url = window.URL.createObjectURL(blob);

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

      const a = document.createElement("a");
      a.href = url;
      a.download = filename;
      a.click();
      window.URL.revokeObjectURL(url);
    } else {
      console.error("Failed to download file");
    }
  } catch (error) {
    console.error("Error downloading file:", error);
  }
};

export default DownloadInvoice;
