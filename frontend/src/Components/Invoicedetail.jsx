import React, { useEffect, useState, useCallback } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import video from "../images/video1.mp4";
import styled from "styled-components";
import { BsBoxArrowInLeft } from "react-icons/bs";
import Preview from "./Preview";
import SendInvoice from "./Sendinvoice";

function useQuery() {
  return new URLSearchParams(useLocation().search);
}

function Invoicedetail() {
  const { invoiceId } = useParams();
  const query = useQuery();
  const [invoice, setInvoice] = useState(null);
  const [selectedFileType, setSelectedFileType] = useState(null);
  const navigate = useNavigate();
  const page = query.get("page") || 1;

  const getFileNameWithoutExtension = (fileName) => {
    return fileName.replace(/\.[^/.]+$/, "");
  };

  const fetchInvoiceDetail = useCallback(async () => {
    const userId = localStorage.getItem("userId");
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(
        `${process.env.REACT_APP_SERVER_URL}/invoice/list`,
        {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "x-access-token": `${token}`,
          },
          body: JSON.stringify({
            pageNum: page,
            pageSize: 10,
            searchCount: true,
            sortItemList: [
              {
                isAsc: true,
                column: "string",
              },
            ],
            userId: userId,
          }),
        }
      );
      const data = await response.json();
      console.log("Fetched invoice details:", data);

      if (data && data.data && data.data.list) {
        const foundInvoice = data.data.list.find(
          (inv) => inv.invoiceId.toString() === invoiceId
        );
        setInvoice(foundInvoice);
        if (foundInvoice.pdfFlag === 1) {
          setSelectedFileType("pdf");
        } else if (foundInvoice.jsonFlag === 1) {
          setSelectedFileType("json");
        } else if (foundInvoice.xmlFlag === 1) {
          setSelectedFileType("xml");
        }
      } else {
        console.error("Invalid API response structure:", data);
      }
    } catch (error) {
      console.error("Error fetching invoice details:", error);
    }
  }, [invoiceId, page]);

  useEffect(() => {
    fetchInvoiceDetail();
  }, [fetchInvoiceDetail]);

  const handleArrowClick = () => {
    navigate(`/my-invoice?page=${page}`);
  };

  const handleFileTypeChange = (event) => {
    setSelectedFileType(event.target.value);
  };

  const handleValidateClick = () => {
    navigate(`/validation/${invoiceId}`);
  };

  return (
    <div>
      <VideoBackground autoPlay muted loop id="background-video-signup">
        <source src={video} type="video/mp4" />
        Your browser does not support the video tag.
      </VideoBackground>
      <PageContainer>
        <Maincontainer>
          <ArrowIcon onClick={handleArrowClick} />
          {invoice ? (
            <>
              <InvoiceName>
                <TruncatedTitle>
                  {getFileNameWithoutExtension(invoice.fileName)}
                </TruncatedTitle>
                <FileTypeDropdown>
                  <select
                    onChange={handleFileTypeChange}
                    value={selectedFileType}
                  >
                    {invoice.pdfFlag === 1 && <option value="pdf">PDF</option>}
                    {invoice.jsonFlag === 1 && <option value="json">JSON</option>}
                    {invoice.xmlFlag === 1 && <option value="xml">XML</option>}
                  </select>
                </FileTypeDropdown>
                <Validate onClick={handleValidateClick}>Validate</Validate>
              </InvoiceName>
              <Preview
                selectedFileType={selectedFileType}
                invoiceId={invoiceId}
                validationFlag={invoice.validationFlag}
              />
              <SendInvoice
                invoiceId={invoiceId}
                selectedFileType={selectedFileType}
                fileName={getFileNameWithoutExtension(invoice.fileName)}
                validationFlag={invoice.validationFlag}
              />
            </>
          ) : (
            <LoadingMessage>Loading invoice details...</LoadingMessage>
          )}
        </Maincontainer>
      </PageContainer>
    </div>
  );
}

export default Invoicedetail;

const VideoBackground = styled.video`
  position: fixed;
  top: 0%;
  left: 0%;
  width: 90%;
  height: 100%;
  z-index: -1;
  transform: translate(-0%, -0%);
  object-fit: cover;
`;

const PageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  width: 100%;
`;

const Maincontainer = styled.div`
  display: flex;
  position: relative;
  justify-content: flex-start;
  align-items: flex-start;
  flex-direction: column;
  height: 76vh;
  width: 50vw;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: invert(20%);
  border-radius: 10px;
  color: #ffffff;
  z-index: 1;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    width: 90%;
    height: auto;
    padding: 20px;
  }
`;

const ArrowIcon = styled(BsBoxArrowInLeft)`
  position: relative;
  top: 20px;
  left: 29px;
  font-size: 2rem;
  &:hover {
    cursor: pointer;
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    top: 10px;
    left: 10px;
    font-size: 1.5rem;
  }
`;

const InvoiceName = styled.div`
  display: flex;
  margin-top: 20px;
  flex-direction: row;
  align-items: center;
  justify-content: space-between;
  height: 10vh;
  width: 100%;
  border-bottom: 3px solid rgba(255, 255, 255, 0.2);
  padding-right: 20px;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin-top: 20px;
    padding-right: 10px;
  }
`;

const TruncatedTitle = styled.h1`
  color: #ffffff;
  letter-spacing: 0.1rem;
  font-size: 1.5rem;
  padding-left: 80px;
  margin-right: 20px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 300px;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 1.2rem;
    padding-left: 17px;
    margin-right: 17px;
    max-width: 150px;
  }
`;

const FileTypeDropdown = styled.div`
  position: relative;
  margin-left: 20px;
  z-index: 1;
  select {
    background: rgba(0, 0, 0, 0.3);
    border: none;
    padding: 10px;
    border-radius: 15px;
    color: #ffffff;
    font-size: 1.1rem;
    &:focus {
      outline: none;
    }
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin-left: 40px;
    height: 40px;
  }
`;

const Validate = styled.button`
  background-color: #5011cc;
  position: relative;
  color: white;
  height: 38px;
  padding: 10px 20px;
  border: 2px solid #6414ff;
  border-radius: 20px;
  cursor: pointer;
  letter-spacing: 0.1rem;
  font-size: 1rem;
  text-transform: uppercase;
  margin-left: auto;
  &:hover {
    background-color: transparent;
    transition: all ease 0.5s;
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    height: 40px;
    padding: 6px 5px;
    font-size: 0.8rem;
  }
`;

const LoadingMessage = styled.div`
  color: #ffffff;
  font-size: 1.5rem;
  text-align: center;
  margin-top: 50px;
  margin-left: 50px;
`;
