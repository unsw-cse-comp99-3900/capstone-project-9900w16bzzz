import React, {useState} from "react";
import {Link} from "react-scroll";
import { Link as RouterLink } from "react-router-dom";

function Navbar(){
    const [nav, setnav] = useState(false);
    const [token, setToken] = useState(null);
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
                <li><RouterLink to = "/create-invoice">Create invoice</RouterLink></li>
                <li><Link to = "#">My invoice</Link></li>
                {token ? (
                    <li>
                        {username} 
                    </li>
                ) : (
                    <li>
                        <RouterLink to="/sign-up">Sign up</RouterLink>
                    </li>
                )}
            </ul>

        </nav>
    )
}
export default Navbar;