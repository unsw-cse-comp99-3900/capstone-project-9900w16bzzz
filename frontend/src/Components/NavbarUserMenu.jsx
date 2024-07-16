import React, { useState, useRef, useEffect } from 'react';
import styled from 'styled-components';
import { Link } from 'react-router-dom';

const UserMenuContainer = styled.div`
  position: relative;
  display: inline-block;
  @media (max-width: 1100px) {
    display: flex;
    justify-content: center;
    width: 100%;
  }

`;


const UserNameDisplay = styled.div`
  font-family: 'Lato';
  height: 40px;
  line-height: 40px;
  margin: 3px;
  padding: 0px 22px;
  font-size: 0.9rem;
  text-transform: uppercase;
  font-weight: 500;
  color: #ffffff;
  letter-spacing: 1px;
  border-radius: 20px;
  transition: 0.2s ease-in-out;
  cursor: pointer;

  &:hover {
    background-color: #6414FF;
    color: #ffffff;
    box-shadow: 5px 10px 30px rgba(64, 64, 198, 0.411);
  }

  @media (max-width: 1100px) {
    width: 100%;
    text-align: center;
  }
`;

const DropdownContent = styled.div`
  position: absolute;
  left: 50%;
  top: 100%;
  transform: translateX(-50%);
  margin-top: 5px;
  min-width: 100px;
  background-color: rgba(0, 0, 0, 0.5);
  border-radius: 10px;
  box-shadow: 0 8px 32px 0 rgba(31, 38, 135, 0.37);
  backdrop-filter: blur(2px);
  z-index: 1;
  overflow: hidden;
  border: 2px solid #6414FF;
  opacity: ${props => props.show ? 1 : 0};
  visibility: ${props => props.show ? 'visible' : 'hidden'};
  transition: opacity 0.2s ease-in-out, visibility 0.2s ease-in-out;
`;

const DropdownItem = styled(Link)`
  color: white;
  padding: 12px 16px;
  text-decoration: none;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: 0.2s ease-in-out;
  font-weight: 500;

  &:hover {
    background-color: rgba(100, 20, 255, 0.5);
  }
`;

const UsernameDisplay = styled.div`
  color: white;
  padding: 12px 16px;
  text-decoration: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
`;

const UserMenu = ({ username }) => {
  const [showDropdown, setShowDropdown] = useState(false);
  const timeoutRef = useRef(null);

  const handleMouseEnter = () => {
    clearTimeout(timeoutRef.current);
    setShowDropdown(true);
  };

  const handleMouseLeave = () => {
    timeoutRef.current = setTimeout(() => {
      setShowDropdown(false);
    }, 1000);  // 1 second delay
  };

  useEffect(() => {
    return () => {
      if (timeoutRef.current) {
        clearTimeout(timeoutRef.current);
      }
    };
  }, []);

  return (
    <UserMenuContainer 
      onMouseEnter={handleMouseEnter} 
      onMouseLeave={handleMouseLeave}
    >
      <UserNameDisplay>{username}</UserNameDisplay>
      <DropdownContent show={showDropdown}>
        <UsernameDisplay>{username}</UsernameDisplay>
        <DropdownItem to="/settings">Settings</DropdownItem>
        <DropdownItem to="/reset-password">Reset Password</DropdownItem>
        <DropdownItem to="/" onClick={()=>{
            localStorage.removeItem('username');
            localStorage.removeItem('token');
            window.dispatchEvent(new Event('localStorageChange'));
        }}>Log out</DropdownItem>
      </DropdownContent>
    </UserMenuContainer>
  );
};

export default UserMenu;