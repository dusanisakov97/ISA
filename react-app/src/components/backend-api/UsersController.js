import axios from "axios";

const backendUrl = 'http://localhost:8080/users';

const getCompanyAdminsAPI = async (requestBody) => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        const response = await axios.get(backendUrl + "/company-admins", {
            headers: {
                Authorization: 'Basic ' + credentials
            }
        }); 
        console.log(response.data)
        return response.data;
    } catch (error) {
        console.log(error);
        return []; 
    }
};

export {
    getCompanyAdminsAPI
};