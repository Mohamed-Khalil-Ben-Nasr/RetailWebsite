function ProductItem({item, addToCartList, showDetails}){

    function addToCartItem(){
        addToCartList(item);
    }

    function showDetailItem(){
        showDetails(item);
    }

    return(
        <div>
            <img src={item.image} alt="Product Image" />
            <h3>{item.name}</h3>
            <h5>{item.price / 100}</h5>
            <button onClick = {addToCartItem}>Add to Cart</button>
            <button onClick = {showDetailItem}>See Details</button>
        </div>
    );
}

export default ProductItem