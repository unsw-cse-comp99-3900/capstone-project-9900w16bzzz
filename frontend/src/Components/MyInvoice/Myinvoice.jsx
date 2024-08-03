import React, { useEffect, useState, useCallback } from "react";
import video from "../../images/video1.mp4";
import styled from "styled-components";
import { FaSearch } from "react-icons/fa";
import { BiDownload } from "react-icons/bi";
import { useNavigate, useLocation } from "react-router-dom";
import deleteInvoice from "./Deleteinvoice";
import DownloadInvoice from "./Downloadinvoice";
import { usePopup } from "../PopupWindow/PopupContext";

/**
 * Custom hook to parse URL query parameters.
 *
 * @returns {URLSearchParams} The URL search parameters.
 */
function useQuery() {
  return new URLSearchParams(useLocation().search);
}

/**
 * MyInvoice component for managing and displaying user invoices.
 *
 * This component allows users to view, search, download, and delete their invoices.
 * It also provides pagination controls and handles different file types and validation statuses.
 */
function Myinvoice() {
  const query = useQuery();
  const navigate = useNavigate();
  const { showPopup } = usePopup();

  const [invoiceData, setInvoiceData] = useState([]);
  const [searchTerm, setSearchTerm] = useState("");
  const [openDropdown, setOpenDropdown] = useState(null);
  const [currentPage, setCurrentPage] = useState(parseInt(query.get("page") || 1));
  const [totalPages, setTotalPages] = useState(1);
  const [pageSize] = useState(10);
  const [windowWidth, setWindowWidth] = useState(window.innerWidth);

  /**
   * Fetches a list of invoices from the server.
   *
   * @param {number} page - The current page number.
   */
  const fetchInvoices = useCallback(
    async (page) => {
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
              pageSize: pageSize,
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
        console.log("API Response:", data);

        if (data && data.data) {
          setInvoiceData(data.data.list);
          setTotalPages(data.data.pages);
          setCurrentPage(data.data.pageNum);
        } else {
          console.error("Invalid API response structure:", data);
          showPopup(`Failed to fetch invoices. Errors: ${data.msg}`, "error");
        }
      } catch (error) {
        console.error("Error fetching invoices:", error);
      }
    },
    [pageSize, showPopup]
  );

  /**
   * Searches for invoices by name.
   *
   * @param {number} page - The current page number.
   */
  const searchInvoices = useCallback(
    async (page) => {
      const userId = localStorage.getItem("userId");
      const token = localStorage.getItem("token");
      try {
        const response = await fetch(
          `${process.env.REACT_APP_SERVER_URL}/invoice/searchByName`,
          {
            method: "POST",
            headers: {
              "Content-Type": "application/json",
              "x-access-token": `${token}`,
            },
            body: JSON.stringify({
              pageNum: page,
              pageSize: pageSize,
              searchCount: true,
              sortItemList: [
                {
                  isAsc: true,
                  column: "string",
                },
              ],
              fileName: searchTerm,
              userId: userId,
            }),
          }
        );
        const data = await response.json();
        console.log("API Response:", data);

        if (data && data.data) {
          setInvoiceData(data.data.list);
          setTotalPages(data.data.pages);
          setCurrentPage(data.data.pageNum);
        } else {
          console.error("Invalid API response structure:", data);
        }
      } catch (error) {
        console.error("Error fetching invoices:", error);
      }
    },
    [pageSize, searchTerm]
  );

  useEffect(() => {
    if (searchTerm) {
      searchInvoices(currentPage);
    } else {
      fetchInvoices(currentPage);
    }
  }, [fetchInvoices, searchInvoices, currentPage, searchTerm]);

  useEffect(() => {
    const handleResize = () => {
      setWindowWidth(window.innerWidth);
    };
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  /**
   * Handles the click event for an invoice item.
   *
   * @param {string} invoiceId - The ID of the invoice.
   */
  const handleInvoiceClick = (invoiceId) => {
    navigate(`/invoice/${invoiceId}?page=${currentPage}`);
  };

  /**
   * Handles the delete button click event for an invoice.
   *
   * @param {Event} event - The click event.
   * @param {string} invoiceId - The ID of the invoice.
   */
  const handleDeleteClick = async (event, invoiceId) => {
    event.stopPropagation();
    await deleteInvoice(invoiceId, setInvoiceData, invoiceData, showPopup);
    fetchInvoices(currentPage);
  };

  /**
   * Toggles the dropdown for more options on an invoice item.
   *
   * @param {Event} event - The click event.
   * @param {string} invoiceId - The ID of the invoice.
   */
  const handleDropdownToggle = (event, invoiceId) => {
    event.stopPropagation();
    setOpenDropdown(openDropdown === invoiceId ? null : invoiceId);
  };

  /**
   * Handles the download button click event for an invoice.
   *
   * @param {Event} event - The click event.
   * @param {string} invoiceId - The ID of the invoice.
   * @param {number} fileType - The type of file to download (1 for JSON, 2 for XML, 3 for PDF).
   */
  const handleDownloadClick = async (event, invoiceId, fileType) => {
    event.stopPropagation();
    event.preventDefault();
    try {
      await DownloadInvoice(invoiceId, fileType);
      showPopup("Download successful!", "success");
    } catch (error) {
      showPopup("Download failed!", "error");
    }
  };

  /**
   * Utility function to remove the file extension from a filename.
   *
   * @param {string} fileName - The filename with extension.
   * @returns {string} The filename without extension.
   */
  const getFileNameWithoutExtension = (fileName) => {
    return fileName.replace(/\.[^/.]+$/, "");
  };

  /**
   * Truncates text to a specified maximum length, adding ellipsis if needed.
   *
   * @param {string} text - The text to truncate.
   * @param {number} maxLength - The maximum length of the truncated text.
   * @returns {string} The truncated text.
   */
  const truncateText = (text, maxLength) => {
    if (text.length > maxLength) {
      return text.substring(0, maxLength - 1) + "...";
    }
    return text;
  };

  /**
   * Checks if the device is an iPhone 14 Pro Max.
   *
   * @returns {boolean} True if the device is an iPhone 14 Pro Max, false otherwise.
   */
  const isIphone14ProMax = () => {
    const width = window.innerWidth;
    const height = window.innerHeight;
    const pixelRatio = window.devicePixelRatio;
    return width === 430 && height === 932 && pixelRatio === 3;
  };

  /**
   * Handles the click event for the more options button on an invoice item.
   *
   * @param {Event} event - The click event.
   * @param {string} invoiceId - The ID of the invoice.
   */
  const handleMoreOptionsClick = (event, invoiceId) => {
    event.stopPropagation();
    setOpenDropdown(openDropdown === invoiceId ? null : invoiceId);
  };

  /**
   * Component for rendering pagination controls.
   */
  const PaginationControls = () => (
    <PaginationContainer>
      <PageButton
        onClick={() => setCurrentPage(1)}
        disabled={currentPage === 1}
      >
        First
      </PageButton>
      <PageButton
        onClick={() => setCurrentPage(currentPage - 1)}
        disabled={currentPage === 1}
      >
        Prev
      </PageButton>
      <span>
        {currentPage} of {totalPages}
      </span>
      <PageButton
        onClick={() => setCurrentPage(currentPage + 1)}
        disabled={currentPage === totalPages}
      >
        Next
      </PageButton>
      <PageButton
        onClick={() => setCurrentPage(totalPages)}
        disabled={currentPage === totalPages}
      >
        Last
      </PageButton>
    </PaginationContainer>
  );

  return (
    <div>
      {/* Background video */}
      <VideoBackground autoPlay muted loop id="background-video-signup">
        <source src={video} type="video/mp4" />
        Your browser does not support the video tag.
      </VideoBackground>
      <PageContainer>
        <Container>
          <Title>
            <Mytext>My </Mytext>Invoice
          </Title>
          <Maincontainer>
            <Search>
              <SearchBox>
                <input
                  type="text"
                  placeholder="Search..."
                  value={searchTerm}
                  onChange={(e) => setSearchTerm(e.target.value)}
                  onKeyDown={(e) => {
                    if (e.key === "Enter") {
                      setCurrentPage(1);
                      searchInvoices(1);
                    }
                  }}
                />
              </SearchBox>
            </Search>
            <Invoicecontainer>
              <InvoiceList>
                {invoiceData.map((invoice) => (
                  <InvoiceItem
                    key={invoice.invoiceId}
                    onClick={() => handleInvoiceClick(invoice.invoiceId)}
                  >
                    <InvoiceNameContainer>
                      <InvoiceName>
                        {getFileNameWithoutExtension(invoice.fileName)}
                      </InvoiceName>
                      <UpdateDate>
                        Update: {new Date(invoice.updateTime).toLocaleDateString()}
                      </UpdateDate>
                    </InvoiceNameContainer>
                    {invoice.validationFlag === 1 && (
                      <ValidationContainer>
                        <ValidationPass>
                          <StatusCircleGreen />
                          Validation Passed
                        </ValidationPass>
                      </ValidationContainer>
                    )}
                    {invoice.validationFlag === 2 && (
                      <ValidationContainer>
                        <ValidationFail>
                          <StatusCircleRed />
                          Validation Failed
                        </ValidationFail>
                      </ValidationContainer>
                    )}
                    {invoice.validationFlag === 0 && (
                      <ValidationContainer>
                        <NotValidated>
                          <StatusCircleGray />
                          Not Validated
                        </NotValidated>
                      </ValidationContainer>
                    )}
                    {isIphone14ProMax() ? (
                      <MoreOptionsContainer>
                        <MoreOptions
                          onClick={(event) =>
                            handleMoreOptionsClick(event, invoice.invoiceId)
                          }
                        >
                          ...
                        </MoreOptions>
                        {openDropdown === invoice.invoiceId && (
                          <MoreOptionsDropdown>
                            {invoice.pdfFlag === 1 && (
                              <DownloadOption
                                onClick={(event) =>
                                  handleDownloadClick(event, invoice.invoiceId, 3)
                                }
                              >
                                PDF
                                <BiDownload style={{ marginLeft: "8px" }} />
                              </DownloadOption>
                            )}
                            {invoice.jsonFlag === 1 && (
                              <DownloadOption
                                onClick={(event) =>
                                  handleDownloadClick(event, invoice.invoiceId, 1)
                                }
                              >
                                JSON
                                <BiDownload style={{ marginLeft: "8px" }} />
                              </DownloadOption>
                            )}
                            {invoice.xmlFlag === 1 && (
                              <DownloadOption
                                onClick={(event) =>
                                  handleDownloadClick(event, invoice.invoiceId, 2)
                                }
                              >
                                XML
                                <BiDownload style={{ marginLeft: "8px" }} />
                              </DownloadOption>
                            )}
                            <DropdownOption
                              onClick={(event) =>
                                handleDeleteClick(event, invoice.invoiceId)
                              }
                            >
                              Delete
                            </DropdownOption>
                          </MoreOptionsDropdown>
                        )}
                      </MoreOptionsContainer>
                    ) : (
                      <>
                        <DownloadContainer>
                          <Download
                            onClick={(event) =>
                              handleDropdownToggle(event, invoice.invoiceId)
                            }
                          />
                          {openDropdown === invoice.invoiceId && (
                            <DownloadOptions>
                              {invoice.pdfFlag === 1 &&
                                invoice.validationFlag === 0 && (
                                  <DownloadOption
                                    onClick={(event) =>
                                      handleDownloadClick(
                                        event,
                                        invoice.invoiceId,
                                        3
                                      )
                                    }
                                  >
                                    PDF
                                  </DownloadOption>
                                )}
                              {invoice.jsonFlag === 1 && (
                                <DownloadOption
                                  onClick={(event) =>
                                    handleDownloadClick(
                                      event,
                                      invoice.invoiceId,
                                      1
                                    )
                                  }
                                >
                                  JSON
                                </DownloadOption>
                              )}
                              {invoice.xmlFlag === 1 && (
                                <DownloadOption
                                  onClick={(event) =>
                                    handleDownloadClick(
                                      event,
                                      invoice.invoiceId,
                                      2
                                    )
                                  }
                                >
                                  XML
                                </DownloadOption>
                              )}
                            </DownloadOptions>
                          )}
                        </DownloadContainer>
                        <DeleteButton
                          onClick={(event) =>
                            handleDeleteClick(event, invoice.invoiceId)
                          }
                        >
                          Delete
                        </DeleteButton>
                      </>
                    )}
                  </InvoiceItem>
                ))}
              </InvoiceList>
              <PaginationControls />
            </Invoicecontainer>
          </Maincontainer>
        </Container>
      </PageContainer>
    </div>
  );
}

export default Myinvoice;

// Styled components for the MyInvoice component

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

const Container = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 20px;
`;

const Mytext = styled.span`
  color: #6414ff;
`;

const Title = styled.h1`
  color: #ffffff;
  text-transform: uppercase;
  letter-spacing: 0.4rem;
  z-index: 1;
  margin-bottom: 20px;
  font-size: 2.5rem;
`;

const Maincontainer = styled.div`
  display: flex;
  position: relative;
  justify-content: flex-start;
  align-items: flex-start;
  flex-direction: column;
  height: 80%;
  width: 100%;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: invert(20%);
  border-radius: 10px;
  color: #ffffff;
  z-index: 1;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    width: 410px;
    height: 680px;
    padding: 1px;
  }
`;

const Search = styled.div`
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  width: 100%;
`;

const MoreOptions = styled.span`
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.5rem;
  cursor: pointer;
  &:hover {
    color: rgba(255, 255, 255, 1);
  }
`;

const MoreOptionsDropdown = styled.div`
  position: absolute;
  background-color: rgba(0, 0, 0, 0.7);
  min-width: 100px;
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
  z-index: 1;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  border-radius: 5px;
  padding: 5px 0;
`;

const DropdownOption = styled.div`
  color: rgba(255, 255, 255, 0.7);
  padding: 5px 10px;
  text-decoration: none;
  display: block;
  font-size: 1rem;
  cursor: pointer;
  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }
`;

const MoreOptionsContainer = styled.div`
  position: relative;
  display: inline-block;
  top: -8px;
  left: -18px;
`;

const SearchBox = styled.div`
  position: relative;
  margin: 20px;

  input {
    padding: 10px;
    width: 16vw;
    border: none;
    border-radius: 20px;
    background: rgba(255, 255, 255, 0.2);
    box-shadow: 0 0 0 0.1rem #9a86d2;
    color: #ffffff;
    font-size: 1rem;
    outline: none;

    &::placeholder {
      color: rgba(255, 255, 255, 0.7);
    }
    &:focus {
      display: inline-block;
      box-shadow: 0 0 0 0.2rem #b9abe0;
      backdrop-filter: blur(12rem);
      border-radius: 2rem;
    }
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    input {
      width: 140px;
    }
  }
`;

const SearchIcon = styled(FaSearch)`
  position: absolute;
  top: 10px;
  left: 165px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.2rem;
  &:hover {
    cursor: pointer;
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    left: 110px;
  }
`;

const DownloadContainer = styled.div`
  position: relative;
  display: inline-block;
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.2rem;
  margin-right: 20px;
  &:hover {
    cursor: pointer;
  }
  &:hover > div {
    display: block;
  }
`;

const Download = styled(BiDownload)`
  color: rgba(255, 255, 255, 0.7);
  font-size: 1.3rem;
  &:hover {
    cursor: pointer;
  }
`;

const DownloadOptions = styled.div`
  display: block;
  position: absolute;
  background-color: rgba(0, 0, 0, 0.7);
  min-width: 100px;
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
  z-index: 1;
  top: 100%;
  left: 50%;
  transform: translateX(-50%);
  border-radius: 5px;
  padding: 5px 0;
`;

const DownloadOption = styled.div`
  color: rgba(255, 255, 255, 0.7);
  padding: 5px 10px;
  text-decoration: none;
  display: block;
  font-size: 1rem;
  &:hover {
    background-color: rgba(255, 255, 255, 0.2);
  }
`;

const Invoicecontainer = styled.div`
  position: relative;
  margin: 0 20px 20px 20px;
  height: 60vh;
  width: 68vw;
  backdrop-filter: blur(10px);
  overflow: hidden;
  overflow-y: scroll;
  &::-webkit-scrollbar {
    width: 12px;
  }

  &::-webkit-scrollbar-track {
    background: rgba(255, 255, 255, 0.1);
    border-radius: 10px;
  }

  &::-webkit-scrollbar-thumb {
    background: rgba(255, 255, 255, 0.15);
    border-radius: 10px;
    border: 3px solid rgba(255, 255, 255, 0.3);
  }

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    width: 93%;
    height: 110%;
    transform: translateX(px);
  }
`;

const InvoiceList = styled.ul`
  margin: 0;
  list-style-type: none;
  padding: 0;
  width: 100%;
`;

const InvoiceItem = styled.li`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
  &:hover {
    background: rgba(0, 0, 0, 0.3);
    cursor: pointer;
  }
`;

const InvoiceNameContainer = styled.div`
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  margin-right: 20px;
`;

const InvoiceName = styled.span`
  color: #ffffff;
  font-size: 1.2rem;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
`;

const UpdateDate = styled.span`
  color: #888;
  font-size: 0.8rem;
  margin-top: 4px;
`;

const ValidationContainer = styled.div`
  display: flex;
  align-items: center;
  margin-right: 10vw;
  width: 16vw;
  flex-shrink: 0;

  @media (max-width: 430px) {
    margin-right: 115px;
    width: 40px;
  }
`;

const ValidationPass = styled.span`
  display: flex;
  align-items: center;
  color: #00ff80;
  font-size: 1.1rem;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 0.8rem;
    margin-right: 30px;
    margin-left: 5px;
  }
`;

const StatusCircleGreen = styled.div`
  width: 6px;
  height: 6px;
  background-color: #00ff00;
  border-radius: 50%;
  margin-right: 10px;
`;

const NotValidated = styled.span`
  display: flex;
  align-items: center;
  color: #eeeeee;
  font-size: 1.1rem;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 0.8rem;
    margin-right: 30px;
    margin-left: 5px;
  }
`;

const StatusCircleGray = styled.div`
  width: 6px;
  height: 6px;
  background-color: #eeeeee;
  border-radius: 50%;
  margin-right: 10px;
`;

const ValidationFail = styled.span`
  display: flex;
  align-items: center;
  color: #ff0000;
  font-size: 1.1rem;

  @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    font-size: 0.8rem;
    margin-right: 30px;
    margin-left: 5px;
  }
`;

const StatusCircleRed = styled.div`
  width: 6px;
  height: 6px;
  background-color: #ff0000;
  border-radius: 50%;
  margin-right: 10px;
`;

const DeleteButton = styled.button`
  background-color: transparent;
  border-radius: 20px;
  border: 1px solid #ff1a1a;
  color: rgba(255, 255, 255, 0.7);
  padding: 4px 15px;
  font-size: 1.1rem;
  cursor: pointer;
  outline: none;
  &:hover {
    background: rgba(255, 10, 40, 0.48);
    transition: all ease 0.5s;
  }
`;

const PaginationContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
  padding: 10px 0;
`;

const PageButton = styled.button`
  background-color: ${(props) =>
    props.disabled ? "rgba(100, 20, 255, 0.5)" : "transparent"};
  color: #ffffff;
  border: 1px solid #6414ff;
  padding: 8px 16px;
  margin: 0 8px;
  cursor: ${(props) => (props.disabled ? "default" : "pointer")};
  border-radius: 20px;
  font-size: 1rem;
  min-width: 40px;
  &:hover {
    background-color: ${(props) =>
      props.disabled ? "rgba(100, 20, 255, 0.5)" : "#6414FF"};
  }
`;
