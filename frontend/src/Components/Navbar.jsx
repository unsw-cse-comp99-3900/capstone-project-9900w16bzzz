import React, {useState, useEffect} from "react";
import { Link as RouterLink } from "react-router-dom";
import UserMenu from "./NavbarUserMenu";
import ProtectedLink from "./InvoiceCreationComponents/ProtectedPage";

function Navbar(){
    const [nav, setnav] = useState(false);
    const [username, setUsername] = useState(null);

    useEffect(() => {
        const checkUserAuth = () => {
            const token = localStorage.getItem('token');
            const storedUsername = localStorage.getItem('username');
            if (token && storedUsername) {
                setUsername(storedUsername);
            } else {
                setUsername(null);
            }
        }

        checkUserAuth();

        window.addEventListener('localStorageChange', checkUserAuth);

        return () => {
            window.removeEventListener('localStorageChange', checkUserAuth);
        };
    }, []);
    const changeBackground = ()=>{
        if (window.scrollY >= 50) {
            setnav(true);
        }
        else{
            setnav(false);
        }
    }
    window.addEventListener('scroll', changeBackground);
    return (
        <nav className={nav ? "nav active" :"nav"}>
            <input className="menu-btn" type = "checkbox" id = "menu-btn"></input>
            <label className="menu-icon" htmlFor="menu-btn">
                <span className="nav-icon"></span>
            </label>
            <ul className="menu">
                <li><RouterLink to = "/">Home</RouterLink></li>
                <li><ProtectedLink to="/create-invoice">Create invoice</ProtectedLink></li>
                <li><ProtectedLink to = "/my-invoice">My invoice</ProtectedLink></li>
                {username ? (
                    <li><UserMenu username={username} /></li>
                ) : (
                    <li><RouterLink to="/log-in">sign in</RouterLink></li>
                )}
            </ul>

        </nav>
    )
}
export default Navbar;