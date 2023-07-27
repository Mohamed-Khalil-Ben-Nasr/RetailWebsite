function ProductDetail({item, addToCartList, hideDetails}){

    function addToCartItem(){
        addToCartList(item);
    }

    function hideDetailsItem(){
        hideDetails();
    }

    return(
        <div>
            <img src={item.image} alt="Product Image" />
            <h3>{item.name}</h3>
            <h5>{item.price}</h5>
            <p>{item.description}</p>
            <button onClick = {addToCartItem}>Add to Cart</button>
            <button onClick = {hideDetailsItem}>Back to Catalog</button>
        </div>
    );
}

export default ProductDetail