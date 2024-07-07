import React from 'react';
import { Link as RouterLink, useNavigate } from 'react-router-dom';

const ProtectedLink = ({ to, children }) => {
  const navigate = useNavigate();

  const handleClick = (e) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    if (token) {
      navigate(to);
    } else {
      alert('Please log in to access this page.');
      navigate('/log-in');
    }
  };

  return (
    <RouterLink to={to} onClick={handleClick}>
      {children}
    </RouterLink>
  );
};

export default ProtectedLink;