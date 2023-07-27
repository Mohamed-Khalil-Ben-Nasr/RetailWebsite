import { useState,useRef,useEffect,useContext } from "react";
import AuthContext from "../AuthContext";
import PurchasedItem from "./PurchasedItem";
import { silentJSON, processAlert } from "../FetchRoutines";

function PurchasedItemDisplay(){
    const jwt = useContext(AuthContext);
    const [purchased, setPurchased] = useState([]);
    const [items,setItems] = useState([]);
    useEffect(() => {getPurchases()},[]);

    function getPurchases() {
        const headers = {"Authorization" : "Bearer "+ jwt};
        fetch("http://localhost:8085/purchase/forseller", {method:"GET",headers:headers}).then(response => response.json())
        .then(response=>{setPurchased(response);return response;}).then(response => {getItems(response)});
    }

    function getItems(p){
        for (let index = 0; index < p.length; index++) {
            const current = p[index];
            const headers = {"Authorization" : "Bearer "+ jwt};
            fetch("http://localhost:8085/item/" + current.item, {method:"GET",headers:headers}).then(response => response.json())
            .then(response=>{
                setItems([...items,response])})
        }
    }

    if(purchased.length == 0){
        return(
            <h2>No Purchases Yet</h2>
        );
    }else{
        return(
            <div>  
                <h2>Purchased Orders</h2>
                {items.map((e) => {return(<PurchasedItem purchaseItem = {e}/>)})}
            </div>
        );
    }
}

export default PurchasedItemDisplay