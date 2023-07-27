import React, { useRef } from 'react';
import { Link } from 'react-router-dom';
import { FaBars, FaTimes } from "react-icons/fa";
import "./navbar.css";
import logo from './logo.jpg'

function Navbar() {
  const navRef = useRef();

  const showNavbar = () => {
    navRef.current.classList.toggle("responsive_nav");
  };

  return (
    <header>
        <div>
            <img className='logo'src={logo} alt="logo"></img>
        </div>
      <nav ref={navRef}>
        <div className="gpt3__navbar-links_container">
          <p><Link to="/">Home</Link></p>
          <p><Link to="/catalog">Catalog</Link></p>
          <p><Link to="/cart">Cart</Link></p>
          <p><Link to="/seller_Section">Seller Section</Link></p>
          <p><Link to="/profile">Profile</Link></p>
          <div className="signIn_container">
            <Link to="/login">
              <button type="button">Log In / Register</button>
            </Link>
          </div>
        </div>
        <button className="nav-btn nav-close-btn" onClick={showNavbar}>
          <FaTimes />
        </button>
      </nav>
      <button className='nav-btn' onClick={showNavbar}>
        <FaBars />
      </button>
    </header>
  );
}

export default Navbar;