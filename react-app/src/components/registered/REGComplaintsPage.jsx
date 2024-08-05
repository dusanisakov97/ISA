import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import CompanyComplaints from "./complaints/CompanyComplaints";
import AdminComplaints from "./complaints/AdminComplaints";
import ComplaintHistory from "./complaints/ComplaintHistory";


const REGComplaintsPage = () => {
    const [stateOption, setStateOption] = useState([]);

    return(
        <>

        <div className="text-center">
            <button className="btn btn-info mx-2" onClick={() => setStateOption(1)}>Company complaints</button>
            <button className="btn btn-info " onClick={() => setStateOption(2)}>Admin complaints</button>
            <button className="btn btn-info ms-2" onClick={() => setStateOption(3)}>History</button>
        </div>
        {stateOption === 1 && 
            <CompanyComplaints />
        }

        {stateOption === 2 && 
            <AdminComplaints />
        }

        {stateOption === 3 && 
            <ComplaintHistory />
        }
        </>
    );
};

export default REGComplaintsPage;