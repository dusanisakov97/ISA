import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getCompanies } from "../../backend-api/CompanyController";


const CompanyComplaints = () => {

    const [stateCompanies, setStateCompanies] = useState([]);

    useEffect(() => {
        populateCompanies();
    }, []);

    const populateCompanies = async () => {
        const companies = await getCompanies();
        setStateCompanies(companies);
    };

    return(
        <>
             <div className="container p-2">
                <div className="row">
                    {stateCompanies && stateCompanies.map(iterable => (
                    <div className="col col-md-4">
                        <div className="card" >
                            <div className="card-body">
                                <h5 className="card-title">{iterable.name}</h5>
                                <p className="card-text">{iterable.description}</p>
                                <Link to={"/registered/complaints/company/" + iterable.id} className="btn btn-primary">Write complaint</Link>
                            </div>
                        </div>
                    </div>
                    ))}
                </div>
            </div>
        </>
    );
};

export default CompanyComplaints;