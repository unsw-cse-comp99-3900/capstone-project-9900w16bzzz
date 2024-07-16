import React from 'react';
import { FaEye, FaEyeSlash } from 'react-icons/fa6';
import styled from 'styled-components';

const Preview = ({ selectedFileType, handlePreviewClick }) => {
    return (
        <PreviewBox>
            {selectedFileType === "pdf" ? (
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
