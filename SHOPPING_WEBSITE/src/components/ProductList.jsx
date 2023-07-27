import { useContext, useState, useRef, useEffect } from 'react'
import AuthContext from "../AuthContext";
import ProductItem from './ProductItem';
import ProductDetail from './ProductDetail';

function ProductList({addToCartApp}) {
    const [items, setItems] = useState([]);
    const [searchedItems, setSearchedItems] = useState([]);
    const [details, setDetails] = useState(false);
    const [detailedItem, setDetailedItem] = useState(null);
    const [isSearching, setIsSearching] = useState(false);
    const jwt = useContext(AuthContext);
    let searchInput = useRef();
    useEffect(() => {getItems()},[]);

    function getItems(){
        const headers = {"Authorization" : "Bearer "+jwt};
        fetch("http://localhost:8085/item/all ", {method:"GET",headers:headers}).then(response => response.json())
        .then(response=>{setItems(response)});
    }

    function addToCartList(item){
        addToCartApp(item);
    }

    function showDetails(item){
        setDetailedItem(item);
        setDetails(true);
    }

    function hideDetails(){
        setDetailedItem(null);
        setDetails(false);
    }

    function search(){
        const headers = {"Authorization" : "Bearer "+jwt};
        fetch("http://localhost:8085/item?tag=" + searchInput.current.value, {method:"GET",headers:headers}).then(response => response.json())
        .then(response=>{setSearchedItems(response)}).then(setIsSearching(true));

    }

    function displayAll(){
        setIsSearching(false);
    }


    if(details == false && isSearching == false){
        return(
            <div>  
                <h2>Catalog</h2>
                <p>Search by Tag: <input type="text" ref={searchInput} /></p>
                <button onClick={search}>Search</button> <button onClick={displayAll}>Display All</button>
                {items.map((e) => {return(<ProductItem item = {e} addToCartList={addToCartList} showDetails={showDetails}/>)})}
            </div>
        );
    }
    else if(details == false){
        return(
            <div>
                <h2>Catalog</h2>
                <p>Search by Tag: <input type="text" ref={searchInput} /></p>
                <button onClick={search}>Search</button> <button onClick={displayAll}>Display All</button>
                {searchedItems.map((e) => {return(<ProductItem item = {e} addToCartList={addToCartList} showDetails={showDetails}/>)})}
            </div>
        );
    }
    else{
        return(
            <div>  
                <h2>Product Details</h2>
                {<ProductDetail item = {detailedItem} addToCartList={addToCartList} hideDetails = {hideDetails}/>}
            </div>
        );
    }

}

export default ProductList