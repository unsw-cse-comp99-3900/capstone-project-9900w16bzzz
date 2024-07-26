import React from 'react';
import { FaEye, FaEyeSlash } from 'react-icons/fa6';
import styled from 'styled-components';

const Preview = ({ selectedFileType, invoiceId, validationFlag }) => {
    const handlePreviewClick = async () => {
        const token = localStorage.getItem('token');
        const fileTypeMap = {
            "pdf": 3,
            "json": 1,
            "xml": 2
        };
        const fileType = fileTypeMap[selectedFileType];
        
        const response = await fetch(`${process.env.REACT_APP_SERVER_URL}/invoice/download?invoiceId=${invoiceId}&fileType=${fileType}`, {
            method: 'GET',
            headers: {
                'x-access-token': `${token}`
            }
        });
        const blob = await response.blob();
        const url = window.URL.createObjectURL(blob);
        window.open(url, '_blank');
    };

    const isPreviewAllowed = () => {
        if (validationFlag === 0) {
            return true;
        } else if (selectedFileType !== "pdf") {
            return true;
        }
        return false;
    };

    return (
        <PreviewBox>
            {isPreviewAllowed() ? (
                <EyeIcon onClick={handlePreviewClick} />
            ) : (
                <EyeSlashIcon />
            )}
        </PreviewBox>
    );
};

export default Preview;

const PreviewBox = styled.div`
    position: relative;
    margin-top: 20px;
    margin-left: 30px;
    margin-right: 30px;
    height: 240px;
    width: 90%;
    background: rgba(0, 0, 0, 0.3);
    border-radius: 20px;

    @media only screen and (max-width: 430px) and (max-height: 932px) and (-webkit-device-pixel-ratio: 3) {
    margin-top: 30px;
    margin-left: 1px;
    margin-right: 15px;
    height: 220px; /* 可以根据需要调整高度 */
    width: 100%; /* 调整宽度以适应较小屏幕 */
    }
`;

const EyeIcon = styled(FaEye)`
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 2rem;
    &:hover {
        cursor: pointer;
    }
`;

const EyeSlashIcon = styled(FaEyeSlash)`
    position: relative;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    font-size: 2.2rem;
    &:hover {
        cursor: pointer;
    }
`;
