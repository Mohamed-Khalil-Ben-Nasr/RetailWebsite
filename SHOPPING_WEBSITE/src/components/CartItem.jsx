function CartItem({cartItem, removeCartList}){

    function removeCartItem(){
        removeCartList(cartItem);
    }

    return(
        <div>
            <img src={cartItem.image} alt="Product Image" />
            <h3>{cartItem.name}</h3>
            <h5>{cartItem.price}</h5>
            <button onClick = {removeCartItem}>Remove</button>
        </div>
    );
}

export default CartItem