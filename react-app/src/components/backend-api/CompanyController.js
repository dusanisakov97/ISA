import axios from "axios";

const backendUrl = 'http://localhost:8080/';

const getCompanies = async () => {
    try {
        const response = await axios.get(backendUrl + `company`); 
        console.log(response.data)
        return response.data;
    } catch (error) {
        console.log(error);
        return null;             
    }
};

const getProductsByCompany = async (companyId) => {
    try {
        const response = await axios.get(backendUrl + `products/company/` + companyId); 
        console.log(response.data)
        return response.data;
    } catch (error) {
        console.log(error);
        return null;             
    }
};

export {
    getCompanies, getProductsByCompany
};