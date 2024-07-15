import React from 'react';

const DownloadInvoice = async (invoiceId, fileType) => {
    const token = localStorage.getItem('token');
    try {
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/invoice/download?invoiceId=${invoiceId}&fileType=${fileType}`, {
            method: "GET",
            headers: {
                'x-access-token': `${token}`
            }
        });

        if (response.ok) {
            const blob = await response.blob();
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = `${invoiceId}.${fileType === 3 ? 'pdf' : fileType === 1 ? 'json' : 'xml'}`;
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
