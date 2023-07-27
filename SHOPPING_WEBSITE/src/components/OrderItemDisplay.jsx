import { useState,useRef,useEffect,useContext } from "react";
import AuthContext from "../AuthContext";
import OrderItem from "./OrderItem";
import { silentJSON, processAlert } from "../FetchRoutines";

function OrderItemDisplay(){
    const jwt = useContext(AuthContext);
    const [orders, setOrders] = useState([]);
    useEffect(() => {getPurchases()},[]);

    function getOrders() {
        const headers = {"Authorization" : "Bearer "+ jwt};
        fetch("http://localhost:8085/purchase/forseller", {method:"GET",headers:headers}).then(response => response.json())
        .then(response=>{setPurchased(response)});
    }

    if(orders.length == 0){
        return(
            <h2>No Orders Yet</h2>
        );
    }else{
        return(
            <div>  
                <h2>My Orders</h2>
                {orders.map((e) => {return(<OrderItem orderItem = {e}/>)})}
            </div>
        );
    }
}

export default OrderItemDisplay