import './App.css';
import { Routes, Route, BrowserRouter} from "react-router-dom";
import { useState } from 'react'
import Navbar from "./components/navbar"
import ProductList from './components/ProductList';
import LoginPage from './components/LoginPage';
import ShoppingCart from './components/ShoppingCart';
import Home from './components/Home';
import SellerSection from './components/SellerSection';
import UserAccount from './components/UserAccount';
import AuthContext from './AuthContext'

function App() {
  const [cart, setCart] = useState([]);
  const [jwt, setJwt] = useState("");
  

  function addToCartApp(item){
    const newCart = [...cart];      
    newCart.push(item);
    setCart(newCart);
  }

  function removeCartApp(cartItem){
    let newCart = cart;
    let index = newCart.indexOf(cartItem);
    newCart.splice(index,1);
    setCart(newCart);
  }
  
  function clearCart(){
    setCart([]);
  }

  return (
    <>
    <AuthContext.Provider value = {jwt}>
      <BrowserRouter>
        <Navbar></Navbar>

        <Routes>
          <Route path = "/" element ={<Home/>} />
          <Route path = "/catalog" element ={<ProductList addToCartApp={addToCartApp}/>} />
          <Route path = "/cart" element ={<ShoppingCart cart = {cart} removeCartApp={removeCartApp} clearCart={clearCart}/>} />
          <Route path = "/seller_Section" element ={<SellerSection/>} />
          <Route path = "/login" element ={<LoginPage setJwt={setJwt}/>} />
          <Route path = "/profile" element ={<UserAccount/>} />
        </Routes>
      </BrowserRouter>
    </AuthContext.Provider>
    </>

  )
}

export default App