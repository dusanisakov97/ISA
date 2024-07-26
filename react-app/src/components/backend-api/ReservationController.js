import axios from "axios";

const backendUrl = 'http://localhost:8080/reservations';

const makeOrderAPI = async (requestBody) => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
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

const getActiveReservationsAPI = async () => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        const response = await axios.get(backendUrl + "/active", {
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

const deleteReservationAPI = async (id) => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        const response = await axios.delete(backendUrl + "/" + id, {
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
    makeOrderAPI, getActiveReservationsAPI, deleteReservationAPI
};