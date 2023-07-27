import { useState,useRef,useEffect,useContext } from "react";
import AuthContext from "../AuthContext";

function PurchasedItem({purchaseItem}){
    const jwt = useContext(AuthContext);
    const [update, setUpdate] = useState(0);

    function markAsShipped(){
        const headers = {"Authorization" : "Bearer "+jwt};
        fetch("http://localhost:8085/purchase/ship/" + purchaseItem.id, {method:"GET",headers:headers})
        .then(response => response.json());
    }

    function deleteRequest(){
        const headers = {"Authorization" : "Bearer "+jwt};
        fetch("http://localhost:8085/purchase/" + purchaseItem.id, {method:"DELETE",headers:headers})
        .then(response => response.json());
        setUpdate(update + 1);
    }

    return(
        <div>
            <h3>{purchaseItem.name}</h3>
            <h5>{purchaseItem.price}</h5>
            <button onClick = {markAsShipped}>Mark as Shipped</button>
            <button onClick={deleteRequest}>Cancel Purchase Request</button>
        </div>
    );
}

export default PurchasedItem