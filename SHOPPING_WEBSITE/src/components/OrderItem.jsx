import { useState,useRef,useEffect,useContext } from "react";
import AuthContext from "../AuthContext";

function OrderItem({orderItem}){
    const jwt = useContext(AuthContext);

    return(
        <div>
            <img src={orderItem.image} alt="Product Image" />
            <h3>{orderItem.name}</h3>
            <h5>{orderItem.price}</h5>
        </div>
    );
}

export default OrderItem