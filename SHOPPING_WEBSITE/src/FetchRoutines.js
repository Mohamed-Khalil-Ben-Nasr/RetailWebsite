export function processJSON(response) {
    if(response.ok) 
        return response.json();
    if(response.status == 401) // Not authorized
        alert("Your login has expired. Please log in again.");
    return Promise.reject(response);
}

export function silentJSON(response) {
    if(response.ok) 
        return response.json();
    return Promise.reject(response);
}

export function processAlert(response,message) {
    if(response.ok)
        alert(message);
    if(response.status == 401) // Not authorized
        alert("Your login has expired. Please log in again.");
    return Promise.reject(response);
}

export function processText(response) {
    if(response.ok) 
        return response.text();
    if(response.status == 401) // Not authorized
        alert("Your login has expired. Please log in again.");
    return Promise.reject(response);
}