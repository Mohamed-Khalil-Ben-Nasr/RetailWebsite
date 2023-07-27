import React,{ useRef, useContext, useState } from 'react'
import CartItem from './CartItem';
import OrderPlacer from './OrderPlacer';

function ShoppingCart({cart, removeCartApp, clearCart}){
    const [update, setUpdate] = useState(0);

    function removeCartList(cartItem){
        removeCartApp(cartItem);
        setUpdate(update + 1);
    }

    function clearCartList(){
        clearCart();
    }

    return(
        <div>  
            <h2>My Cart</h2>
            {cart.map((e) => {return(<CartItem cartItem = {e} removeCartList={removeCartList}/>)})}
            {<OrderPlacer cart = {cart} clearCartList={clearCartList}/>};
        </div>
    );
}

export default ShoppingCart