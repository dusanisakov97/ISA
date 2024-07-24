import axios from "axios";

const backendUrl = 'http://localhost:8080/reservations';

const makeOrderAPI = async (requestBody) => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        console.log(credentials)
        const response = await axios.post(backendUrl, requestBody, {
            headers: {
                Authorization: credentials
            }
        }); 
        console.log(response.data)
        return response.data;
    } catch (error) {
        console.log(error);
        return null;             
    }
};

export {
    makeOrderAPI
};