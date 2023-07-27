import React, { useRef, useContext, useState } from 'react';
import Axios from 'axios';
import AuthContext from '../AuthContext';
import { silentJSON, processAlert } from '../FetchRoutines';

async function handleUpload(formData) {
  try {
    const response = await Axios.post(
      'https://api.cloudinary.com/v1_1/dyufhqzen/image/upload',
      formData
    );
    console.log(response);
    return response.data.secure_url;
  } catch (error) {
    console.log(error);
    throw error;
  }
}

function Upload() {
  const [uploadFile, setUploadFile] = useState('');
  const [cloudinaryImage, setCloudinaryImage] = useState('');
  const jwt = useContext(AuthContext);

  const handleSubmit = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append('file', uploadFile);
    formData.append('upload_preset', 'shopping');

    try {
      const imageUrl = await handleUpload(formData);
      setCloudinaryImage(imageUrl);

      let priceCents = parseInt(priceInput.current.value) * 100;
      let stockInt = parseInt(stockInput.current.value);
      let tagArray = tagInput.current.value.split(',');

      const toPost = {
        name: nameInput.current.value,
        description: descInput.current.value,
        image: imageUrl,
        price: priceCents,
        stock: stockInt,
        tags: tagArray
      };

      const headers = {
        Authorization: 'Bearer ' + jwt,
        'Content-type': 'application/json; charset=UTF-8',
      };

      fetch('http://localhost:8085/item', {
        method: 'POST',
        body: JSON.stringify(toPost),
        headers: headers,
      }).then((response) => processAlert(response, 'Item Posted'));
    } catch (error) {
      // Handle error if necessary
    }
  };

  let nameInput = useRef();
  let descInput = useRef();
  let priceInput = useRef();
  let stockInput = useRef();
  let tagInput = useRef();

  return (
    <div className="App">
      <form>
        <h2>Upload Item</h2>
        <div>
          <p>Item Name: <input type="text" ref={nameInput} /></p>
          <p>Item Description: <input type="text" ref={descInput} /></p>
          <p>Item Price: <input type="text" ref={priceInput} /></p>
          <p>Item Stock: <input type="text" ref={stockInput} /></p>
          <p>Item Tags (separate by ,): <input type="text" ref={tagInput} /></p>
          <input type="file" onChange={(event) => { setUploadFile(event.target.files[0]); }} />
        </div>
        <button onClick={handleSubmit}>Upload Item</button>
      </form>
    </div>
  );
}

export default Upload;