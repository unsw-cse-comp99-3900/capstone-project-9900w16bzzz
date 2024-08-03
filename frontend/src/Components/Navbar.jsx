import React, { useState, useEffect } from "react";
import { Link as RouterLink, useLocation, useNavigate } from "react-router-dom";
import UserMenu from "./NavbarUserMenu";
import ProtectedLink from "./InvoiceCreationComponents/ProtectedPage";

/**
 * Navbar component for navigation.
 *
 * This component provides a navigation bar with links to different pages,
 * and it handles user authentication, background change on scroll, and menu toggle.
 */
function Navbar() {
  const [nav, setNav] = useState(false);
  const [menuOpen, setMenuOpen] = useState(false);
  const [username, setUsername] = useState(null);
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    /**
     * Checks if the user is authenticated and sets the username state.
     */
    const checkUserAuth = () => {
      const token = localStorage.getItem("token");
      const storedUsername = localStorage.getItem("username");
      if (token && storedUsername) {
        setUsername(storedUsername);
      } else {
        navigate('/');
        setUsername(null);
      }
    };

    checkUserAuth();

    window.addEventListener("localStorageChange", checkUserAuth);
    window.addEventListener("scroll", changeBackground);
    document.addEventListener("click", handleClickOutside);

    return () => {
      window.removeEventListener("localStorageChange", checkUserAuth);
      window.removeEventListener("scroll", changeBackground);
      document.removeEventListener("click", handleClickOutside);
    };
  }, []);

  useEffect(() => {
    setMenuOpen(false);
  }, [location]);

  /**
   * Changes the background of the navbar when the user scrolls.
   */
  const changeBackground = () => {
    if (window.scrollY >= 50) {
      setNav(true);
    } else {
      setNav(false);
    }
  };

  /**
   * Closes the menu if the user clicks outside of it.
   *
   * @param {Event} event - The click event.
   */
  const handleClickOutside = (event) => {
    if (!event.target.closest('.nav') && !event.target.closest('.menu-icon')) {
      setMenuOpen(false);
    }
  };

  /**
   * Toggles the menu open and closed.
   */
  const handleMenuToggle = () => {
    setMenuOpen((prevMenuOpen) => !prevMenuOpen);
  };

  return (
    <nav className={nav ? "nav active" : "nav"}>
      <input className="menu-btn" type="checkbox" id="menu-btn" checked={menuOpen} readOnly />
      <label className="menu-icon" htmlFor="menu-btn" onClick={handleMenuToggle}>
        <span className="nav-icon"></span>
      </label>
      <ul className={menuOpen ? "menu open" : "menu"}>
        <li>
          <RouterLink to="/">Home</RouterLink>
        </li>
        <li>
          <ProtectedLink to="/create-invoice">Create invoice</ProtectedLink>
        </li>
        <li>
          <ProtectedLink to="/my-invoice">My invoice</ProtectedLink>
        </li>
        {username ? (
          <li>
            <UserMenu username={username} />
          </li>
        ) : (
          <li>
            <RouterLink to="/log-in">sign in</RouterLink>
          </li>
        )}
      </ul>
    </nav>
  );
}

export default Navbar;
