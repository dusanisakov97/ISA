import axios from "axios";

const backendUrl = 'http://localhost:8080/reservations';

const makeOrderAPI = async (requestBody) => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        const response = await axios.post(backendUrl, requestBody, {
            headers: {
                Authorization: 'Basic ' + credentials
            }
        }); 
        console.log(response.data)
        return null;
    } catch (error) {
        console.log(error);
        return error.response.data;             
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
                Authorization: 'Basic ' + credentials
            }
        }); 
        console.log(response.data)
        return response.data;
    } catch (error) {
        console.log(error);
        return null;             
    }
};


const checkIsAllowedToWriteComplaint = async (adminId) => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        const response = await axios.get(backendUrl + `/complaints/company-admin/` + adminId + "/allowed", {
            headers: {
                Authorization: 'Basic ' + credentials
            }
        }); 
        console.log(response.data)
        return true;
    } catch (error) {
        console.log(error);
        return false;             
    }
};

const saveAdminComplaintAPI = async (requestBody) => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        const response = await axios.post(backendUrl + `/complaints/company-admin`, requestBody, {
            headers: {
                Authorization: 'Basic ' + credentials
            }
        }); 
        console.log(response.data)
        return true;
    } catch (error) {
        console.log(error);
        return false;             
    }
};

const checkIsAllowedToWriteComplaintForCompany = async (companyId) => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        const response = await axios.get(backendUrl + `/complaints/company/` + companyId + "/allowed", {
            headers: {
                Authorization: 'Basic ' + credentials
            }
        }); 
        console.log(response.data)
        return true;
    } catch (error) {
        console.log(error);
        return false;             
    }
};

const saveCompanyComplaintAPI = async (requestBody) => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        const response = await axios.post(backendUrl + `/complaints/company`, requestBody, {
            headers: {
                Authorization: 'Basic ' + credentials
            }
        }); 
        console.log(response.data)
        return true;
    } catch (error) {
        console.log(error);
        return false;             
    }
};

const getComplaintHistoryAPI = async () => {
    try {
        const credentials = btoa(`${localStorage.getItem('EMAIL')}:${localStorage.getItem('PASSWORD')}`);
        const response = await axios.get(backendUrl + "/complaints/history", {
            headers: {
                Authorization: 'Basic ' +  credentials
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
    makeOrderAPI, getActiveReservationsAPI, deleteReservationAPI, checkIsAllowedToWriteComplaint, saveAdminComplaintAPI, checkIsAllowedToWriteComplaintForCompany, saveCompanyComplaintAPI, getComplaintHistoryAPI
};