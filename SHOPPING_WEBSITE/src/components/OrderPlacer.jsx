import React,{ useContext, useState } from 'react'
import { processAlert } from '../FetchRoutines'
import AuthContext from '../AuthContext';

function OrderPlacer({cart, clearCartList}){
    const [confirmed,setConfirmed] = useState(false);
    let leftToPost = 0;
    const jwt = useContext(AuthContext);

    function postOrder(i){
        const toPost = {item: i.iditem};
        const headers = {"Authorization" : "Bearer "+jwt,"Content-type" : "application/json; charset=UTF-8"};
        
        fetch('http://localhost:8085/purchase', {
            method: 'POST',
            body: JSON.stringify(toPost),
            headers: headers
          }).then(response => {leftToPost--;        
            if(leftToPost == 0){
                clearCartList();
            }processAlert(response,"Orders Placed")});

    }

    function splitOrder(){
        for (let index = 0; index < cart.length; index++) {
            let item = cart[index];
            leftToPost++;
            postOrder(item);
            
        }

    }

    if(confirmed == false){
        return(
            <div className='placer'>
                <button onClick = {splitOrder}>Place Order</button>
            </div>
        );
    } else{
        return(        
        <div className='placer'>
            Order Placed
        </div>
    );
    }
}

export default OrderPlacer