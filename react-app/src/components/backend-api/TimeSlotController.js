import axios from "axios";

const backendUrl = 'http://localhost:8080/time-slots/';

const getTimeSlotsByCompany = async (companyId) => {
    try {
        const response = await axios.get(backendUrl + `company/` + companyId); 
        console.log(response.data)
        return response.data;
    } catch (error) {
        console.log(error);
        return null;             
    }
};

export {
    getTimeSlotsByCompany
};