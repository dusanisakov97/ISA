import axios from "axios";

const backendUrl = 'http://localhost:8080/';

const registerAPI = async (requestBody) => {
    try {
        const response = await axios.post(backendUrl + `identity/register`, requestBody, {headers: {
            'Content-Type': 'application/json'
        }}); 
        return [response.data, null];
    } catch (error) {
        console.log(error)
        return [null, error.response.data];             
    }
};

const signInAPI = async (requestBody) => {
    try {
        const response = await axios.post(backendUrl + `identity/sign-in`, requestBody, {headers: {
            'Content-Type': 'application/json'
        }}); 
        return [response.data, null];
    } catch (error) {
        console.log(error)
        return [null, error.response.data];             
    }
};

export {
    registerAPI, signInAPI
};